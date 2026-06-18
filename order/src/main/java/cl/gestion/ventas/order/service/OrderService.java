package cl.gestion.ventas.order.service;

import java.util.List;
import java.util.NoSuchElementException;


import org.springframework.stereotype.Service;

import cl.gestion.ventas.order.client.CartClient;
import cl.gestion.ventas.order.client.InventoryClient;
import cl.gestion.ventas.order.dto.CartResponse;
import cl.gestion.ventas.order.dto.InventoryRequest;
import cl.gestion.ventas.order.dto.OrderRequest;
import cl.gestion.ventas.order.dto.OrderResponse;
import cl.gestion.ventas.order.dto.OrderResponseForShipping;
import cl.gestion.ventas.order.dto.OrderSmallResponse;
import cl.gestion.ventas.order.dto.OrderStatusUpdate;
import cl.gestion.ventas.order.mapper.OrderMapper;
import cl.gestion.ventas.order.model.Order;
import cl.gestion.ventas.order.model.OrderStatus;
import cl.gestion.ventas.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final CartClient cartClient;

    private final InventoryClient inventoryClient;

    @Transactional
    public List<OrderResponse> obtenerOrdenes() {
        log.info("Obteniendo todas las ordenes");
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::toResponse).toList();
    }

    @Transactional
    public OrderResponse obtenerOrdenPorId(Long id) {
        log.info("Obteniendo orden por su ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro la orden con esa id"));
        return orderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponseForShipping obtenerOrdenParaEnvio(Long id) {
        log.info("Obteniendo orden para envío con ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro la orden con esa id"));
        return orderMapper.toResponseForShipping(order);
    }

    @Transactional
    public OrderSmallResponse obtenerOrdenResumidaPorId(Long id) {
        log.info("Obteniendo orden por su ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro la orden con esa id"));
        return orderMapper.toSmallResponse(order);
    }

    @Transactional
    public List<OrderResponse> obtenerOrdenesPorUserId(Long userId) {
        log.info("Obteniendo ordenes de usuario: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(orderMapper::toResponse).toList();
    }

    @Transactional
    public OrderResponse crear(OrderRequest request) {
        log.info("Creando nueva orden");
        CartResponse cart = cartClient.getCartByUserId(request.getUserId());

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        List<InventoryRequest> items = orderMapper.toInventoryRequestDecrement(cart.getItems());

        log.info("Descontando stock de productos en Servicio de Inventario");
        inventoryClient.actualizarStock(items);

        Order nuevaOrden = orderMapper.fromCartResponse(request, cart);

        Order ordenGuardada = orderRepository.save(nuevaOrden);

        log.info("Orden {} guardada exitosamente, vaciando carrito", ordenGuardada.getId());

        cartClient.cleanCartByUserId(request.getUserId());

        return orderMapper.toResponse(ordenGuardada);
    }

    @Transactional
    public OrderResponse editarEstado(Long orderId, OrderStatusUpdate update) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Orden no encontrada"));

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException(
                    "No se puede modificar la orden " + order.getId() + " porque su estado es: " + order.getStatus());
        }

        if (order.getStatus() == OrderStatus.SHIPPED && update.getNewStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException(
                    "No se puede modificar la orden a un estado previo o cancelarse una vez enviada");
        }

        log.info("Cambiando estado de orden {} de {} a {}", orderId, order.getStatus(), update.getNewStatus());

        if (update.getNewStatus() == OrderStatus.CANCELED) {
            log.info("Restaurando stock de productos");
            List<InventoryRequest> items = orderMapper.toInventoryRequestRestore(order.getItems());
            inventoryClient.actualizarStock(items);
        }

        order.setStatus(update.getNewStatus());

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toResponse(updatedOrder);

    }

    @Transactional
    public void eliminarOrden(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Orden no encontrada"));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("No se puede eliminar una orden entregada");
        }

        if (order.getStatus() != OrderStatus.CANCELED) {
            List<InventoryRequest> items = orderMapper.toInventoryRequestRestore(order.getItems());
            inventoryClient.actualizarStock(items);
            log.info("Stock restaurado para la orden {}", orderId);
        }

        orderRepository.delete(order);
        log.info("Orden {} eliminada", orderId);
    }
}

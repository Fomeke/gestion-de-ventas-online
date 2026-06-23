package cl.gestion.ventas.order.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.gestion.ventas.order.client.CartClient;
import cl.gestion.ventas.order.client.InventoryClient;
import cl.gestion.ventas.order.dto.CartItemDTO;
import cl.gestion.ventas.order.dto.CartResponse;
import cl.gestion.ventas.order.dto.InventoryRequest;
import cl.gestion.ventas.order.dto.OrderRequest;
import cl.gestion.ventas.order.dto.OrderResponse;
import cl.gestion.ventas.order.dto.OrderStatusUpdate;
import cl.gestion.ventas.order.mapper.OrderMapper;
import cl.gestion.ventas.order.model.Order;
import cl.gestion.ventas.order.model.OrderStatus;
import cl.gestion.ventas.order.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CartClient cartClient;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testObtenerOrdenes(){
        Order order = new Order();
        OrderResponse response = new OrderResponse();
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toResponse(order)).thenReturn(response);

        List<OrderResponse> resultado = orderService.obtenerOrdenes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(orderRepository,times(1)).findAll();
        
    }

    @Test
    public void testObtenerOrdenPorId(){
        Long id = 69L;
        Order order = new Order();
        OrderResponse response = new OrderResponse();

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(response);

        OrderResponse resultado = orderService.obtenerOrdenPorId(id);

        assertNotNull(resultado);
        verify(orderRepository,times(1)).findById(id);
    }

    @Test
    public void testObtenerOrdenesPorUsuario(){
        Long userId = 67L;
        Order order = new Order();
        OrderResponse response = new OrderResponse();
        
        when(orderRepository.findByUserId(userId)).thenReturn(List.of(order));
        when(orderMapper.toResponse(order)).thenReturn(response);

        List<OrderResponse> resultado = orderService.obtenerOrdenesPorUserId(userId);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(orderRepository,times(1)).findByUserId(userId);
    }

    @Test
    public void testCrear(){
        Long userId = 1L;
        OrderRequest request = OrderRequest.builder().userId(userId).build();
        CartItemDTO item = CartItemDTO.builder().productId(666L).quantity(2).build();
        CartResponse cart = CartResponse.builder().items(List.of(item)).build();
        
        List<InventoryRequest> inventorios = List.of(new InventoryRequest());
        Order newOrder = new Order();
        Order savedOrder = new Order();
        OrderResponse response = new OrderResponse();

        when(cartClient.getCartByUserId(userId)).thenReturn(cart);
        when(orderMapper.toInventoryRequestDecrement(cart.getItems())).thenReturn(inventorios);
        doNothing().when(inventoryClient).actualizarStock(inventorios);
        when(orderMapper.fromCartResponse(request, cart)).thenReturn(newOrder);
        when(orderRepository.save(newOrder)).thenReturn(savedOrder);
        doNothing().when(cartClient).cleanCartByUserId(userId);
        when(orderMapper.toResponse(savedOrder)).thenReturn(response);

        OrderResponse resultado = orderService.crear(request);

        assertNotNull(resultado);
        verify(cartClient,times(1)).getCartByUserId(userId);
        verify(inventoryClient,times(1)).actualizarStock(inventorios);
        verify(orderRepository,times(1)).save(newOrder);
        verify(cartClient,times(1)).cleanCartByUserId(userId);
    }

    @Test
    public void testEditarEstado(){
        Long id = 67L;
        Order order = Order.builder().id(id).status(OrderStatus.PAID).items(new ArrayList<>()).build();

        OrderStatusUpdate update = OrderStatusUpdate.builder().newStatus(OrderStatus.CANCELED).build();

        List<InventoryRequest> restoreRequests = List.of(new InventoryRequest());
        Order updatedOrder = new Order();
        OrderResponse response = new OrderResponse();

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderMapper.toInventoryRequestRestore(order.getItems())).thenReturn(restoreRequests);
        doNothing().when(inventoryClient).actualizarStock(restoreRequests);
        when(orderRepository.save(order)).thenReturn(updatedOrder);
        when(orderMapper.toResponse(updatedOrder)).thenReturn(response);

        OrderResponse resultado = orderService.editarEstado(id, update);

        assertNotNull(resultado);
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(inventoryClient,times(1)).actualizarStock(restoreRequests);
        verify(orderRepository,times(1)).save(order);
    }

    @Test
    public void testEliminarOrder(){
        Long id = 67L;
        Order order = Order.builder().status(OrderStatus.PAID).items(new ArrayList<>()).build();
        List<InventoryRequest> restoreRequests = List.of(new InventoryRequest());

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderMapper.toInventoryRequestRestore(order.getItems())).thenReturn(restoreRequests);
        doNothing().when(inventoryClient).actualizarStock(restoreRequests);
        doNothing().when(orderRepository).delete(order);

        assertDoesNotThrow((Executable) () -> orderService.eliminarOrden(id));
        verify(inventoryClient,times(1)).actualizarStock(restoreRequests);
        verify(orderRepository,times(1)).delete(order);
    }

    @Test
    void testObtenerOrdenPorId_NoExiste(){
        Long orderId = 100L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            orderService.obtenerOrdenPorId(orderId);
        });

        assertEquals("No se encontro la orden con esa id", exception.getMessage());
        
        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void testCrear_CarritoVacio(){
        Long userId = 1L;
        OrderRequest request = OrderRequest.builder().userId(userId).build();

        CartResponse mockCartResponse = new CartResponse();
        mockCartResponse.setItems(Collections.emptyList());

        when(cartClient.getCartByUserId(userId)).thenReturn(mockCartResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.crear(request);
        });

        assertEquals("El carrito está vacío", exception.getMessage());

        verify(cartClient, times(1)).getCartByUserId(userId);
        verifyNoInteractions(inventoryClient);
        verifyNoInteractions(orderRepository);
        verify(cartClient, never()).cleanCartByUserId(anyLong());
    }

    @Test
    void testEditarEstado_Error_OrdenFinalizada(){
        Long orderId = 10L;
        OrderStatusUpdate update = new OrderStatusUpdate(OrderStatus.SHIPPED); // Intento de cambio inválido
        
        Order order = Order.builder().id(orderId).status(OrderStatus.DELIVERED).build(); 

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.editarEstado(orderId, update);
        });

        assertTrue(exception.getMessage().contains("No se puede modificar la orden"));
        assertTrue(exception.getMessage().contains("DELIVERED"));

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }
}

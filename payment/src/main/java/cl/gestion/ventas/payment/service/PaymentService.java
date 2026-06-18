package cl.gestion.ventas.payment.service;

import java.util.List;
import java.util.NoSuchElementException;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.gestion.ventas.payment.Repository.PaymentRepository;
import cl.gestion.ventas.payment.client.OrderClient;
import cl.gestion.ventas.payment.dto.OrderResponse;
import cl.gestion.ventas.payment.dto.PaymentRequest;
import cl.gestion.ventas.payment.dto.PaymentResponse;
import cl.gestion.ventas.payment.mapper.PaymentMapper;
import cl.gestion.ventas.payment.model.Payment;
import cl.gestion.ventas.payment.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper mapper;

    private final PaymentRepository repo;

    private final OrderClient client;

    public List<PaymentResponse> obtenerPagos() {
        log.info("Obteniendo lista de pagos..");
        List<Payment> payment = repo.findAll();
        return payment.stream().map(mapper::toResponse).toList();
    }

    public PaymentResponse obtenerPagoPorId(Long id) {
        log.info("Obteniendo pago por id: {}", id);
        Payment payment = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro el pago con esa id"));
        return mapper.toResponse(payment);
    }

    @Transactional
    public PaymentResponse procesarPago(PaymentRequest request, String token) {
        log.info("Procesando pago para la orden ID: {}", request.getOrderId());

        OrderResponse order = client.obtenerOrden(request.getOrderId(), token);

        if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalArgumentException(
                    "La Orden tiene un estado no válido, sólo se acepta PENDIENTE (PENDING)");
        }

        if (order.getTotal().compareTo(request.getAmount()) != 0) {
            throw new IllegalArgumentException("El monto a pagar no coincide con el total de la orden");
        }

        Payment payment = mapper.fromRequest(request);

        client.actualizarEstado(order.getId(), "PAID", token);
        payment.setStatus(PaymentStatus.SUCCESS);

        return mapper.toResponse(repo.save(payment));
    }

    @Transactional
    public PaymentResponse actualizarPago(Long id, PaymentRequest request, String token) {
        Payment payment = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pago no encontrado"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException("No se puede modificar un pago ya completado");
        }

        OrderResponse order = client.obtenerOrden(request.getOrderId(), token);
        if (order.getTotal().compareTo(request.getAmount()) != 0) {
            throw new IllegalArgumentException("El monto no coincide con el total de la orden");
        }

        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        
        client.actualizarEstado(order.getId(), "PAID", token);
        payment.setStatus(PaymentStatus.SUCCESS);

        return mapper.toResponse(repo.save(payment));
    }

    @Transactional
    public void eliminarPago(Long id, String token) {
        Payment payment = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pago no encontrado"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException("No se puede eliminar un pago exitoso; debe procesarse un reembolso");
        }

        repo.delete(payment);
        log.info("Pago {} eliminado", id);
    }
}

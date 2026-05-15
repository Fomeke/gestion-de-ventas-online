package cl.gestion.ventas.payment.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentMapper mapper;

    @Autowired
    private PaymentRepository repo;

    @Autowired
    private OrderClient client;


    public List<PaymentResponse> obtenerPagos(){
        log.info("Obteniendo lista de pagos..");
        List<Payment> payment = repo.findAll();
        return payment.stream().map(mapper::toResponse).toList();
    }

    public PaymentResponse obtenerPagoPorId(Long id){
        log.info("Obteniendo pago por id: {}", id);
        Payment payment = repo.findById(id).orElseThrow(() -> new NoSuchElementException("No se encontro el pago con esa id"));
        return mapper.toResponse(payment);
    }

    @Transactional
    public PaymentResponse procesarPago(PaymentRequest request, String token){
        log.info("Procesando pago para la orden ID: {}",request.getOrderId());

        OrderResponse order = client.obtenerOrden(request.getOrderId(), token);


        if(!"PENDING".equalsIgnoreCase(order.getStatus())){
            throw new IllegalArgumentException("La Orden tiene un estado no válido, sólo se acepta PENDIENTE (PENDING)");
        }

        if(order.getTotal().compareTo(request.getAmount()) != 0){
            throw new IllegalArgumentException("El monto a pagar no coincide con el total de la orden");
        }

        Payment payment = mapper.fromRequest(request);

        client.actualizarEstado(order.getId(), "PAID", token);
        payment.setStatus(PaymentStatus.SUCCESS);

        return mapper.toResponse(repo.save(payment));
    }

    
}

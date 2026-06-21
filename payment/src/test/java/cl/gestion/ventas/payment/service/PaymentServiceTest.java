package cl.gestion.ventas.payment.service;

import cl.gestion.ventas.payment.Repository.PaymentRepository;
import cl.gestion.ventas.payment.client.OrderClient;
import cl.gestion.ventas.payment.dto.OrderResponse;
import cl.gestion.ventas.payment.dto.PaymentRequest;
import cl.gestion.ventas.payment.dto.PaymentResponse;
import cl.gestion.ventas.payment.mapper.PaymentMapper;
import cl.gestion.ventas.payment.model.Payment;
import cl.gestion.ventas.payment.model.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repo;

    @Mock
    private PaymentMapper mapper;

    @Mock
    private OrderClient client;

    @InjectMocks
    private PaymentService servi;

    @Test
    void procesarPago_Exito() {
        
        String token = "Bearer m1t0k3n";
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(10L);
        request.setAmount(new BigDecimal("50000.00"));

        OrderResponse order = new OrderResponse();
        order.setId(10L);
        order.setStatus("PENDING"); 
        order.setTotal(new BigDecimal("50000.00"));

        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();
        response.setStatus(PaymentStatus.SUCCESS);

    
        when(client.obtenerOrden(10L, token)).thenReturn(order);
        when(mapper.fromRequest(request)).thenReturn(payment);
        when(repo.save(payment)).thenReturn(payment);
        when(mapper.toResponse(payment)).thenReturn(response);


        PaymentResponse resultado = servi.procesarPago(request, token);

        
        assertEquals(PaymentStatus.SUCCESS, resultado.getStatus());
        
        
        verify(client, times(1)).actualizarEstado(10L, "PAID", token);
        verify(repo, times(1)).save(payment);
    }

    @Test
    void procesarPago_Error_EstadoOrdenNoValido() {
        
        String token = "Bearer m1t0k3n";
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(10L);

        OrderResponse order = new OrderResponse();
        order.setStatus("PAID"); 

        when(client.obtenerOrden(10L, token)).thenReturn(order);

        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            servi.procesarPago(request, token);
        });

        assertEquals("La orden tiene un estado no valido solo se acepta PENDIENTE (PENDING)", excepcion.getMessage());
        
    
        verify(repo, never()).save(any());
        verify(client, never()).actualizarEstado(anyLong(), anyString(), anyString());
    }

    @Test
    void procesarPago_Error_MontoNoCoincide() {
        
        String token = "Bearer m1t0k3n";
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(10L);
        request.setAmount(new BigDecimal("30000.00")); 

        OrderResponse order = new OrderResponse();
        order.setStatus("PENDING");
        order.setTotal(new BigDecimal("50000.00")); 

        when(client.obtenerOrden(10L, token)).thenReturn(order);

    
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            servi.procesarPago(request, token);
        });

        assertEquals("El monto a pagar no coincide con el total de la orden.", excepcion.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void eliminarPago_Error_PagoYaCompletado() {
        
        Long id = 1L;
        String token = "Bearer m1t0k3n";
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.SUCCESS); 

        when(repo.findById(id)).thenReturn(Optional.of(payment));

        
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            servi.eliminarPago(id, token);
        });

        assertEquals("No se puede eliminar un pago exitoso debe procesarse un reembolso.", excepcion.getMessage());
        verify(repo, never()).delete(any());
    }
    @Test
    void obtenerPagosExito() {
        when(repo.findAll()).thenReturn(java.util.List.of(new Payment()));
        assertNotNull(servi.obtenerPagos());
        verify(repo, times(1)).findAll();
    }

    @Test
    void obtenerPagoPorIdExito() {
        Long id = 1L;
        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();
        
        when(repo.findById(id)).thenReturn(Optional.of(payment));
        when(mapper.toResponse(payment)).thenReturn(response);
        
        assertNotNull(servi.obtenerPagoPorId(id));
        verify(repo, times(1)).findById(id);
    }

    @Test
    void actualizarPagoExito() {
        Long id = 1L;
        String token = "Bearer m1t0k3n";
        PaymentRequest request = new PaymentRequest();
        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();
        
        when(repo.findById(id)).thenReturn(Optional.of(payment));
        when(repo.save(any(Payment.class))).thenReturn(payment);
        when(mapper.toResponse(payment)).thenReturn(response);

        assertNotNull(servi.actualizarPago(id, request, token));
        verify(repo, times(1)).save(any(Payment.class));
    }
}
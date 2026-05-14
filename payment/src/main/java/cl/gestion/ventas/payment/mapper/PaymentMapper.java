package cl.gestion.ventas.payment.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import cl.gestion.ventas.payment.dto.PaymentRequest;
import cl.gestion.ventas.payment.dto.PaymentResponse;
import cl.gestion.ventas.payment.model.Payment;
import cl.gestion.ventas.payment.model.PaymentStatus;

@Component
public class PaymentMapper {


    public Payment fromRequest(PaymentRequest request){
        return Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .transactionDate(LocalDateTime.now())
                .build();
    }


    public PaymentResponse toResponse(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionDate(payment.getTransactionDate())
                .build();
    }
}

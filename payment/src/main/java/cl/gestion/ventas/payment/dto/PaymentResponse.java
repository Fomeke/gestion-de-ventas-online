package cl.gestion.ventas.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cl.gestion.ventas.payment.model.PaymentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus status;
    private LocalDateTime transactionDate;
    
}

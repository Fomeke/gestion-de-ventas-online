package cl.gestion.ventas.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cl.gestion.ventas.payment.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus status;
    private LocalDateTime transactionDate;
    
}

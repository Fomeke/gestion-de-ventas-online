package cl.gestion.ventas.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cl.gestion.ventas.payment.model.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    @Schema(description = "ID del pago", example = "1")
    private Long id;
    @Schema(description = "ID de la orden", example = "2")
    private Long orderId;
    @Schema(description = "Monto total del pago", example = "670.00")
    private BigDecimal amount;
    @Schema(description = "El metodo de pago", example = "Debito mastercard")
    private String paymentMethod;
    @Schema(description = "Estado en el que esta el pago", example = "PAID")
    private PaymentStatus status;
    @Schema(description = "Fecha en la se realizo el pago")
    private LocalDateTime transactionDate;
    
}

package cl.gestion.ventas.payment.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "El id de la orden es obligatorio.")
    @Schema(description = "ID de la orden", example = "2")
    private Long orderId;

    @NotNull(message = "El monto es obligatorio.")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    @Schema(description = "Monto total del pago", example = "670.00")
    private BigDecimal amount;

    @NotBlank(message = "Debe seleccionar un metodo de pago")
    @Schema(description = "El metodo de pago", example = "Debito mastercard")
    private String paymentMethod;
}

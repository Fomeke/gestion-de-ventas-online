package cl.gestion.ventas.payment.dto;

import java.math.BigDecimal;

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
    private Long orderId;

    @NotNull(message = "El monto es obligatorio.")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    @NotBlank(message = "Debe seleccionar un metodo de pago")
    private String paymentMethod;
}

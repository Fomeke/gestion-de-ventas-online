package cl.gestion.ventas.order.dto;

import cl.gestion.ventas.order.model.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message="La ID de usuario es obligatoria")
    @Schema(description="Identificador único de usuario", example = "2")
    private Long userId;

    @NotNull(message="El método de pago es obligatorio")
    @Schema(description="Método de pago", example = "CREDIT_CARD")
    private PaymentMethod paymentMethod;
}

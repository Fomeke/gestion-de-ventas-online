package cl.gestion.ventas.order.dto;

import cl.gestion.ventas.order.model.PaymentMethod;
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
    private Long userId;

    @NotNull(message="El método de pago es obligatorio")
    private PaymentMethod paymentMethod;
}

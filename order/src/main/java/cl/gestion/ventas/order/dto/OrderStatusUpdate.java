package cl.gestion.ventas.order.dto;

import cl.gestion.ventas.order.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdate {
    @NotNull(message="El nuevo estado es obligatorio")
    @Schema(description="Nuevo estado de la orden",example="PAID")
    private OrderStatus newStatus;
}

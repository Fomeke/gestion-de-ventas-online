package cl.gestion.ventas.order.dto;

import cl.gestion.ventas.order.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseForShipping {
    @Schema(description="ID de la órden",example="1")
    private Long id;

    @Schema(description="ID del usuario",example="1")
    private Long userId;

    @Schema(description="Estado de la orden",example="PENDING")
    private OrderStatus status;
}

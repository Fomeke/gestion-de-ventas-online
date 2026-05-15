package cl.gestion.ventas.order.dto;

import cl.gestion.ventas.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseForShipping {
    private Long id;
    private Long userId;
    private OrderStatus status;
}

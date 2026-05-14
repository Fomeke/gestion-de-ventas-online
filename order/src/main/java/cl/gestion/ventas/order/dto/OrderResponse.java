package cl.gestion.ventas.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import cl.gestion.ventas.order.model.OrderStatus;
import cl.gestion.ventas.order.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private BigDecimal total;
    private List<OrderItemDTO> items;
}

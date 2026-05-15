package cl.gestion.ventas.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cl.gestion.ventas.order.model.OrderStatus;
import cl.gestion.ventas.order.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderSmallResponse {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private BigDecimal total;
}

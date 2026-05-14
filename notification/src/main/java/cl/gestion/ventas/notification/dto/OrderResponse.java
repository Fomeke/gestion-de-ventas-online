package cl.gestion.ventas.notification.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private BigDecimal totalAmount;
    private String status; 
}

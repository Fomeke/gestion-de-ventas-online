package cl.gestion.ventas.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cl.gestion.ventas.order.model.OrderStatus;
import cl.gestion.ventas.order.model.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSmallResponse {
    @Schema(description="ID de la órden",example="1")
    private Long id;

    @Schema(description="ID del usuario",example="1")
    private Long userId;

    @Schema(description="Fecha y hora de emisión de la orden",example="2026-06-20T20:20:00")
    private LocalDateTime orderDate;

    @Schema(description="Estado de la orden",example="PENDING")
    private OrderStatus status;

    @Schema(description="Método de pago", example = "CREDIT_CARD")
    private PaymentMethod paymentMethod;

    @Schema(description="Monto total de la compra",example="10000")
    private BigDecimal total;
}

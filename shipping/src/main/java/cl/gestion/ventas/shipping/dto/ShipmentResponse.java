package cl.gestion.ventas.shipping.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponse {
    private Long id;
    private Long orderId;
    private String trackingNumber;
    private String carrier;
    private String shippingAddress;
    private LocalDate estimatedDeliveryDate;

}

package cl.gestion.ventas.shipping.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponse {
    @Schema(description = "Identificador único interno del envío", example = "2")
    private Long id;

    @Schema(description = "ID de la orden de compra", example = "1")
    private Long orderId;

    @Schema(description = "N° único de rastreo", example = "NUT-67")
    private String trackingNumber;
    
    @Schema(description = "Empresa de transporte que hará el envío", example = "FedEx")
    private String carrier;

    @Schema(description = "Dirección de destino", example = "Calle falsa 123, Springfield")
    private String shippingAddress;

    @Schema(description= "Tiempo estimado de entrega, suele ser 30 días después de la fecha de emisión", example= "2026-07-30")
    private LocalDate estimatedDeliveryDate;

}

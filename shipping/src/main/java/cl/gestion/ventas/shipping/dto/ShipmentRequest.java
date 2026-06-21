package cl.gestion.ventas.shipping.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentRequest {
    @NotNull(message="La ID de compra es obligatoria")
    @Schema(description = "ID de la orden de compra", example = "1")
    private Long orderId;

    @NotBlank(message="El n° de rastreo es obligatorio")
    @Size(min=5,max=200,message="El n° de rastreo debe tener entre 5 y 200 carácteres")
    @Schema(description = "N° único de rastreo", example = "NUT-67")
    private String trackingNumber;

    @NotBlank(message="La empresa de transporte es obligatoria")
    @Size(min=5,max=100,message="La empresa de transporte debe tener entre 5 y 100 carácteres")
    @Schema(description = "Empresa de transporte que hará el envío", example = "FedEx")
    private String carrier;

    @NotBlank(message="La dirección de destino es obligatoria")
    @Size(min=5,max=300,message="La dirección debe tener entre 5 y 300 carácteres")
    @Schema(description = "Dirección de destino", example = "Calle falsa 123, Springfield")
    private String shippingAddress;

}

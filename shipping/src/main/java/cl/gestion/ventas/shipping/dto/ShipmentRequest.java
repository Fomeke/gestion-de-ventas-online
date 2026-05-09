package cl.gestion.ventas.shipping.dto;

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
    private Long orderId;

    @NotBlank(message="El n° de rastreo es obligatorio")
    @Size(min=5,max=200,message="El n° de rastreo debe tener entre 5 y 200 carácteres")
    private String trackingNumber;

    @NotBlank(message="La empresa de transporte es obligatoria")
    @Size(min=5,max=100,message="La empresa de transporte debe tener entre 5 y 100 carácteres")
    private String carrier;

    @NotBlank(message="La dirección de destino es obligatoria")
    @Size(min=5,max=300,message="La dirección debe tener entre 5 y 300 carácteres")
    private String shippingAddress;

}

package cl.gestion.ventas.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    @NotNull(message="La ID de producto es obligatoria")
    @Schema(description="Identificador único de producto", example = "3")
    private Long productId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value=1,message="La cantidad mínima es 1 producto")
    @Schema(description="Cantidad de productos", example = "3")
    private Integer quantity;

}

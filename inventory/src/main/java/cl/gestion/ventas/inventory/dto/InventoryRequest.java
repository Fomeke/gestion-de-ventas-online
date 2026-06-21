package cl.gestion.ventas.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    @NotNull(message="La ID de producto es obligatoria")
    @Schema(description="ID del producto, es obligatorio", example = "1")
    private Long productId;

    @NotNull(message="El stock es obligatorio")
    @Max(value=9999,message="El stock excede la cantidad máxima")
    @Schema(description="Cantidad de unidades del producto", example = "2")
    private Integer stock;
}

package cl.gestion.ventas.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {
    @Schema(description="ID del inventario mismo", example = "1")
    private Long id;

    @Schema(description="ID del producto", example = "1")
    private Long productId;

    @Schema(description="Cantidad de unidades del producto", example = "2")
    private Integer stock;
}

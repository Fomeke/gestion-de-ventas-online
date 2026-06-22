package cl.gestion.ventas.product.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {

    @Schema(description = "ID del prducto", example = "1")
    private Long id;
    @Schema(description = "Nombre del producto", example = "Notebook")
    private String name;
    @Schema(description = "Precio del producto")
    private BigDecimal price;
    @Schema(description = "Descripcion del producto", example = "Notebook Asus")
    private String description;
    @Schema(description = "ID de la categoria", example = "1")
    private Long categoryId;
    
}

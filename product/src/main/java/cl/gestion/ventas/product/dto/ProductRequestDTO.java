package cl.gestion.ventas.product.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Debe ingresar un nombre.")
    @Size(min = 2, max = 50)
    @Schema(description = "Nombre del producto", example = "Notebook")
    private String name;
    
    @NotNull(message = "Debe ingresar un precio.")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Schema(description = "Precio del producto")
    private BigDecimal price;

    @Size(max = 150)
    @Schema(description = "Descripcion del producto", example = "Notebook Asus")
    private String description; 

    @NotNull(message = "Debe ingresar una categoria.")
    @Schema(description = "ID de la categoria", example = "1")
    private Long categoryId;
}

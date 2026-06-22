package cl.gestion.ventas.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDTO {
    @Schema(description = "ID unico de la categoria", example = "1")
    private Long id; 

    @Schema(description = "Nombre de la categoria", example = "Electronica")
    private String name;
}

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
<<<<<<< HEAD

    @Schema(description="Identificador único de la categoria",example="1")
    private Long id;

    @Schema(description="Nombre de la categoria",example="Accesorios")
=======
    @Schema(description = "ID unico de la categoria", example = "1")
    private Long id; 

    @Schema(description = "Nombre de la categoria", example = "Electronica")
>>>>>>> 628ed770da95f14a56697383f83425bf1d73285d
    private String name;
}

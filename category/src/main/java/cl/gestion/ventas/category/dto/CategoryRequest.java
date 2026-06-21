package cl.gestion.ventas.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "La categoria no debe quedar sin nombre.")
    @Size(min = 2,max = 50, message = "El nombre debe ser en 2 y 50 caracteres.")
    @Schema(description = "Nombre de la categoria", example = "Electronica")
    private String name;
}

package cl.gestion.ventas.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequestDTO {

    @Size(max = 200, message = "La reseña no debe tener mas de 200 caracteres.")
    private String comment;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es de 1")
    @Max(value = 5, message = "La calificacion maxima es de 5")
    private Integer rating;

    @NotNull(message = "El id del producto es obligatorio")
    private Long productId;
}

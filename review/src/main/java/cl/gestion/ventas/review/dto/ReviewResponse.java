package cl.gestion.ventas.review.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    @Schema(description = "ID de la reseña", example = "1")
    private Long id;
    @Schema(description = "ID del producto", example = "1")
    private Long idProduct;
    @Schema(description = "Comentario a hacer del producto", example = "Buen producto!")
    private String comment;
    @Schema(description = "Rating del producto de 1 a 5", example = "5")
    private Integer Rating;
    @Schema(description = "Fecha de la reseña")
    private LocalDateTime createdAt;
}

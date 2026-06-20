package cl.gestion.ventas.review.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private Long id;
    private Long idProduct;
    private String comment;
    private Integer Rating;
    private LocalDateTime createdAt;
}

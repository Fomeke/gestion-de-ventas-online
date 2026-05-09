package cl.gestion.ventas.review.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {

    private Long id;
    private Long idProduct;
    private String userName;
    private String comment;
    private Integer Rating;
    private LocalDateTime createdAt;
}

package cl.gestion.ventas.review.mapper;

import org.springframework.stereotype.Component;

import cl.gestion.ventas.review.dto.ReviewRequestDTO;
import cl.gestion.ventas.review.dto.ReviewResponse;
import cl.gestion.ventas.review.model.Review;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequestDTO request, Long idUser, String userName) {
        return Review.builder()
                .comment(request.getComment())
                .rating(request.getRating())
                .productId(request.getProductId())
                .userId(idUser)
                .userName(userName)
                .build();
    }

    public ReviewResponse toDTO(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .idProduct(review.getProductId())
                .comment(review.getComment())
                .Rating(review.getRating())
                .userName(review.getUserName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}

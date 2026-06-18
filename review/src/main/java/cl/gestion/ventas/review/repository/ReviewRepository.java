package cl.gestion.ventas.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import cl.gestion.ventas.review.model.Review;
import java.util.List;



public interface ReviewRepository extends JpaRepository<Review,Long>{

    List<Review> findByProductId(Long productId);

}

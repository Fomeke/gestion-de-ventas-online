package cl.gestion.ventas.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.gestion.ventas.review.model.Review;
import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>{

    List<Review> findByProductId(Long productId);

}

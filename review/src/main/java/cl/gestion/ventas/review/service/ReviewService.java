package cl.gestion.ventas.review.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gestion.ventas.review.client.ProductClient;
import cl.gestion.ventas.review.dto.ReviewRequestDTO;
import cl.gestion.ventas.review.dto.ReviewResponse;
import cl.gestion.ventas.review.mapper.ReviewMapper;
import cl.gestion.ventas.review.model.Review;
import cl.gestion.ventas.review.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    private ReviewRepository repo;

    @Autowired
    private ReviewMapper mapper;

    @Autowired
    private ProductClient client;

    public List<Review> listReviews() {
        log.info("Buscando todos los reviews..");
        return repo.findAll();
    }

    public ReviewResponse reviewPorid(Long id) {
        log.info("Buscando review con la id: {}", id);
        Review review = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro la review con esa id."));
        return mapper.toDTO(review);
    }

    public List<Review> obtenerReviewPorProducto(Long productId) {
        log.info("Buscando reseñas del producto con id: {}", productId);
        return repo.findByProductId(productId);
    }

    public ReviewResponse crearReview(ReviewRequestDTO request, String token) {
        log.info("Creando id para el producto id: {}", request.getProductId());

        client.obtenerProductoPorId(request.getProductId(), token);

        Review review = repo.save(mapper.toEntity(request));
        return mapper.toDTO(review);
    }

    public ReviewResponse modificarReview(Long id, ReviewRequestDTO request, String token) {
        log.info("Modificando review del product: {}", id);

        Review review = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review no encontrada."));
        
        if(request.getComment() != null){
            review.setComment(request.getComment());
        }
        if(request.getRating() != null){
            review.setRating(request.getRating());
        }
        if(request.getProductId() != null && !request.getProductId().equals(review.getProductId())){
            client.obtenerProductoPorId(request.getProductId(), token);
            review.setProductId(request.getProductId());
        }
        Review reviewUpdate = repo.save(review);
        return mapper.toDTO(reviewUpdate);

    }

    public void eliminarReview(Long id){
        log.info("Eliminando producto con la id: {}", id);
        if(!repo.existsById(id)){
            throw new NoSuchElementException("No existe esa review con tal id.");
        }
        repo.deleteById(id);
    }
}

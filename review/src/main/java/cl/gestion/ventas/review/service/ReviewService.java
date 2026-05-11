package cl.gestion.ventas.review.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public List<Review> listReviews(){
        log.info("Buscando todos los reviews..");
        return repo.findAll();
    }

    public ReviewResponse reviewPorid(Long id){
        log.info("Buscando review con la id: {}", id);
        Review review = repo.findById(id).orElseThrow(() -> new NoSuchElementException("No se encontro la review con esa id."));
        return mapper.toDTO(review);
    }

    
}

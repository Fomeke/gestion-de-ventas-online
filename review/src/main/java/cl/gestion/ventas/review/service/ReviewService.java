package cl.gestion.ventas.review.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

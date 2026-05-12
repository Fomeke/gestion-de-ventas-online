package cl.gestion.ventas.review.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.review.dto.ReviewRequestDTO;
import cl.gestion.ventas.review.dto.ReviewResponse;
import cl.gestion.ventas.review.model.Review;
import cl.gestion.ventas.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v1/review")
public class ReviewController {

    @Autowired
    private ReviewService service;



    @GetMapping
    public ResponseEntity<List<Review>> getReviews(){
        log.info("GET api/v1/review/allreviews");

        return ResponseEntity.ok().body(service.listReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewPorId(@PathVariable Long id){
        log.info("GET api/v1/review/reviewPorId");
        return ResponseEntity.ok().body(service.reviewPorid(id));
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> postReview(
        @Valid @RequestBody ReviewRequestDTO request,
        @RequestHeader("Authorization") String auth){
        
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crearReview(request, auth));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> putReview(
        @PathVariable Long id,
        @Valid @RequestBody ReviewRequestDTO request,
        @RequestHeader("Authorization") String auth){
        
            return ResponseEntity.status(HttpStatus.CREATED).body(service.modificarReview(id, request, auth));
    }
}

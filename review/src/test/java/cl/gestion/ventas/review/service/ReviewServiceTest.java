package cl.gestion.ventas.review.service;

import cl.gestion.ventas.review.client.ProductClient;
import cl.gestion.ventas.review.dto.ReviewRequestDTO;
import cl.gestion.ventas.review.dto.ReviewResponse;
import cl.gestion.ventas.review.mapper.ReviewMapper;
import cl.gestion.ventas.review.model.Review;
import cl.gestion.ventas.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository repo;

    @Mock
    private ReviewMapper mapper;

    @Mock
    private ProductClient client; 

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void reviewPorid_Exito() {
        
        Long id = 1L;
        Review review = new Review();
        review.setId(id);
        
        ReviewResponse response = new ReviewResponse();
        response.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(review));
        when(mapper.toDTO(review)).thenReturn(response);

    
        ReviewResponse resultado = reviewService.reviewPorid(id);

        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repo, times(1)).findById(id);
    }

    @Test
    void crearReview_Exito() {
    
        String token = "Bearer token123";
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setProductId(10L);
        request.setComment("Excelente producto");

        Review review = new Review();
        ReviewResponse response = new ReviewResponse();
        response.setComment("Excelente producto");

        
        when(client.obtenerProductoPorId(10L, token)).thenReturn(null);
        when(mapper.toEntity(request)).thenReturn(review);
        when(repo.save(review)).thenReturn(review);
        when(mapper.toDTO(review)).thenReturn(response);

        
        ReviewResponse resultado = reviewService.crearReview(request, token);

        
        assertEquals("Excelente producto", resultado.getComment());
        verify(client, times(1)).obtenerProductoPorId(10L, token); 
        verify(repo, times(1)).save(review);
    }

    @Test
    void modificarReview_Exito_ConCambioDeProducto() {
        
        Long id = 1L;
        String token = "Bearer token123";
        
        
        Review reviewExistente = new Review();
        reviewExistente.setId(id);
        reviewExistente.setProductId(10L); 
        reviewExistente.setRating(3);
        
        
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setProductId(20L); 
        request.setRating(5);

        ReviewResponse response = new ReviewResponse();
        response.setRating(5);

        when(repo.findById(id)).thenReturn(Optional.of(reviewExistente));
        when(client.obtenerProductoPorId(20L, token)).thenReturn(null); // Debe validar el NUEVO producto
        when(repo.save(reviewExistente)).thenReturn(reviewExistente);
        when(mapper.toDTO(reviewExistente)).thenReturn(response);

        
        ReviewResponse resultado = reviewService.modificarReview(id, request, token);

        
        assertEquals(5, resultado.getRating());
        
        
        verify(client, times(1)).obtenerProductoPorId(20L, token); 
        verify(repo, times(1)).save(reviewExistente);
    }

    @Test
    void eliminarReview_Error_NoEncontrada() {
        
        Long id = 99L;
        when(repo.existsById(id)).thenReturn(false);

        
        NoSuchElementException excepcion = assertThrows(NoSuchElementException.class, () -> {
            reviewService.eliminarReview(id);
        });

        assertEquals("No existe esa review con tal id.", excepcion.getMessage());
        verify(repo, never()).deleteById(anyLong()); // Validamos que la BD no se toca si falla
    }

    @Test
    void listReviewsExito() {
        when(repo.findAll()).thenReturn(java.util.List.of(new Review()));
        assertNotNull(reviewService.listReviews());
        verify(repo, times(1)).findAll();
    }
}

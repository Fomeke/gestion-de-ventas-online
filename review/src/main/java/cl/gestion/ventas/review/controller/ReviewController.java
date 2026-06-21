package cl.gestion.ventas.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.gestion.ventas.review.dto.ReviewRequestDTO;
import cl.gestion.ventas.review.dto.ReviewResponse;
import cl.gestion.ventas.review.model.Review;
import cl.gestion.ventas.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApiResponses({
        @ApiResponse(responseCode = "401", description = "No autenticado (Falta token o es invalido)"),
        @ApiResponse(responseCode = "403", description = "No autorizado (No tienes permisos)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
@RestController
@Slf4j
@RequestMapping("/v1/review")
@RequiredArgsConstructor
@Tag(name = "Review API", description = "Endpoints para la gestion de reseñas y calificaciones de productos")
public class ReviewController {

    private final ReviewService service;

    @Operation(summary = "Obtener todas las reseñas", description = "Retorna una lista con todas las reseñas registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenidas correctamente")
    @GetMapping
    public ResponseEntity<List<Review>> getReviews(){
        log.info("GET api/v1/review/allreviews");
        return ResponseEntity.ok().body(service.listReviews());
    }

    @Operation(summary = "Buscar reseña por ID", description = "Busca y retorna los detalles de una reseña especifica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewPorId(@PathVariable Long id){
        log.info("GET api/v1/review/reviewPorId");
        return ResponseEntity.ok().body(service.reviewPorid(id));
    }

    @Operation(summary = "Crear nueva reseña", description = "Registra una nueva reseña validando internamente con el microservicio product que el producto exista.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o producto no existente")
    })
    @PostMapping
    public ResponseEntity<ReviewResponse> postReview(
        @Valid @RequestBody ReviewRequestDTO request,
        @RequestHeader("Authorization") String auth){
            log.info("POST api/v1/review/crearReview");
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crearReview(request, auth));
    }

    @Operation(summary = "Modificar reseña", description = "Actualiza el comentario o calificación de una reseña si se cambia de producto valida la existencia del nuevo producto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> putReview(
        @PathVariable Long id,
        @Valid @RequestBody ReviewRequestDTO request,
        @RequestHeader("Authorization") String auth){
            log.info("PUT api/v1/review/modificarReview");
            return ResponseEntity.status(HttpStatus.CREATED).body(service.modificarReview(id, request, auth));
    }

    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña de forma permanente mediante su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reseña eliminada con éxito (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        log.info("DELETE api/v1/review/deleteReview");
        service.eliminarReview(id);
        return ResponseEntity.noContent().build();
    }
}
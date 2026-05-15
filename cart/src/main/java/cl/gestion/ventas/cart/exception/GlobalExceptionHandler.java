package cl.gestion.ventas.cart.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.gestion.ventas.cart.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase maneja las excepciones de manera centralizada en la aplicación.
 * La anotación @RestControllerAdvice indica que es un controlador global de la
 * aplicación, en este caso sólo para las excepciones, mientras que @Slf4j 
 * habilita las funciones para insertar logs al momento de ocurrir una excepcion.
 * Se manejan las siguientes excepciones, que retornan un ResponseEntity con
 * el cuerpo en formato (dto) ApiErrorResponse:
 * 
 * DataIntegrityViolationException: cuando se viola la integridad de la base de datos,
 *      por ejemplo si se rompe una unique/not null/primary key constraint.
 * 
 * NoSuchElementException: Si no se encuentra el registro en la tabla.
 * 
 * MethodArgumentNotValidException: Devuelve uno o más errores de validacion,
 *      aplicados en el DTO como @NotBlank o @NotNull.
 * 
 * AccessDeniedException: Ocurre si el usuario no tiene permisos para realizar una acción.
 * 
 * RuntimeException: Excepcion "paraguas" que maneja el resto de excepciones
 *      no especificadas anteriormente.
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        log.error("Violación de integridad de datos: {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.name())
                .message("Data integrity violation")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchElementException(NoSuchElementException ex,
            HttpServletRequest request) {
        log.error("Carrito no encontrado: {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        log.error("Solicitud inválida: {}", ex.getMessage());
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .message("Invalid request")
                .path(request.getRequestURI())
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex,
            HttpServletRequest request) {
        log.error("Error inesperado: {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message("Unexpected error")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchElementException(BadCredentialsException ex,
            HttpServletRequest request) {
        log.error("Usuario con credenciales inválidas");
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
            HttpServletRequest request) {
        log.error("Usuario sin permisos para realizar acción");
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

        @ExceptionHandler(feign.FeignException.class)
    public ResponseEntity<ApiErrorResponse> handleFeignException(feign.FeignException ex,
            HttpServletRequest request) {
        log.error("Error desde servicio externo: {}",ex.getMessage());

        String message = "Error en microservicio externo";

        String responseBody = ex.contentUTF8();

        if(responseBody != null && !responseBody.isEmpty()){
                if(responseBody.contains("\"message\":\"")){
                        message = responseBody.split("\"message\":\"")[1].split("\"")[0];
                }else if (responseBody.contains("\"errors\":[")){
                        message = "Error de validación: "+ responseBody.split("\"errors\":\\[")[1].split("\\]")[0];
                }
        }else{
                message = ex.getMessage();
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.status() > 0 ? ex.status() : 500)
                .error("EXTERNAL_SERVICE_ERROR")
                .message(message)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(ex.status() > 0 ? ex.status() : 500).body(errorResponse);
    } 
    
    
}

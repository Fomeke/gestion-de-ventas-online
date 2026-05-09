package cl.gestion.ventas.shipping.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Clase que sirve como formato para devolver respuestas de errores en la
 * API de Auth, contiene atributos de la fecha y hora del error, estado,
 * mensaje, URI, y la(s) descripciones de error correspondientes. Se utiliza 
 * como formato general para devolver excepciones en la clase
 * GlobalExceptionHandler.
 */

@Data
@Builder
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;
}
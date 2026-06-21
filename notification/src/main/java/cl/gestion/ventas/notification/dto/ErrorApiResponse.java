package cl.gestion.ventas.notification.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorApiResponse {
    @Schema(description="Fecha y hora del error", example = "2026-06-20T19:15:30")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "404")
    private Integer status;

    @Schema(description = "Nombre del error HTTP", example = "NOT_FOUND")
    private String error;

    @Schema(description = "Mensaje explicativo del fallo", example = "Recurso no encontrado")
    private String message;

    @Schema(description = "Ruta URL que originó el error", example = "/api/v1/servicio/1")
    private String path;

    @Schema(
        description = "Lista con los mensajes de error detallados",
        examples = {"campo1: no puede ser nulo", "campo2: no puede estar vacío"})
    private List<String> errors;
}

package cl.gestion.ventas.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.notification.dto.NotificationRequest;
import cl.gestion.ventas.notification.dto.NotificationResponse;
import cl.gestion.ventas.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "No autorizado - Token JWT ausente, expirado o inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
@Slf4j
@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "Endpoints para la gestion y registro de notificaciones")
public class NotificationController {

    private final NotificationService service;

    @Operation(summary = "Obtener todas las notificaciones", description = "Retorna el historial completo de notificaciones generadas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {
        log.info("GET api/v1/notification");
        return ResponseEntity.ok(service.listNotificacion());
    }

    @Operation(summary = "Crear nueva notificación", description = "Genera una notificacion validando internamente que la orden y el usuario asociados existan mediante webclient.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error de comunicación con otros servicios")
    })
    @PostMapping
    public ResponseEntity<NotificationResponse> postNotification(@Valid @RequestBody NotificationRequest request,
            @RequestHeader("Authorization") String token) {
        log.info("POST api/v1/notification");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearNotificacion(request, token));
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificacion específica del sistema utilizando su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notificacion eliminada con exito (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Notificacion no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.info("DELETE api/v1/notification/{}", id);
        service.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar notificación", description = "Modifica los detalles de una notificacion ya existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificacion actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "404", description = "Notificacion no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> putNotification(
            @PathVariable Long id,
            @Valid @RequestBody NotificationRequest request,
            @RequestHeader("Authorization") String token) {

        log.info("PUT api/v1/notification/{}", id);
        return ResponseEntity.ok(service.actualizarNotificacion(id, request, token));
    }
}

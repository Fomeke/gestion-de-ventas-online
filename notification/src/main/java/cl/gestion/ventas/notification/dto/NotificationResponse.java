package cl.gestion.ventas.notification.dto;

import java.time.LocalDateTime;

import cl.gestion.ventas.notification.models.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    @Schema(description = "ID de la notificacion", example = "2")
    private Long id;
    @Schema(description = "ID de la orden", example = "1")
    private Long orderId;
    @Schema(description = "ID del usuario", example = "3")
    private Long userId;
    @Schema(description = "Asunto de la notificacion", example = "Pago realizado")
    private String subject;
    @Schema(description = "Mensaje a enviar", example = "Su pago a sido realizado con exito")
    private String message;
    @Schema(description = "Tipo de notificacion a enviar", example = "Email")
    private NotificationType type;
    @Schema(description = "Fecha de envio de la notificacion")
    private LocalDateTime createdAt;
}

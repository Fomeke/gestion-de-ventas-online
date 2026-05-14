package cl.gestion.ventas.notification.dto;

import java.time.LocalDateTime;

import cl.gestion.ventas.notification.models.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {

    private Long id;
    private Long orderId;
    private Long userId;
    private String subject;
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;
}

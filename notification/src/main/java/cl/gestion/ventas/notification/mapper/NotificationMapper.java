package cl.gestion.ventas.notification.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import cl.gestion.ventas.notification.dto.NotificationRequest;
import cl.gestion.ventas.notification.dto.NotificationResponse;
import cl.gestion.ventas.notification.models.Notification;
import cl.gestion.ventas.notification.models.NotificationType;

@Component
public class NotificationMapper {


    public Notification fromRequest(NotificationRequest request){
        return Notification.builder()
                .userId(request.getUserId())
                .orderId(request.getOrderId())
                .subject(request.getSubject())
                .message(request.getMessage())
                .type(request.getType() != null ? NotificationType.valueOf(request.getType()) : NotificationType.EMAIL)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public NotificationResponse toResponse(Notification notification){
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .orderId(notification.getOrderId())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}

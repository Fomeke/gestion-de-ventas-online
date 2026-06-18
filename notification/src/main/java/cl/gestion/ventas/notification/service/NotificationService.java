package cl.gestion.ventas.notification.service;

import java.util.List;
import java.util.NoSuchElementException;


import org.springframework.stereotype.Service;

import cl.gestion.ventas.notification.client.OrderClient;
import cl.gestion.ventas.notification.client.UserClient;
import cl.gestion.ventas.notification.dto.NotificationRequest;
import cl.gestion.ventas.notification.dto.NotificationResponse;
import cl.gestion.ventas.notification.dto.UserResponse;
import cl.gestion.ventas.notification.mapper.NotificationMapper;
import cl.gestion.ventas.notification.models.Notification;
import cl.gestion.ventas.notification.models.NotificationType;
import cl.gestion.ventas.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;

    private final UserClient clientUser;

    private final OrderClient clientOrder;

    private final NotificationMapper mapper;

    public List<NotificationResponse> listNotificacion() {
        log.info("Obteniendo lista de notificaciones..");
        List<Notification> noti = repo.findAll();
        return noti.stream().map(mapper::toResponse).toList();
    }

    public NotificationResponse crearNotificacion(NotificationRequest request, String token) {

        clientOrder.obtenerOrden(request.getOrderId(), token);
        log.info("Orden {} validada correctamente", request.getOrderId());

        UserResponse user = clientUser.obtenerUsuarioPorId(request.getUserId(), token);

        log.info("Enviando correo a: {} para la orden #{}", user.getEmail(), request.getOrderId());

        Notification noti = mapper.fromRequest(request);

        return mapper.toResponse(repo.save(noti));
    }

    public NotificationResponse actualizarNotificacion(Long id, NotificationRequest request, String token) {
        Notification existe = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notificación no encontrada"));

        clientOrder.obtenerOrden(request.getOrderId(), token);
        clientUser.obtenerUsuarioPorId(request.getUserId(), token);

        Notification updated = Notification.builder()
                .id(existe.getId())
                .userId(request.getUserId())
                .orderId(request.getOrderId())
                .subject(request.getSubject())
                .message(request.getMessage())
                .type(request.getType() != null ? NotificationType.valueOf(request.getType()) : NotificationType.EMAIL)
                .createdAt(existe.getCreatedAt())
                .build();

        return mapper.toResponse(repo.save(updated));
    }

    public void eliminarNotificacion(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Notificación no encontrada");
        }
        repo.deleteById(id);
    }
}

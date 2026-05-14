package cl.gestion.ventas.notification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gestion.ventas.notification.client.OrderClient;
import cl.gestion.ventas.notification.client.UserClient;
import cl.gestion.ventas.notification.dto.NotificationRequest;
import cl.gestion.ventas.notification.dto.NotificationResponse;
import cl.gestion.ventas.notification.dto.UserResponse;
import cl.gestion.ventas.notification.mapper.NotificationMapper;
import cl.gestion.ventas.notification.models.Notification;
import cl.gestion.ventas.notification.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    @Autowired
    private UserClient clientUser;

    @Autowired
    private OrderClient clientOrder;

    @Autowired
    private NotificationMapper mapper;

    public List<NotificationResponse> listNotificacion(){
        log.info("Obteniendo lista de notificaciones..");
        List<Notification> noti = repo.findAll();
        return noti.stream().map(mapper::toResponse).toList();
    }

    public NotificationResponse crearNotificacion(NotificationRequest request, String token){
        
        clientOrder.obtenerOrden(request.getOrderId(), token);
        log.info("Orden {} validada correctamente", request.getOrderId());

        UserResponse user = clientUser.obtenerUsuarioPorId(request.getUserId(), token);

        log.info("Enviando correo a: {} para la orden #{}", user.getEmail(), request.getOrderId());

        Notification noti = mapper.fromRequest(request);

        return mapper.toResponse(repo.save(noti));
    }
}

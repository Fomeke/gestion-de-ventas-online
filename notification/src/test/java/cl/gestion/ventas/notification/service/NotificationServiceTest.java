package cl.gestion.ventas.notification.service;

import cl.gestion.ventas.notification.client.OrderClient;
import cl.gestion.ventas.notification.client.UserClient;
import cl.gestion.ventas.notification.dto.NotificationRequest;
import cl.gestion.ventas.notification.dto.NotificationResponse;
import cl.gestion.ventas.notification.dto.UserResponse;
import cl.gestion.ventas.notification.mapper.NotificationMapper;
import cl.gestion.ventas.notification.models.Notification;
import cl.gestion.ventas.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repo;

    @Mock
    private UserClient clientUser; 

    @Mock
    private OrderClient clientOrder; 

    @Mock
    private NotificationMapper mapper;

    @InjectMocks
    private NotificationService servi;

    @Test
    void crearNotificacion_Exito() {
        
        String token = "Bearer token123";
        NotificationRequest request = new NotificationRequest();
        request.setOrderId(10L);
        request.setUserId(5L);
        request.setSubject("Compra exitosa");

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("cliente@correo.com");

        Notification noti = new Notification();
        NotificationResponse response = new NotificationResponse();
        response.setSubject("Compra exitosa");

        
        when(clientOrder.obtenerOrden(10L, token)).thenReturn(null); 
        when(clientUser.obtenerUsuarioPorId(5L, token)).thenReturn(userResponse); 
        
        when(mapper.fromRequest(request)).thenReturn(noti);
        when(repo.save(noti)).thenReturn(noti);
        when(mapper.toResponse(noti)).thenReturn(response);

        
        NotificationResponse resultado = servi.crearNotificacion(request, token);

        
        assertNotNull(resultado);
        assertEquals("Compra exitosa", resultado.getSubject());
        
        
        verify(clientOrder, times(1)).obtenerOrden(10L, token);
        verify(clientUser, times(1)).obtenerUsuarioPorId(5L, token);
        verify(repo, times(1)).save(noti);
    }

    @Test
    void actualizarNotificacion_Exito() {
        
        Long id = 1L;
        String token = "Bearer token123";
        
        NotificationRequest request = new NotificationRequest();
        request.setOrderId(15L);
        request.setUserId(2L);
        request.setSubject("Actualización de envío");
        request.setType("EMAIL");

        Notification notiExistente = new Notification();
        notiExistente.setId(id);

        NotificationResponse response = new NotificationResponse();
        response.setSubject("Actualización de envío");

        when(repo.findById(id)).thenReturn(Optional.of(notiExistente));
        when(clientOrder.obtenerOrden(15L, token)).thenReturn(null);
        when(clientUser.obtenerUsuarioPorId(2L, token)).thenReturn(null);
        
        
        when(repo.save(any(Notification.class))).thenReturn(notiExistente);
        when(mapper.toResponse(notiExistente)).thenReturn(response);

        
        NotificationResponse resultado = servi.actualizarNotificacion(id, request, token);

        
        assertEquals("Actualización de envío", resultado.getSubject());
        verify(clientOrder, times(1)).obtenerOrden(15L, token);
        verify(clientUser, times(1)).obtenerUsuarioPorId(2L, token);
        verify(repo, times(1)).save(any(Notification.class));
    }

    @Test
    void actualizarNotificacion_Error_NoEncontrada() {
        
        Long id = 99L;
        String token = "Bearer token123";
        NotificationRequest request = new NotificationRequest();

        when(repo.findById(id)).thenReturn(Optional.empty());

        
        NoSuchElementException excepcion = assertThrows(NoSuchElementException.class, () -> {
            servi.actualizarNotificacion(id, request, token);
        });

        assertEquals("Notificación no encontrada", excepcion.getMessage());
        
        
        verify(clientOrder, never()).obtenerOrden(anyLong(), anyString());
        verify(clientUser, never()).obtenerUsuarioPorId(anyLong(), anyString());
        verify(repo, never()).save(any());
    }

    @Test
    void eliminarNotificacion_Exito() {
        
        Long id = 1L;
        when(repo.existsById(id)).thenReturn(true);

        
        servi.eliminarNotificacion(id);

        
        verify(repo, times(1)).deleteById(id);
    }
}

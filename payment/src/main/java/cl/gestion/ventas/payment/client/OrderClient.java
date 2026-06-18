package cl.gestion.ventas.payment.client;

import java.util.NoSuchElementException;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.gestion.ventas.payment.dto.OrderResponse;
import cl.gestion.ventas.payment.dto.OrderStatusUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderClient {


    private final WebClient webClient;

    public OrderResponse obtenerOrden(Long orderId,String token){
        try{
            return webClient.get()
                    .uri("/api/v1/orders/noitems/{id}", orderId)
                    .header("Authorization",token)
                    .retrieve()
                    .bodyToMono(OrderResponse.class)
                    .block();
        }catch(WebClientResponseException ex){
            log.error("Error al comunicarse con Order: {}", ex.getMessage());
            if(ex.getStatusCode().value() == 404){
                throw new NoSuchElementException("La orden con ID " + orderId + "no existe.");
            }
            throw new RuntimeException("Error interno al validar la orden.");
        }
    }

    public void actualizarEstado(Long orderId, String nuevoEstado, String token){
        OrderStatusUpdate requestBody = OrderStatusUpdate.builder()
                        .newStatus(nuevoEstado)
                        .build();
        try{
            webClient.put()
                    .uri("/api/v1/orders/{orderId}", orderId)
                    .header("Authorization", token)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("Estado de la orden {} actualizado a {}",orderId,nuevoEstado);

        }catch(WebClientResponseException ex){
            
            log.error("Error al actualizar estado de Orden: ",ex.getMessage());
            if(ex.getStatusCode().value() == 404){
                throw new NoSuchElementException("La orden con ID " + orderId + "no existe.");
            }
            throw new RuntimeException("Error interno al validar la orden.");
        }
    }
}

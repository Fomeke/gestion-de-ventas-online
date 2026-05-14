package cl.gestion.ventas.notification.client;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.gestion.ventas.notification.dto.OrderResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderClient {
    @Autowired
    private WebClient webClient;

    public OrderResponse obtenerOrden(Long orderId,String token){
        log.info("Validando existencia de la orden ID: {}", orderId);
        try{
            return webClient.get()
                    .uri("/{id}", orderId)
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
}

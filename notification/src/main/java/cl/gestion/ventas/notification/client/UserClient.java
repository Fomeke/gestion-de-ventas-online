package cl.gestion.ventas.notification.client;

import java.util.NoSuchElementException;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.gestion.ventas.notification.dto.UserResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserClient {
    private final WebClient webClient;

    public UserClient(@Qualifier("webClientAuth") WebClient webClient) {
        this.webClient = webClient;
    }

    public UserResponse obtenerUsuarioPorId(Long userId, String token){
        try{
            return webClient.get()
                    .uri("/api/v1/usuarios/{id}",userId)
                    .header("Authorization",token)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        }catch(WebClientResponseException ex){
            log.error("Error al comunicarse con User: {}", ex.getMessage());

            if(ex.getStatusCode().value() == 404){
                throw new NoSuchElementException("El usuario con la ID " + userId + " no existe");
            }
            throw new RuntimeException("Error interno al validar el usuario");
        }
    }
}

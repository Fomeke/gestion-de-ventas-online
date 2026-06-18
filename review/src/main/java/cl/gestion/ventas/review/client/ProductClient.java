package cl.gestion.ventas.review.client;

import java.util.NoSuchElementException;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.gestion.ventas.review.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductClient {


    private final WebClient webClient;

    public ProductResponse obtenerProductoPorId(Long productId, String token){
        log.info("Obteniendo reviews del producto: {}", productId);
        try{
            return webClient.get()
                    .uri("/{id}", productId)
                    .header("Authorization",token)
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block();
        }catch(WebClientResponseException ex){
            log.error("Error al obtener informacion de las reviews del producto con la id: {}", productId, ex);
            switch(ex.getStatusCode().value()){
                case 404 -> throw new NoSuchElementException("No se encontro ese producto");
                
                default -> throw new RuntimeException("Error interno en el servidor");
            }
        }
    }
}

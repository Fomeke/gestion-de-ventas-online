package cl.gestion.ventas.product.client;

import java.util.List;
import java.util.NoSuchElementException;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.gestion.ventas.product.dto.CategoryResponseDTO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CategoryClient {

    @Autowired
    private WebClient webClient;

    public CategoryResponseDTO obtenerProductoPorCategoriaId(Long id, String token){
        log.info("Obteniendo productos por la categoria: {}", id);
        try{
            return webClient.get()
                    .uri("/porid/{id}", id)
                    .header("Authorization",token)
                    .retrieve()
                    .bodyToMono(CategoryResponseDTO.class)
                    .block();
        }catch (WebClientResponseException ex){
            log.error("Error al obtener informacion de la categoria con id: {}", id,ex);
            switch(ex.getStatusCode().value()){
                case 404 -> throw new NoSuchElementException("No se encontro esa categoria: {}" + id);

                default -> throw new RuntimeException("Error interno en el servidor");
            }
        }
    }
}

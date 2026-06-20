package cl.gestion.ventas.inventory.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(new Info()
                                        .title("API de Inventario")
                                        .version("1.0")
                                        .description("API para la gestión de inventario"))
                            .servers(List.of(
                                        new Server().url("http://localhost:8083/api/").description("Servidor local"),
                                        new Server().url("http://localhost:8080").description("Vía API Gateway")
                            ));
    }
}

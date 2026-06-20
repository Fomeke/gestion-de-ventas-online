package cl.gestion.ventas.cart.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    final String securitySchemeName = "BearerAuth";

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(new Info()
                                        .title("API de Carritos de Compra")
                                        .version("1.0")
                                        .description("API para la gestión de carritos de compra"))
                            .servers(List.of(
                                        new Server().url("http://localhost:8082/api/").description("Servidor local"),
                                        new Server().url("http://localhost:8080").description("Vía API Gateway")))
                            .components(new Components()
                                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                    .name(securitySchemeName)
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                                    .description("Ingresa directamente tu token JWT generado por el servicio de Auth.")))
                            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}

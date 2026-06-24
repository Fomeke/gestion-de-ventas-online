package cl.gestion.ventas.order.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${GATEWAY_PUBLIC_URL:http:localhost:8080}")
    private String gatewayPublicUrl;

    @Bean
    public OpenAPI customOpenAPI(){

        List<Server> servers = new ArrayList<>();

        servers.add(new Server().url("/api").description("Vía API Gateway"));

        if(gatewayPublicUrl.contains("localhost") || gatewayPublicUrl.contains("127.0.0.1")){
            servers.add(new Server().url("http://localhost:8084/api").description("Servidor local"));
        }

        return new OpenAPI().info(new Info()
                                        .title("API de Ordenes de compra")
                                        .version("1.0")
                                        .description("API para la gestión de ordenes de compra"))
                            .servers(servers)
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

package cl.gestion.ventas.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.order.baseUrl}")
    private String orderBaseUrl;

    @Value("${services.auth.baseUrl}")
    private String authBaseUrl;

    @Bean
    public WebClient webClientOrder(){
        return WebClient.builder()
                .baseUrl(orderBaseUrl)
                .build();
    }

    @Bean
    public WebClient webClientAuth(){
        return WebClient.builder()
                .baseUrl(authBaseUrl)
                .build();
    }
}

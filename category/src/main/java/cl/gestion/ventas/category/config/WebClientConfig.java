package cl.gestion.ventas.category.config;

import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.context.annotation.Bean;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {
    @Value("${services.product.baseUrl}")
    private String baseUrl;


    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}

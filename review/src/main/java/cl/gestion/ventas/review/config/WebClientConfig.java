package cl.gestion.ventas.review.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @Value("${services.product.baseUrl}")
    private String productbaseUrl;




    @Bean
    public WebClient productWebClient(){
        return WebClient.builder()
                .baseUrl(productbaseUrl)
                .build();
    }

  
}

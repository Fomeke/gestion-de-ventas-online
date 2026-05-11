package cl.gestion.ventas.cart.client;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor jwtRequestInterceptor(){
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null){
                String token = attributes.getRequest().getHeader("Authorization");
                if(token != null){
                    template.header("Authorization", token);
                }
            }
        };
    }
}

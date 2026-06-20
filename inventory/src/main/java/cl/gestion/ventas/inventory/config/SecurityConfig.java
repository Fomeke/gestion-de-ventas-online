package cl.gestion.ventas.inventory.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cl.gestion.ventas.inventory.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;



/**
 * SecurityConfig configura la seguridad de la aplicación de Spring Security,
 * desactiva la proteccion de falsificación de solicitud entre sitios (csrf),
 * establece politica de sesiones sin estado para ser compatible con JWT y
 * permite el acceso libre solamente a solicitudes con tokens válidos.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/swagger-ui/**","/v3/api-docs/**","/swagger-ui.html","/actuator/**")
                        .permitAll().anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}

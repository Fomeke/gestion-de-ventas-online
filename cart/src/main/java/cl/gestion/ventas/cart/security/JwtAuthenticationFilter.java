package cl.gestion.ventas.cart.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cl.gestion.ventas.cart.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase aplica un filtro a cada solicitud HTTP antes de ser procesada,
 * primero revisando que el header Authorization sea de tipo JWT Bearer,
 * luego verifica que el token sea valido. No niega las solicitudes 
 * derechamente sino que devuelve observaciones para que las
 * siguientes capas (SecurityConfig) tomen las decisiones que correspondan.
 */

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filter.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (jwtService.isTokenValid(token)) {
            Claims claims = jwtService.extractClaims(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Token válido para usuario: {}", claims.getSubject());
        } else {
            log.warn("Token inválido en solicitud a: {}", request.getRequestURI());
        }

        filter.doFilter(request, response);
    }
}

package cl.gestion.ventas.auth.security;

import java.io.IOException;
import java.util.List;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cl.gestion.ventas.auth.repository.UserRepository;
import cl.gestion.ventas.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase aplica un filtro a cada solicitud HTTP antes de ser procesada,
 * primero revisando que el header Authorization sea de tipo JWT Bearer,
 * luego verifica que el usuario exista y que su token sea valido. No niega
 * las solicitudes derechamente sino que devuelve observaciones para que las
 * siguientes capas (SecurityConfig) tomen las decisiones que correspondan.
 */

 

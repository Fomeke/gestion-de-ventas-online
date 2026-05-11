package cl.gestion.ventas.inventory.service;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtService es un servicio que utiliza las variables secret y expiration
 * de application.properties para firmar y generar los JSON Web Tokens.
 * 
 * getSigningKey transforma la llave secreta a un objeto criptografico SecretKey.
 * 
 * generateToken genera el token con datos asociados como nombre de usuario,
 *      expiracion y firma con la SecretKey generada anteriormente.
 * 
 * extractClaims e isTokenValid verifican la validez del token entrante 
 * usando el SecretKey generado por getSigningKey.
 */

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;


    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            log.error("JWT inválido: {}", e.getMessage());
            return false;
        }
    }
}

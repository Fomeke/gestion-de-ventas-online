package cl.gestion.ventas.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.auth.dto.AuthResponse;
import cl.gestion.ventas.auth.dto.LoginRequest;
import cl.gestion.ventas.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * AuthController es el controlador que permite el inicio de sesión, tal
 * como aparece en SecurityConfig, es el único recurso de la aplicación
 * al que se puede acceder sin un JWT válido. Pide un usuario y contraseña
 * ya existente (en V2__initial_data.sql) que el controlador procesa 
 * como un LoginRequest y lo deriva al authService para validar y generar
 * el JWT.
 */

@RestController
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        log.info("Iniciando sesión con usuario: {}",request.getUsername());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}

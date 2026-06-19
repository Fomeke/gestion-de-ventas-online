package cl.gestion.ventas.auth.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.auth.dto.ApiErrorResponse;
import cl.gestion.ventas.auth.dto.AuthResponse;
import cl.gestion.ventas.auth.dto.LoginRequest;
import cl.gestion.ventas.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Tag(name="Control de Autenticación", description="Endpoints para la gestión de sesiones (solo está habilitado el iniciar sesión)")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    @Operation(summary="Iniciar sesión",description="Retorna nombre de usuario y JWT para acceder al resto de los microservicios, con su respectiva duración")
    @ApiResponses(value={
        @ApiResponse(responseCode= "200", description= "Inicio de sesión exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode= "404", description= "Credenciales inválidas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        log.info("Iniciando sesión con usuario: {}",request.getUsername());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}

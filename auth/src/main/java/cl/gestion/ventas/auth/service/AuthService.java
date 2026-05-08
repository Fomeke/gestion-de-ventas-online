package cl.gestion.ventas.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.gestion.ventas.auth.dto.AuthResponse;
import cl.gestion.ventas.auth.dto.LoginRequest;
import cl.gestion.ventas.auth.model.User;
import cl.gestion.ventas.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Authservice maneja el proceso login y autenticación, utiliza el
 * repositorio para autenticar que el usuario exista y passwordEncoder
 * para comparar la contraseña entrante con la guardada en la BD, 
 * además utilizanuevamente el repositorio para extraer el usuario y generar
 * el token con el método generateToken de JwtService, retornando un 
 * AuthResponse hacia el controlador.
 */

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public AuthResponse login(LoginRequest request) {

        if (!userRepository.existsByUsername(request.getUsername())) {
            throw new BadCredentialsException("Usuario no existe");
        }

        User user = userRepository.findByUsername(request.getUsername()).get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Contraseña incorrecta para el usuario: {}", user.getUsername());
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .nombre(user.getUsername())
                .expiresIn(jwtExpiration)
                .build();
    }
}

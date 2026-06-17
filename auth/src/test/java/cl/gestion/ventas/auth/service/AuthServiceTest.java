package cl.gestion.ventas.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import cl.gestion.ventas.auth.dto.AuthResponse;
import cl.gestion.ventas.auth.dto.LoginRequest;
import cl.gestion.ventas.auth.model.User;
import cl.gestion.ventas.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testLogin(){
        LoginRequest request = new LoginRequest("pepito","contrasenatest123");
        User user = User.builder().username("pepito").password("encodedPassword").build();

        ReflectionTestUtils.setField(authService, "jwtExpiration", 3600L);

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("mocked-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mocked-token", response.getToken());
        assertEquals("pepito", response.getNombre());
        assertEquals(3600L, response.getExpiresIn());

        verify(userRepository,times(1)).existsByUsername(request.getUsername());
        verify(userRepository,times(1)).findByUsername(request.getUsername());
        verify(passwordEncoder,times(1)).matches(request.getPassword(), user.getPassword());
        verify(jwtService,times(1)).generateToken(user);
        
    }
}

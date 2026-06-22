package cl.gestion.ventas.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.gestion.ventas.auth.dto.UserRequest;
import cl.gestion.ventas.auth.dto.UserResponse;
import cl.gestion.ventas.auth.mapper.UserMapper;
import cl.gestion.ventas.auth.model.User;
import cl.gestion.ventas.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testObtenerUsuarios(){
        User user = new User();
        UserResponse response = new UserResponse();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);


        List<UserResponse> resultado = userService.obtenerUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(userRepository,times(1)).findAll();
        verify(userMapper,times(1)).toResponse(user);
    }

    @Test
    public void testObtenerUsuarioPorId(){
        Long id = 1L;
        User user = new User();
        UserResponse response = new UserResponse();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse resultado = userService.obtenerUsuarioPorId(id);

        assertNotNull(resultado);
        verify(userRepository,times(1)).findById(id);
        verify(userMapper,times(1)).toResponse(user);
    }

    @Test
    public void testObtenerUsuarioPorNombre(){
        String nombre = "test";
        User user = new User();
        UserResponse response = new UserResponse();

        when(userRepository.findByUsername(nombre)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse resultado = userService.obtenerUsurarioPorNombre(nombre);

        assertNotNull(resultado);
        verify(userRepository,times(1)).findByUsername(nombre);
        verify(userMapper,times(1)).toResponse(user);
    }

    @Test
    public void testCrear(){
        UserRequest request = new UserRequest("usuariotest","Test Perez","1234","test@correo.com","123456789");
        User user = new User();
        User savedUser = new User();
        UserResponse response = new UserResponse();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByFullName(request.getFullName())).thenReturn(false);
        when(userMapper.fromRequest(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(response);

        UserResponse resultado = userService.crear(request);

        assertNotNull(resultado);
        verify(userRepository,times(1)).existsByUsername(request.getUsername());
        verify(userRepository,times(1)).existsByFullName(request.getFullName());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void testEliminar(){
        Long id = 99L;
        when(userRepository.existsById(id)).thenReturn(true);
        doNothing().when(userRepository).deleteById(id);

        userService.eliminar(id);
        verify(userRepository,times(1)).deleteById(id);

    }

    @Test
    public void testModificarUsuario(){
        Long id = 1L;
        UserRequest request = new UserRequest("nuevoNombreUsuario","nuevo@correo.cl","nuevoPass","nuevo Nombre Completo","1234567");
        User userPrevio = new User();
        User userModificado = new User();
        UserResponse response = new UserResponse();

        when(userRepository.findById(id)).thenReturn(Optional.of(userPrevio));
        when(userRepository.save(userPrevio)).thenReturn(userModificado);
        when(userMapper.toResponse(userModificado)).thenReturn(response);

        UserResponse resultado = userService.modificarUsuario(id, request);

        assertNotNull(resultado);
        verify(userRepository,times(1)).findById(id);
        verify(userRepository,times(1)).save(userPrevio);
        verify(userMapper,times(1)).toResponse(userModificado);
    }
}

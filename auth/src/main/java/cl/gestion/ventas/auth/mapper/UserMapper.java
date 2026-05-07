package cl.gestion.ventas.auth.mapper;

import org.springframework.stereotype.Component;

import cl.gestion.ventas.auth.dto.UserRequest;
import cl.gestion.ventas.auth.dto.UserResponse;
import cl.gestion.ventas.auth.model.User;

/**
 * UserMapper es un componente que convierte la entidad User a DTO y viceversa,
 * permitiendo separar la lógica de negocios y la represntacion en respuestas
 * HTTP de las representaciones en entidades de la BD, permitiendo omitir datos
 * sensibles como la contraseña. Adicionalmente aporta flexibilidad al
 * reducir la dependencia entre capas.
 */

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getCorreo())
                .phone(user.getPhone())
                .build();
    }

    public User fromRequest(UserRequest request){
        return User.builder()
                .username(request.getUsername())
                .fullName(request.getFullName())
                .password(request.getPassword())
                .correo(request.getEmail())
                .phone(request.getPhone())
                .build();
    }
}

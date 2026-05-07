package cl.gestion.ventas.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase UserResponse es un DTO que tiene como finalidad ser el formato
 * de respuesta del endpoint, se utiliza esta clase en vez del modelo User para
 * no exponer la contraseña.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String fullName;
    private String email;
    private String phone;
}

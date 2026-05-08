package cl.gestion.ventas.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequest es un Objeto de Transferencia de Datos (DTO) que transporta
 * solamente los datos necesarios del modelo User para hacer una solicitud de
 * inicio de sesión: nombre de usuario y contraseña. Las anotaciones validan
 * que los datos no sean nulos y que sus largos en carácteres no esten fuera
 * de rango.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    private String password;
}

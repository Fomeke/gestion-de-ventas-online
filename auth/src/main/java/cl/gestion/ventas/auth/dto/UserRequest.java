package cl.gestion.ventas.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message= "Nombre de usuario es obligatorio")
    @Size(max=20,min=5,message="El nombre de usuario debe contener entre 5 y 20 carácteres")
    private String username;

    @NotBlank(message="Nombre completo es obligatorio")
    @Size(min=5,max=150,message="El nombre completo debe tener entre 5 y 150 carácteres")
    private String fullName;

    @NotBlank(message="La contraseña es obligatoria")
    @Size(min=5,max=50,message="La contraseña debe tener entre 5 y 50 carácteres")
    private String password;

    @NotBlank(message="El correo es obligatorio")
    @Email(message="Debe tener formato de e-mail")
    @Size(min=5,max=80,message="El correo debe tener entre 5 y 80 carácteres")
    private String email;

    @Size(max=20,message="El telefono debe tener como máximo 20 carácteres")
    private String phone;

}

package cl.gestion.ventas.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Size(max=20,min=3,message="El nombre de usuario debe contener entre 5 y 20 carácteres")
    @Schema(description="Nombre de usuario", example = "pepe")
    private String username;

    @NotBlank(message="Nombre completo es obligatorio")
    @Size(min=5,max=150,message="El nombre completo debe tener entre 5 y 150 carácteres")
    @Schema(description="Nombre completo de usuario", example = "Elver Galarga González")
    private String fullName;

    @NotBlank(message="La contraseña es obligatoria")
    @Size(min=5,max=50,message="La contraseña debe tener entre 5 y 50 carácteres")
    @Schema(description="Contraseña de usuario", example = "contrasenasegura999")
    private String password;

    @NotBlank(message="El correo es obligatorio")
    @Email(message="Debe tener formato de e-mail")
    @Size(min=5,max=80,message="El correo debe tener entre 5 y 80 carácteres")
    @Schema(description="E-mail de usuario", example = "sixseven@yahoo.com")
    private String email;

    @Size(max=20,message="El telefono debe tener como máximo 20 carácteres")
    @Schema(description="Número telefónico de usuario", example = "+56969696969")
    private String phone;

}

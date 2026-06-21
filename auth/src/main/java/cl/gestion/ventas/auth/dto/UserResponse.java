package cl.gestion.ventas.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description="ID del usuario generada automáticamente", example = "67")
    private Long id;
    @Schema(description="Nombre de usuario", example = "1337Hax0r")
    private String name;
    @Schema(description="Nombre completo de usuario", example = "McLOVIN Perez")
    private String fullName;

    @Schema(description="E-mail de usuario", example = "Sebas@duroc.cl")
    private String email;

    @Schema(description="Número telefónico de usuario", example = "+56967676767")
    private String phone;
}

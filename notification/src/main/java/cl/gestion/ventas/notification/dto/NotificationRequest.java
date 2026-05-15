package cl.gestion.ventas.notification.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotNull(message = "Se debe ingresar la id del usuario.")
    private Long userId;
    @NotBlank(message = "El asunto es obligatorio.")
    private String subject;
    @NotBlank(message = "El mensaje no debe estar vacio.")
    private String message;

    private Long orderId;

    private String type;
}

package cl.gestion.ventas.notification.dto;


import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "ID del usuario", example = "2")
    private Long userId;
    @NotBlank(message = "El asunto es obligatorio.")
    @Schema(description = "Asunto de la notificacion", example = "Pago realizado")
    private String subject;
    @NotBlank(message = "El mensaje no debe estar vacio.")
    @Schema(description = "Mensaje a enviar", example = "Su pago a sido realizado con exito")
    private String message;

    @Schema(description = "ID de la orden", example = "1")
    private Long orderId;
    @Schema(description = "Tipo de notificacion a enviar", example = "Email")
    private String type;
}

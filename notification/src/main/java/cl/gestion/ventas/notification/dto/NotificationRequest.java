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
<<<<<<< HEAD
    @Schema(description="ID del usuario que recibe la notificación",example="1")
    private Long userId;
    @NotBlank(message = "El asunto es obligatorio.")
    @Schema(description="Asunto del mensaje de notificación",example="Pedido confirmado")
    private String subject;
    @NotBlank(message = "El mensaje no debe estar vacio.")
    @Schema(description="Mensaje de la notificación",example="Tu pedido #1 ha sido confirmado y será enviado pronto")
    private String message;

    @Schema(description="ID de la orden",example="1")
    private Long orderId;

    @Schema(description="Tipo de notificación",example="SMS")
=======
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
>>>>>>> 628ed770da95f14a56697383f83425bf1d73285d
    private String type;
}

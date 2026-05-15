package cl.gestion.ventas.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdate {
    @NotNull(message="El nuevo estado es obligatorio")
    private String newStatus;
}

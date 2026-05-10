package cl.gestion.ventas.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    @NotNull(message="La ID de producto es obligatoria")
    private Long productId;

    @Min(value=1,message="La cantidad mínima es 1 producto")
    private Integer quantity;

}

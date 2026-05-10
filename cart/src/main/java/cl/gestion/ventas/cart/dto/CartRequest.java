package cl.gestion.ventas.cart.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    @NotNull(message="La ID de usuario es obligatoria")
    private Long userId;

    @NotEmpty(message="El carrito debe tener al menos un producto")
    @Valid
    private List<CartItemDTO> items;

}

package cl.gestion.ventas.cart.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description="ID de usuario dueño del carrito", example = "1")
    private Long userId;

    @NotEmpty(message="El carrito debe tener al menos un producto")
    @Valid
    @Schema(
        description = "Lista de los productos incluidos en la solicitud. Debe contener al menos un elemento.",
        example = "[{\"productId\": 101, \"quantity\": 3}]"
    )
    private List<CartItemDTO> items;

}

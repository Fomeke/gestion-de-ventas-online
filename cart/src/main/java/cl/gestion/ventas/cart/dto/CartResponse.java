package cl.gestion.ventas.cart.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    @Schema(description="ID del carrito",example="1")
    private Long id;

    @Schema(description="ID de usuario dueño del carrito", example = "1")
    private Long userId;
    
    @Schema(
        description = "Lista de los productos del carrito",
        example = "[{\"productId\": 2, \"quantity\": 3}]"
    )
    private List<CartItemDTO> items;

    @Schema(description="Subtotal del carrito",example="10000")
    private BigDecimal total;

}
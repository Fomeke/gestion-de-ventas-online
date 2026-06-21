package cl.gestion.ventas.order.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    @Schema(description="Identificador único de producto", example = "3")
    private Long productId;

    @Schema(description="Cantidad de productos", example = "3")
    private Integer quantity;
    
}

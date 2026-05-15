package cl.gestion.ventas.inventory.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    @NotNull(message="La ID de producto es obligatoria")
    private Long productId;

    @NotNull(message="El stock es obligatorio")
    @Max(value=9999,message="El stock excede la cantidad máxima")
    private Integer stock;
}

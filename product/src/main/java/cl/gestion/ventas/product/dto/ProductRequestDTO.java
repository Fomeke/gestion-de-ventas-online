package cl.gestion.ventas.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequestDTO {

    @NotBlank(message = "Debe ingresar un nombre.")
    @Size(min = 2, max = 50)
    private String name;
    
    @NotNull(message = "Debe ingresar un precio.")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private BigDecimal price;

    @Size(max = 150)
    private String description; 

    @NotNull(message = "Debe ingresar una categoria.")
    private Long categoryId;
}

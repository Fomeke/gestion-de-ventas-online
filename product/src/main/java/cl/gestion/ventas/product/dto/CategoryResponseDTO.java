package cl.gestion.ventas.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDTO {

    private Long id;
    private String nombre;
}

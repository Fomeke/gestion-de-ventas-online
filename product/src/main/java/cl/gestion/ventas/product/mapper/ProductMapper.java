package cl.gestion.ventas.product.mapper;

import org.springframework.stereotype.Component;

import cl.gestion.ventas.product.dto.ProductRequestDTO;
import cl.gestion.ventas.product.dto.ProductResponseDTO;
import cl.gestion.ventas.product.model.Product;

@Component
public class ProductMapper {

    public Product fromRequest(ProductRequestDTO request){

        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .categoryId(request.getCategoryId())
                .build();
    }


    public ProductResponseDTO toResponse(Product product){
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .categoryId(product.getCategoryId())
                .build();
    }
}

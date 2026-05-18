package cl.gestion.ventas.category.mapper;



import org.springframework.stereotype.Component;

import cl.gestion.ventas.category.dto.CategoryRequest;
import cl.gestion.ventas.category.dto.CategoryResponseDTO;
import cl.gestion.ventas.category.model.Category;

@Component
public class CategoryMapper {

    public CategoryResponseDTO toResponse(Category category){
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }


    public Category fromRequest(CategoryRequest request){
        return Category.builder()
                .name(request.getName())
                .build();
    }
}

package cl.gestion.ventas.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.gestion.ventas.category.dto.CategoryRequest;
import cl.gestion.ventas.category.dto.CategoryResponseDTO;
import cl.gestion.ventas.category.mapper.CategoryMapper;
import cl.gestion.ventas.category.model.Category;
import cl.gestion.ventas.category.repository.CategoryRepository;


@ExtendWith(MockitoExtension.class) // activa mocko
public class CategoryServiceTest {

    @Mock
    private CategoryRepository repo;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CategoryService servi; // el servicio a probar

    @Test
    void buscarCategoriaPorIdExito(){
        Long id = 1L;
        Category category = new Category();
        category.setId(id);
        category.setName("Electronica");

        CategoryResponseDTO dto = new CategoryResponseDTO();

        dto.setName("Electronica");
        
        when(repo.findById(id)).thenReturn(Optional.of(category));
        when(mapper.toResponse(category)).thenReturn(dto);

        CategoryResponseDTO resultado = servi.buscarCategoriaPorId(id);

        assertNotNull(resultado);
        assertEquals("Electronica", resultado.getName());
        verify(repo, times(1)).findById(id);
    }


    @Test
    void crearCategoriaExito(){
        
        CategoryRequest request = new CategoryRequest();
        request.setName("CategoriaNueva");
        
        Category category = new Category();
        category.setName("CategoriaNueva");

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setName("CategoriaNueva");

        when(repo.findByName("CategoriaNueva")).thenReturn(Optional.empty());
        when(mapper.fromRequest(request)).thenReturn(category);
        when(repo.save(category)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(dto);

        CategoryResponseDTO resultado = servi.crearCategoria(request);

        assertNotNull(resultado);
        assertEquals("CategoriaNueva", resultado.getName());
        verify(repo, times(1)).save(category);

    }

    @Test
    void EliminarCategoriaExito(){
        Long id = 67L;
    
        when(repo.existsById(id)).thenReturn(true);
        servi.eliminarCategoria(id);
        verify(repo,times(1)).deleteById(id);
    }

    @Test
    void crearCategoriaExistenteError(){

        CategoryRequest request = new CategoryRequest();
        request.setName("Electronica");

        Category categoryExistente = new Category();
        categoryExistente.setName("Electronica");

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setName("Electronica");

        when(repo.findByName("Electronica")).thenReturn(Optional.of(categoryExistente));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servi.crearCategoria(request);
        });

        assertEquals("Esa categoria ya existe.", exception.getMessage());
        verify(repo, never()).save(any());

    }


    @Test
    void EliminarCategoriaError(){

        Long id = 67L;

        when(repo.existsById(id)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,() ->{
            servi.eliminarCategoria(id);
        } );

        assertEquals("No se encontro la categoria con esa id.", exception.getMessage());
        verify(repo, never()).deleteById(id);
    }
}

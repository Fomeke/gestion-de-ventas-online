package cl.gestion.ventas.category.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
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
    
    @Test
    void buscarCategoriasExito() {
        when(repo.findAll()).thenReturn(java.util.List.of(new Category()));
        assertNotNull(servi.buscarCategorias());
        verify(repo, times(1)).findAll();
    }

    @Test
    void modificarCategoriaExito() {
        Long id = 1L;
        CategoryRequest request = new CategoryRequest();
        request.setName("Categoria Modificada");
        Category category = new Category();
        CategoryResponseDTO dto = new CategoryResponseDTO();

        when(repo.existsById(id)).thenReturn(true);
        when(repo.findById(id)).thenReturn(Optional.of(category));
        when(repo.save(category)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(dto);

        assertNotNull(servi.modificarCategoria(id, request));
        verify(repo, times(1)).save(category);
    }

    @Test
    void buscarCategoriaPorIdError(){
        Long categoryId = 99L;
        when(repo.findById(categoryId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, (Executable) () ->
            servi.buscarCategoriaPorId(categoryId));

        assertEquals("No se encontro esa categoria.", exception.getMessage());
        
        verify(repo, times(1)).findById(categoryId);
        verifyNoInteractions(mapper);
    }

    @Test
    void modificarCategoriaError(){
        Long categoryId = 1L;
        CategoryRequest request = new CategoryRequest("Ropa");
        Category duplicateCategory = new Category(); 

        when(repo.existsById(categoryId)).thenReturn(true);
        when(repo.findByName(request.getName())).thenReturn(Optional.of(duplicateCategory));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servi.modificarCategoria(categoryId, request);
        });

        assertEquals("Ya existe esa categoria", exception.getMessage());

        verify(repo, times(1)).existsById(categoryId);
        verify(repo, times(1)).findByName(request.getName());
        verify(repo, never()).findById(anyLong());
        verify(repo, never()).save(any(Category.class));
    }
}

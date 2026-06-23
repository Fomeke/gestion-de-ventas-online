package cl.gestion.ventas.product.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.gestion.ventas.product.client.CategoryClient;
import cl.gestion.ventas.product.dto.ProductRequestDTO;
import cl.gestion.ventas.product.dto.ProductResponseDTO;
import cl.gestion.ventas.product.mapper.ProductMapper;
import cl.gestion.ventas.product.model.Product;
import cl.gestion.ventas.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService servi;
    
    @Mock
    private ProductMapper mapper;

    @Mock
    private ProductRepository repo;

    @Mock
    private CategoryClient client;

    @Test
    void productoPorIdExito(){

        Long id = 1L;

        Product product = new Product();
        ProductResponseDTO response = new ProductResponseDTO();

        when(repo.findById(id)).thenReturn(Optional.of(product));
        when(mapper.toResponse(product)).thenReturn(response);

        ProductResponseDTO resultado= servi.productoPorId(id);

        assertNotNull(resultado);
        verify(repo, times(1)).findById(id);
    }

    @Test
    void crearProductoExito(){
        String token = "Bearer m1t0ken";
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Notebook");
        request.setCategoryId(2L);
        
        Product product = new Product();
        ProductResponseDTO response = new ProductResponseDTO();
        response.setName("Notebook");

        when(repo.existsByName("Notebook")).thenReturn(false);

        when(client.obtenerProductoPorCategoriaId(2L, token)).thenReturn(null);

        when(mapper.fromRequest(request)).thenReturn(product);
        when(repo.save(product)).thenReturn(product);
        when(mapper.toResponse(product)).thenReturn(response);

        ProductResponseDTO resultado = servi.crearProducto(request, token);

        assertEquals("Notebook", resultado.getName());

        verify(client, times(1)).obtenerProductoPorCategoriaId(2L, token);
        verify(repo,times(1)).save(product);
    }

    @Test
    void eliminarProductoExito(){
        Long id = 1L;

        when(repo.existsById(id)).thenReturn(true);

        servi.eliminarProducto(id);
        verify(repo, times(1)).deleteById(id);
    }

    @Test
    void eliminarProductoError(){
        Long id = 67L;

        when(repo.existsById(id)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,() ->{
            servi.eliminarProducto(id);
        } );

        assertEquals("No se encontro el producto con esa id.", exception.getMessage());
        verify(repo, never()).deleteById(id);
    }

    @Test
    void crearProductoError(){
        String token = "Bearer m1t0ken";
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Notebook");

        when(repo.existsByName("Notebook")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servi.crearProducto(request, token);
        });

        assertEquals("El nombre de ese producto ya existe.", exception.getMessage());

        
        verify(client, never()).obtenerProductoPorCategoriaId(anyLong(), anyString());
        verify(repo,never()).save(any());
        
    }
    @Test
    void modificarProducto_Exito() {
        
        Long id = 1L;
        String token = "Bearer m1t0k3n";
        
        
        Product productExistente = new Product();
        productExistente.setId(id);
        productExistente.setName("Notebook Viejo");
        
        
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Notebook Nuevo");
        request.setCategoryId(3L);
        
        
        ProductResponseDTO response = new ProductResponseDTO();
        response.setName("Notebook Nuevo");
        

        
        when(repo.findById(id)).thenReturn(Optional.of(productExistente));
        when(client.obtenerProductoPorCategoriaId(3L, token)).thenReturn(null); // Simulamos el Feign Client
        when(repo.save(any(Product.class))).thenReturn(productExistente);
        when(mapper.toResponse(productExistente)).thenReturn(response);

        
        ProductResponseDTO resultado = servi.modificarProducto(id, request, token);

        
        assertEquals("Notebook Nuevo", resultado.getName());
        assertEquals(150000.0, resultado.getPrice()); // Confirmamos que el precio cambió
        verify(repo, times(1)).save(productExistente);
        verify(client, times(1)).obtenerProductoPorCategoriaId(3L, token);
    }

    @Test
    void buscarProductosExito() {
        when(repo.findAll()).thenReturn(java.util.List.of(new Product()));
        assertNotNull(servi.buscarProductos());
        verify(repo, times(1)).findAll();
    }
}


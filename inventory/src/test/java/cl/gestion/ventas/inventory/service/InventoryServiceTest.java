package cl.gestion.ventas.inventory.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.gestion.ventas.inventory.client.ProductClient;
import cl.gestion.ventas.inventory.dto.InventoryRequest;
import cl.gestion.ventas.inventory.dto.InventoryResponse;
import cl.gestion.ventas.inventory.dto.ProductResponse;
import cl.gestion.ventas.inventory.mapper.InventoryMapper;
import cl.gestion.ventas.inventory.model.Inventory;
import cl.gestion.ventas.inventory.repository.InventoryRepository;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    public void testObtenerInventorios(){
        Inventory inv = new Inventory();
        InventoryResponse response = new InventoryResponse();

        when(inventoryRepository.findAll()).thenReturn(List.of(inv));
        when(inventoryMapper.toResponse(inv)).thenReturn(response);

        List<InventoryResponse> resultado = inventoryService.obtenerInventorios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inventoryRepository,times(1)).findAll();
    }

    @Test
    public void testObtenerInventarioPorIdProducto(){
        Long id = 1L;
        Inventory inv = new Inventory();
        InventoryResponse response = new InventoryResponse();

        when(inventoryRepository.findByProductId(id)).thenReturn(Optional.of(inv));
        when(inventoryMapper.toResponse(inv)).thenReturn(response);

        InventoryResponse resultado = inventoryService.obtenerInventarioPorIdProducto(id);

        assertNotNull(resultado);
        verify(inventoryRepository,times(1)).findByProductId(id);
        verify(inventoryMapper,times(1)).toResponse(inv);
    }

    @Test
    public void testCrear(){
        InventoryRequest request = new InventoryRequest(1L,3);
        Inventory inv = new Inventory();
        Inventory savedInv = new Inventory();
        InventoryResponse response = new InventoryResponse();
        ProductResponse product = new ProductResponse();

        when(inventoryRepository.existsByProductId(request.getProductId())).thenReturn(false);
        when(productClient.getProductById(request.getProductId())).thenReturn(product);
        when(inventoryMapper.fromRequest(request)).thenReturn(inv);
        when(inventoryRepository.save(inv)).thenReturn(savedInv);
        when(inventoryMapper.toResponse(savedInv)).thenReturn(response);

        InventoryResponse resultado = inventoryService.crear(request);

        assertNotNull(resultado);
        verify(inventoryRepository,times(1)).existsByProductId(request.getProductId());
        verify(productClient,times(1)).getProductById(request.getProductId());
        verify(inventoryRepository,times(1)).save(inv);
    }

    @Test
    public void testEliminar(){
        Long id = 1L;
        when(inventoryRepository.existsByProductId(id)).thenReturn(true);
        doNothing().when(inventoryRepository).deleteByProductId(id);

        inventoryService.eliminar(id);
        verify(inventoryRepository,times(1)).deleteByProductId(id);
        
    }

    @Test
    public void testCambiarStockLista(){
        InventoryRequest item1 = new InventoryRequest(1L,5);
        InventoryRequest item2 = new InventoryRequest(2L,-10);
        List<InventoryRequest> items = List.of(item1,item2);

        Inventory inv1 = Inventory.builder().productId(1L).stock(20).build();
        Inventory inv2 = Inventory.builder().productId(2L).stock(50).build();

        when(inventoryRepository.findByProductId(item1.getProductId())).thenReturn(Optional.of(inv1));
        when(inventoryRepository.findByProductId(item2.getProductId())).thenReturn(Optional.of(inv2));

        when(inventoryMapper.toResponse(inv1)).thenReturn(new InventoryResponse());
        when(inventoryMapper.toResponse(inv2)).thenReturn(new InventoryResponse());

        assertDoesNotThrow((Executable) () -> inventoryService.cambiarStockLista(items));
        
        assertEquals(25, inv1.getStock());
        assertEquals(40, inv2.getStock());

        verify(inventoryRepository,times(1)).findByProductId(1L);
        verify(inventoryRepository,times(1)).findByProductId(2L);
        verify(inventoryMapper, times(1)).toResponse(inv1);
        verify(inventoryMapper, times(1)).toResponse(inv2);
    }
    
}

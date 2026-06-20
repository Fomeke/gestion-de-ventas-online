package cl.gestion.ventas.inventory.service;

import java.util.List;
import java.util.NoSuchElementException;


import org.springframework.stereotype.Service;

import cl.gestion.ventas.inventory.client.ProductClient;
import cl.gestion.ventas.inventory.dto.InventoryRequest;
import cl.gestion.ventas.inventory.dto.InventoryResponse;

import cl.gestion.ventas.inventory.mapper.InventoryMapper;
import cl.gestion.ventas.inventory.model.Inventory;
import cl.gestion.ventas.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;

    private final ProductClient productClient;


    public List<InventoryResponse> obtenerInventorios(){
        log.info("Obteniendo todos los inventorios");
        List<Inventory> invs = inventoryRepository.findAll();
        return invs.stream().map(inventoryMapper::toResponse).toList(); 
    }

    public InventoryResponse obtenerInventarioPorIdProducto(Long id){
        log.info("Obteniendo inventario de Producto {}",id);
        Inventory inv = inventoryRepository.findByProductId(id).get();
        return inventoryMapper.toResponse(inv);
    }

    @Transactional
    public InventoryResponse crear(InventoryRequest request){
        log.info("Creando nuevo inventorio");
        if(inventoryRepository.existsByProductId(request.getProductId())){
            throw new IllegalArgumentException("El producto ya existe");
        }
        productClient.getProductById(request.getProductId());
        Inventory inv = inventoryRepository.save(inventoryMapper.fromRequest(request));
        return inventoryMapper.toResponse(inv);
    }

    @Transactional
    public void eliminar(Long id){
        log.info("Eliminando inventario de producto: {}",id);
        if(!inventoryRepository.existsByProductId(id)){
            throw new NoSuchElementException("Inventorio no encontrado");
        }
        inventoryRepository.deleteByProductId(id);
    }

    @Transactional
    public InventoryResponse cambiarStock(Long productId, Integer stock){
        log.info("Cambiando stock de producto {}",productId);

        Inventory inv = inventoryRepository.findByProductId(productId).orElseThrow(() -> new NoSuchElementException("Inventario no encontrado"));

        int nuevostock = inv.getStock()+stock;
        if(nuevostock<0){
            throw new IllegalArgumentException("Stock insuficiente de producto "+productId+": El stock actual es "+inv.getStock()+" y se intento restar "+Math.abs(stock));
        }
        inv.setStock(nuevostock);

        return inventoryMapper.toResponse(inv);

    }

    @Transactional
    public void cambiarStockLista(List<InventoryRequest> items){
        for (InventoryRequest item: items){
            cambiarStock(item.getProductId(), item.getStock());
        }
    }
}

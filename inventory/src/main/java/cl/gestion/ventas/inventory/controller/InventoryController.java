package cl.gestion.ventas.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.inventory.dto.InventoryRequest;
import cl.gestion.ventas.inventory.dto.InventoryResponse;
import cl.gestion.ventas.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/inventories")
@Slf4j
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;


    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId){
        log.info("GET /inventories/{}",productId);
        return ResponseEntity.ok(inventoryService.obtenerInventarioPorIdProducto(productId));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getInventories(@RequestHeader("Authorization") String token){
        log.info("GET /inventories");
        return ResponseEntity.ok(inventoryService.obtenerInventorios());
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> add(@Valid @RequestBody InventoryRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.crear(request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeInventory(@PathVariable Long productId){
        inventoryService.eliminar(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponse> updateStock(@PathVariable Long productId, @RequestParam Integer quantity){
        return ResponseEntity.ok(inventoryService.cambiarstock(productId, quantity));

    }
}

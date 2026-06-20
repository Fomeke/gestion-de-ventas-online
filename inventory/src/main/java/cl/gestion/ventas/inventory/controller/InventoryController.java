package cl.gestion.ventas.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.inventory.dto.ApiErrorResponse;
import cl.gestion.ventas.inventory.dto.InventoryRequest;
import cl.gestion.ventas.inventory.dto.InventoryResponse;
import cl.gestion.ventas.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/inventories")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Endpoints para el control de stock de productos")
@ApiResponse(responseCode = "403", description = "No autorizado - Token JWT ausente, expirado o inválido", content = @Content(mediaType = "application/json"))
public class InventoryController {
    private final InventoryService inventoryService;


    @GetMapping("/{productId}")
    @Operation(summary = "Obtener inventario por producto", description = "Retorna el estado de stock actual de un producto específico mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado para el producto indicado", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId){
        log.info("GET /inventories/{}",productId);
        return ResponseEntity.ok(inventoryService.obtenerInventarioPorIdProducto(productId));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los inventarios", description = "Retorna una lista completa de los registros de inventario en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inventarios obtenida exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<List<InventoryResponse>> getInventories(){
        log.info("GET /inventories");
        return ResponseEntity.ok(inventoryService.obtenerInventorios());
    }

    @PostMapping
    @Operation(summary = "Crear registro de inventario", description = "Inicializa el inventario físico para un nuevo producto en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro de inventario creado exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto con integridad de datos (ej: crear inventario ya existente)", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<InventoryResponse> add(@Valid @RequestBody InventoryRequest request){
        log.info("POST /inventories");
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.crear(request));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Eliminar inventario de un producto", description = "Remueve el registro de stock asociado a un ID de producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventario eliminado exitosamente"),
            @ApiResponse(responseCode = "444", description = "Inventario no encontrado", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> removeInventory(@PathVariable Long productId){
        log.info("DELETE /inventories/{}",productId);
        inventoryService.eliminar(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Actualizar cantidad de stock de un producto", description = "Modifica la cantidad disponible en almacén para un producto determinado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetro inválido (ej: que el cambio resulte en stock negativo)", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado (NoSuchElementException)", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno o de servicio externo (FeignException)", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<InventoryResponse> updateStock(@PathVariable Long productId, @RequestParam Integer quantity){
        log.info("PUT /inventories/{}?quantity={}",productId,quantity);
        return ResponseEntity.ok(inventoryService.cambiarStock(productId, quantity));

    }

    @PutMapping("/bulk-update")
    @Operation(summary = "Actualización masiva de inventarios", description = "Permite modificar el stock de múltiples productos simultáneamente. Útil para sincronizaciones desde servicio de ordenes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualización en lote procesada de forma correcta"),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud inválido o fallas de validación en la lista", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<Void> bulkUpdateStock(@Valid @RequestBody List<InventoryRequest> items){
        log.info("PUT /inventories/bulk-update");
        inventoryService.cambiarStockLista(items);
        return ResponseEntity.ok().build();
    }
}

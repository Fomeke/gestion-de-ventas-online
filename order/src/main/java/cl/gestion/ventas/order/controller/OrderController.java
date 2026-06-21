package cl.gestion.ventas.order.controller;

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
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.order.dto.ApiErrorResponse;
import cl.gestion.ventas.order.dto.OrderRequest;
import cl.gestion.ventas.order.dto.OrderResponse;
import cl.gestion.ventas.order.dto.OrderResponseForShipping;
import cl.gestion.ventas.order.dto.OrderSmallResponse;
import cl.gestion.ventas.order.dto.OrderStatusUpdate;
import cl.gestion.ventas.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/orders")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Órdenes", description = "Endpoints para la creación, consulta y gestión de estados de las órdenes de compra")
@ApiResponses(value = {
    @ApiResponse(responseCode = "403", description = "No autorizado - Token JWT ausente, expirado o inválido", 
                 content = @Content(mediaType = "application/json")),
    @ApiResponse(responseCode = "500", description = "Error interno - Error inesperado en el servidor", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(responseCode = "400", description = "Solicitud inválida - Error de validación en los campos o estado no permitido", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(responseCode = "404", description = "Orden no encontrada", 
             content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
})
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Obtener todas las órdenes", description = "Retorna una lista completa con todas las órdenes de compra registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    public ResponseEntity<List<OrderResponse>> getOrders() {
        log.info("GET /orders");
        return ResponseEntity.ok(orderService.obtenerOrdenes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID", description = "Recupera los detalles de una orden de compra mediante su identificador.")
    @ApiResponse(responseCode = "200", description = "Orden encontrada exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    public ResponseEntity<OrderResponse> getOrderId(@Parameter(description = "Identificador único de la orden", example = "1") @PathVariable Long id) {
        log.info("GET /orders/{}", id);
        return ResponseEntity.ok(orderService.obtenerOrdenPorId(id));
    }

    @GetMapping("/noitems/{id}")
    @Operation(summary = "Obtener orden resumida (sin ítems)", description = "Recupera una orden por su ID omitiendo el desglose de productos.")
    @ApiResponse(responseCode = "200", description = "Orden resumida encontrada exitosamente", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderSmallResponse.class)))
    public ResponseEntity<OrderSmallResponse> getSmallOrderId(@Parameter(description = "Identificador único de la orden", example = "1") @PathVariable Long id) {
        log.info("GET /orders/noitems/{}", id);
        return ResponseEntity.ok(orderService.obtenerOrdenResumidaPorId(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener órdenes de un usuario", description = "Retorna las órdenes pertenecientes a un usuario en específico.")
    @ApiResponse(responseCode = "200", description = "Órden(es) recuperadas exitosamente", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@Parameter(description = "Identificador único del usuario", example = "1") @PathVariable Long userId) {
        log.info("GET /orders/user/{}", userId);
        return ResponseEntity.ok(orderService.obtenerOrdenesPorUserId(userId));
    }

    @GetMapping("/ship/{id}")
    @Operation(summary = "Obtener orden optimizada para envíos", description = "Endpoint para la comunicación con el servicio de envíos, retornando la estructura necesaria para procesar el envío.")
    @ApiResponse(responseCode = "200", description = "Estructura de envío recuperada con éxito", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseForShipping.class)))
    public ResponseEntity<OrderResponseForShipping> getOrderForShipping(@Parameter(description = "Identificador único de la orden", example = "1") @PathVariable Long id) {
        log.info("GET /orders/{}", id);
        return ResponseEntity.ok(orderService.obtenerOrdenParaEnvio(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva orden", description = "Registra una orden de compra en el sistema basándose en el contenido actual del carrito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden procesada y creada exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto con base de datos o lógica de stock", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<OrderResponse> add(@Valid @RequestBody OrderRequest request) {
        log.info("POST /orders");
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.crear(request));
    }

    @PutMapping("/{orderId}")
    @ApiResponse(responseCode = "200", description = "Estado de la orden actualizado exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    public ResponseEntity<OrderResponse> updateState(@Parameter(description = "Identificador único de la orden", example = "1") @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdate request) {
        log.info("PUT /orders/{}", orderId);
        return ResponseEntity.ok(orderService.editarEstado(orderId, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una orden", description = "Remueve el registro de una orden de compra por su ID.")
    @ApiResponse(responseCode = "204", description = "Orden eliminada exitosamente")
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "Identificador único de la orden", example = "1") @PathVariable Long id) {
        log.info("DELETE /v1/orders/{}", id);
        orderService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}

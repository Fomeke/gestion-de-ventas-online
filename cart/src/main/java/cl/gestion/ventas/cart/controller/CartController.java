package cl.gestion.ventas.cart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.cart.dto.ApiErrorResponse;
import cl.gestion.ventas.cart.dto.CartRequest;
import cl.gestion.ventas.cart.dto.CartResponse;
import cl.gestion.ventas.cart.service.CartService;
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
@RequestMapping("/v1/carts")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Controlador de Carritos de Compra", description = "Endpoints para la gestión y administración de los carritos de compra")
@ApiResponse(responseCode = "403", description = "No autorizado - Token JWT ausente, expirado o inválido", content = @Content(mediaType = "application/json"))
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"}, allowCredentials = "true")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Obtener todos los carritos", description = "Retorna una lista con todos los carritos registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de carritos recuperada con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<List<CartResponse>> getAllCarts(){
        return ResponseEntity.ok(cartService.obtenerCarritos());
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Obtener carrito por ID de usuario", description = "Recupera el carrito de compras activo de un usuario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito encontrado con éxito"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error inesperado", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<CartResponse> getCart(@Parameter(description = "Identificador único de usuario", example = "1")
                                                @PathVariable Long userId) {
        return ResponseEntity.ok(cartService.obtenerCarritoPorId(userId));
    }

    @PostMapping
    @Operation(summary = "Agregar productos al carrito", description = "Agrega ítems al carrito del usuario autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto agregado y carrito actualizado con éxito"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "JWT ausente/inválido o se intenta modificar carrito ajeno", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto con la integridad de los datos (ej: solicitud excede stock)", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<CartResponse> add(@Valid @RequestBody CartRequest request, Authentication auth){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.agregarProductosACarrito(Long.valueOf(auth.getName()), request));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    @Operation(summary = "Eliminar un producto del carrito", description = "Remueve un producto específico del carrito de un usuario. Requiere validación de permisos de dueño.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado con éxito (Sin contenido)"),
        @ApiResponse(responseCode = "403", description = "JWT ausente/inválido o se intenta modificar carrito ajeno", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado en el carrito.", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<Void> removeItem(@Parameter(description = "Identificador único de usuario", example = "1") @PathVariable Long userId, 
                                           @Parameter(description = "Identificador único de producto", example = "2")@PathVariable Long productId, 
                                           Authentication auth){
        cartService.eliminarProductoDeCarrito(userId, productId,Long.valueOf(auth.getName()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Vaciar o eliminar el carrito", description = "Elimina por completo el carrito de compras del usuario indicado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Carrito eliminado con éxito"),
        @ApiResponse(responseCode = "403", description = "JWT ausente/inválido o se intenta eliminar carrito ajeno", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Carrito inexistente", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<Void> removeCart(@Parameter(description = "Identificador único de usuario", example = "1") @PathVariable Long userId, 
                                           Authentication auth){
        cartService.eliminarCarrito(userId,Long.valueOf(auth.getName()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Actualizar cantidades del carrito", description = "Modifica los ítems y cantidades totales del carrito asignado a un usuario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito modificado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estructura errónea o datos no válidos", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "JWT ausente/inválido o se intenta modificar carrito ajeno", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado", 
                     content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    public ResponseEntity<CartResponse> updateCart(@Parameter(description = "Identificador único de usuario", example = "1") @PathVariable Long userId,
            @RequestBody @Valid CartRequest request, Authentication auth){
                Long tokenUserId = Long.valueOf(auth.getName());
                log.info("Actualizando carrito del usuario: {}",userId);
                return ResponseEntity.ok(cartService.actualizarCarrito(userId, request, tokenUserId));
            }
    
}

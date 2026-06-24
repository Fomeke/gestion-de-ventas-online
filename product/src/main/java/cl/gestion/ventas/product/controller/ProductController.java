package cl.gestion.ventas.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.product.dto.ProductRequestDTO;
import cl.gestion.ventas.product.dto.ProductResponseDTO;
import cl.gestion.ventas.product.model.Product;
import cl.gestion.ventas.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApiResponses({
        @ApiResponse(responseCode = "403", description = "No autorizado (No tienes permisos)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
@RestController
@Slf4j
@RequestMapping("/v1/product")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Endpoints para la gestion del catalogo de productos")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"}, allowCredentials = "true")
public class ProductController {

    private final ProductService service;

    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista completa con todos los productos registrados en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Product>> getProductos() {
        log.info("GET api/product/buscarProductos");
        return ResponseEntity.ok(service.buscarProductos());
    }

    @Operation(summary = "Buscar producto por ID", description = "Busca y retorna los detalles de un producto especifico mediante su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductoPorId(@PathVariable Long id) {
        log.info("GET api/product/productoPorId");
        return ResponseEntity.ok(service.productoPorId(id));
    }

    @Operation(summary = "Crear nuevo producto", description = "Registra un nuevo producto internamente valida mediante webclient que la categoria asignada exista.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o producto ya existente")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> postProducto(@Valid @RequestBody ProductRequestDTO request, @RequestHeader("Authorization") String token){
        log.info("POST api/product/crearProducto");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearProducto(request, token));
    }

    @Operation(summary = "Modificar producto", description = "Actualiza los datos de un producto si se cambia la categoria valida su existencia con el microservicio category.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> putProducto(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request,
                @RequestHeader("Authorization") String token
    ){
        log.info("PUT api/product/modificarProducto/{}",id);
        return ResponseEntity.ok().body(service.modificarProducto(id, request, token));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina de forma permanente un producto de la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado con exito (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        log.info("DELETE api/product/eliminarProducto/{}",id);
        service.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}

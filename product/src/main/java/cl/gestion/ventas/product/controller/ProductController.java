package cl.gestion.ventas.product.controller;

import java.util.List;


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
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.product.dto.ProductRequestDTO;
import cl.gestion.ventas.product.dto.ProductResponseDTO;
import cl.gestion.ventas.product.model.Product;
import cl.gestion.ventas.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> getProductos() {
        log.info("GET api/product/buscarProductos");
        return ResponseEntity.ok(service.buscarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductoPorId(@PathVariable Long id) {
        log.info("GET api/product/productoPorId");
        return ResponseEntity.ok(service.productoPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> postProducto(@Valid @RequestBody ProductRequestDTO request, @RequestHeader("Authorization") String token){
        log.info("POST api/product/crearProducto");

        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearProducto(request, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> putProducto(@PathVariable Long id,@Valid @RequestBody ProductRequestDTO request,
                @RequestHeader("Authorization") String token
    ){
        log.info("PUT api/product/modificarProducto/{}",id);

        return ResponseEntity.ok().body(service.modificarProducto(id, request, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        log.info("DELETE api/product/eliminarProducto/{}",id);
        
        service.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}

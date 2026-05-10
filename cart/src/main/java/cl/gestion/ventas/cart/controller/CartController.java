package cl.gestion.ventas.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.cart.dto.CartRequest;
import cl.gestion.ventas.cart.dto.CartResponse;
import cl.gestion.ventas.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/carts")
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;


    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.obtenerCarritoPorId(userId));
    }

    @PostMapping
    public ResponseEntity<CartResponse> add(@Valid @RequestBody CartRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.agregarProductosACarrito(request));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long userId, @PathVariable Long productId){
        cartService.eliminarProductoDeCarrito(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeCart(@PathVariable Long userId){
        cartService.eliminarCarrito(userId);
        return ResponseEntity.noContent().build();
    }
}

package cl.gestion.ventas.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping
    public ResponseEntity<List<CartResponse>> getAllCarts(){
        return ResponseEntity.ok(cartService.obtenerCarritos());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.obtenerCarritoPorId(userId));
    }

    @PostMapping
    public ResponseEntity<CartResponse> add(@Valid @RequestBody CartRequest request, Authentication auth){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.agregarProductosACarrito(Long.valueOf(auth.getName()), request));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long userId, @PathVariable Long productId, Authentication auth){
        cartService.eliminarProductoDeCarrito(userId, productId,Long.valueOf(auth.getName()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeCart(@PathVariable Long userId, Authentication auth){
        cartService.eliminarCarrito(userId,Long.valueOf(auth.getName()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable Long userId,
            @RequestBody CartRequest request, Authentication auth){
                Long tokenUserId = Long.valueOf(auth.getName());
                log.info("Actualizando carrito del usuario: {}",userId);
                return ResponseEntity.ok(cartService.actulizarCarrito(userId, request, tokenUserId));
            }
    
}

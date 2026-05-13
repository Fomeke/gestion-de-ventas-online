package cl.gestion.ventas.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import cl.gestion.ventas.cart.client.ProductClient;
import cl.gestion.ventas.cart.dto.CartItemDTO;
import cl.gestion.ventas.cart.dto.CartRequest;
import cl.gestion.ventas.cart.dto.CartResponse;
import cl.gestion.ventas.cart.dto.ProductResponse;
import cl.gestion.ventas.cart.mapper.CartMapper;
import cl.gestion.ventas.cart.model.Cart;
import cl.gestion.ventas.cart.model.CartItem;
import cl.gestion.ventas.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductClient productClient;

    private CartResponse obtenerTotal(Cart cart){
        BigDecimal total = BigDecimal.ZERO;
        List<CartItemDTO> items = new ArrayList<>();

        for(CartItem item: cart.getItems()){
            ProductResponse product = productClient.getProductById(item.getProductId());

            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(subtotal);

            items.add(CartItemDTO.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .build());
        }

        CartResponse response = cartMapper.toResponse(cart);
        response.setItems(items);
        response.setTotal(total);
        return response;
    }
    
    @Transactional
    public CartResponse obtenerCarritoPorId(Long userId){
        log.info("Obteniendo carrido con ID de usuario: {}",userId);
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("Carrito no encontrado"));

        return obtenerTotal(cart);
    }
    
    @Transactional
    public CartResponse agregarProductosACarrito(Long tokenUserId, CartRequest request){
        if(!request.getUserId().equals(tokenUserId)){
            log.warn("Acceso no autorizado: Usuario {} intentó modificar carrito de {}",
                tokenUserId, request.getUserId());
            throw new AccessDeniedException("No tiene permiso para modificar este carrito");
        }

        log.info("Agregando productos a carrito de usuario: {}",request.getUserId());
        
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElse(Cart.builder().userId(request.getUserId()).build());

        request.getItems().forEach(item -> {
            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(i -> i.getProductId().equals(item.getProductId()))
                    .findFirst();
            if(existingItem.isPresent()){
                existingItem.get().setQuantity(existingItem.get().getQuantity() + item.getQuantity());
            }else{
                cart.getItems().add(CartItem.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .cart(cart)
                        .build());
            }
        });

        Cart nuevoCarrito = cartRepository.save(cart);
        return obtenerTotal(nuevoCarrito);
    }

    @Transactional
    public void eliminarProductoDeCarrito(Long userId, Long productId, Long tokenUserId){
        if(!userId.equals(tokenUserId)){
            log.warn("Acceso no autorizado: Usuario {} intentó modificar carrito de {}",
                tokenUserId, userId);
            throw new AccessDeniedException("No tiene permiso para modificar este carrito");
        }
        
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("Carrito no encontrado"));
        log.info("Eliminando producto {} de carrito",productId);
        boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        if(!removed){
            throw new NoSuchElementException("El producto no existía en el carrito");
        }

        cartRepository.save(cart);
    }

    @Transactional
    public void eliminarCarrito(Long id, Long tokenUserId){
        if(!id.equals(tokenUserId)){
            log.warn("Acceso no autorizado: Usuario {} intentó modificar carrito de {}",
                tokenUserId, id);
            throw new AccessDeniedException("No tiene permiso para modificar este carrito");
        }
        log.info("Eliminando carrito de usuario con ID: {}",id);

        if(!cartRepository.existsByUserId(id)){
            throw new NoSuchElementException("Carrito no encontrado");
        }

        cartRepository.deleteByUserId(id);
    }
}

package cl.gestion.ventas.cart.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cl.gestion.ventas.cart.dto.CartItemDTO;
import cl.gestion.ventas.cart.dto.CartRequest;
import cl.gestion.ventas.cart.dto.CartResponse;
import cl.gestion.ventas.cart.model.Cart;
import cl.gestion.ventas.cart.model.CartItem;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart){
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getItems().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public CartItemDTO toDto(CartItem item){
        return CartItemDTO.builder()
            .productId(item.getProductId())
            .quantity(item.getQuantity())
            .build();
    }

    public Cart fromRequest(CartRequest request){
        Cart cart = Cart.builder()
                .userId(request.getUserId())
                .build();

        List<CartItem> items = request.getItems().stream()
                .map(Dto -> CartItem.builder()
                        .productId(Dto.getProductId())
                        .quantity(Dto.getQuantity())
                        .cart(cart)
                        .build())
                .collect(Collectors.toList());
        
        cart.setItems(items);
        return cart;
    }
}


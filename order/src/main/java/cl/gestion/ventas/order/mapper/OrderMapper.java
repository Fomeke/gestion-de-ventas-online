package cl.gestion.ventas.order.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cl.gestion.ventas.order.dto.CartItemDTO;
import cl.gestion.ventas.order.dto.CartResponse;
import cl.gestion.ventas.order.dto.InventoryRequest;
import cl.gestion.ventas.order.dto.OrderItemDTO;
import cl.gestion.ventas.order.dto.OrderRequest;
import cl.gestion.ventas.order.dto.OrderResponse;
import cl.gestion.ventas.order.dto.OrderResponseForShipping;
import cl.gestion.ventas.order.model.Order;
import cl.gestion.ventas.order.model.OrderItem;
import cl.gestion.ventas.order.model.OrderStatus;
import io.jsonwebtoken.lang.Collections;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order){
        return OrderResponse.builder()
            .id(order.getId())
            .userId(order.getUserId())
            .orderDate(order.getOrderDate())
            .status(order.getStatus())
            .paymentMethod(order.getPaymentMethod())
            .total(order.getTotal())
            .items(order.getItems().stream()
                    .map(item -> OrderItemDTO.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .build())
                    .collect(Collectors.toList()))
            .build();
    }

    public Order fromCartResponse(OrderRequest request, CartResponse cart){
        Order order = Order.builder()
                    .userId(request.getUserId())
                    .paymentMethod(request.getPaymentMethod())
                    .total(cart.getTotal())
                    .status(OrderStatus.PENDING)
                    .build();
        
        List<OrderItem> items = cart.getItems().stream()
                    .map(cartItem -> OrderItem.builder()
                                .productId(cartItem.getProductId())
                                .quantity(cartItem.getQuantity())
                                .orders(order)
                                .build())
                    .collect(Collectors.toList());
        order.setItems(items);
        return order;
    }

    public List<InventoryRequest> toInventoryRequestDecrement(List<CartItemDTO> cartItems){
        if (cartItems == null) return Collections.emptyList();
        return cartItems.stream().map(item -> InventoryRequest.builder()
                                            .productId(item.getProductId())
                                            .stock(-item.getQuantity())
                                            .build())
                                 .collect(Collectors.toList());
    }

    public List<InventoryRequest> toInventoryRequestRestore(List<OrderItem> cartItems){
        if (cartItems == null) return Collections.emptyList();
        return cartItems.stream().map(item -> InventoryRequest.builder()
                                            .productId(item.getProductId())
                                            .stock(item.getQuantity())
                                            .build())
                                 .collect(Collectors.toList());
    }

    public OrderResponseForShipping toResponseForShipping(Order order){
        return OrderResponseForShipping.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus())
                .build();
    }
}

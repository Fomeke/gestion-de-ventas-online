package cl.gestion.ventas.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.gestion.ventas.order.config.FeignConfig;
import cl.gestion.ventas.order.dto.CartResponse;

@FeignClient(name="cart-service",url="${services.cart.baseUrl}",
             configuration=FeignConfig.class)
public interface CartClient {

    @GetMapping("/{id}")
    CartResponse getCartByUserId(@PathVariable Long id);

    @DeleteMapping("/{id}")
    void cleanCartByUserId(@PathVariable Long id);

}

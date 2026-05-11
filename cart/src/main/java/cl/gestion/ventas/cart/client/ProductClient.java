package cl.gestion.ventas.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.gestion.ventas.cart.dto.ProductResponse;

@FeignClient(name="product-service",url="${services.product.baseUrl}",
             configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/{id}")
    ProductResponse getProductById(@PathVariable Long id);

    
}

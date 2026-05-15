package cl.gestion.ventas.shipping.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cl.gestion.ventas.shipping.config.FeignConfig;
import cl.gestion.ventas.shipping.dto.OrderResponseForShipping;
import cl.gestion.ventas.shipping.dto.OrderStatusUpdate;

@FeignClient(name="order-service",url="${services.order.baseUrl}",
             configuration=FeignConfig.class)
public interface OrderClient {

    @GetMapping("/ship/{id}")
    OrderResponseForShipping getOrderById(@PathVariable Long id);

    @PutMapping("/{id}")
    void updateState(@PathVariable Long id, @RequestBody OrderStatusUpdate request);
}

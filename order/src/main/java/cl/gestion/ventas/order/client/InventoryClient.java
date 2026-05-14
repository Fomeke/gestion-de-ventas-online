package cl.gestion.ventas.order.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cl.gestion.ventas.order.config.FeignConfig;
import cl.gestion.ventas.order.dto.InventoryRequest;

@FeignClient(name="inventory-service",url="${services.inventory.baseUrl}",
             configuration=FeignConfig.class)
public interface InventoryClient {

    @PutMapping("/bulk-update")
    void actualizarStock(@RequestBody List<InventoryRequest> items);


}

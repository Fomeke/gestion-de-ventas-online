package cl.gestion.ventas.inventory.mapper;

import org.springframework.stereotype.Component;

import cl.gestion.ventas.inventory.dto.InventoryRequest;
import cl.gestion.ventas.inventory.dto.InventoryResponse;
import cl.gestion.ventas.inventory.model.Inventory;

@Component
public class InventoryMapper {
    public InventoryResponse toResponse(Inventory inv){
        return InventoryResponse.builder()
                .id(inv.getId())
                .productId(inv.getProductId())
                .stock(inv.getStock())
                .build();
    }

    public Inventory fromRequest(InventoryRequest request){
        return Inventory.builder()
                .productId(request.getProductId())
                .stock(request.getStock())
                .build();
    }
}

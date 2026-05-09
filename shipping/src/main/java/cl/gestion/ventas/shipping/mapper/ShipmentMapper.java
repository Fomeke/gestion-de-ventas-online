package cl.gestion.ventas.shipping.mapper;

import org.springframework.stereotype.Component;

import cl.gestion.ventas.shipping.dto.ShipmentRequest;
import cl.gestion.ventas.shipping.dto.ShipmentResponse;
import cl.gestion.ventas.shipping.model.Shipment;

@Component
public class ShipmentMapper {
    public ShipmentResponse toResponse(Shipment shipment){
        return ShipmentResponse.builder()
                .id(shipment.getId())
                .orderId(shipment.getOrderId())
                .trackingNumber(shipment.getTrackingNumber())
                .carrier(shipment.getCarrier())
                .shippingAddress(shipment.getShippingAddress())
                .estimatedDeliveryDate(shipment.getEstimatedDeliveryDate())
                .build();
    }

    public Shipment fromRequest(ShipmentRequest request){
        return Shipment.builder()
                .orderId(request.getOrderId())
                .trackingNumber(request.getTrackingNumber())
                .carrier(request.getCarrier())
                .shippingAddress(request.getShippingAddress())
                .build();
                
    }

}

package cl.gestion.ventas.shipping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.shipping.dto.ShipmentRequest;
import cl.gestion.ventas.shipping.dto.ShipmentResponse;
import cl.gestion.ventas.shipping.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/shipments")
@Slf4j
public class ShippingController {
    @Autowired
    private ShipmentService shipmentService;

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getShipments(){
        log.info("GET /shipments");
        return ResponseEntity.ok(shipmentService.obtenerEnvios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getShipmentById(@PathVariable Long id){
        log.info("GET /shipments/{}",id);

        return ResponseEntity.ok(shipmentService.obtenerEnvioPorId(id));
    }

    @GetMapping("/{trackingNum}")
    public ResponseEntity<ShipmentResponse> getShipmentByTrktrackingNum(@PathVariable String trackingNum){
        log.info("GET /shipments/{}",trackingNum);
        return ResponseEntity.ok(shipmentService.obtenerEnvioPorNumero(trackingNum));
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody ShipmentRequest request){
        log.info("POST /shipments");
        ShipmentResponse created = shipmentService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{trackingNum}")
    public ResponseEntity<Void> deleteShipment(@PathVariable String trackingNum){
        log.info("DELETE /shipments/{}",trackingNum);
        shipmentService.eliminar(trackingNum);
        return ResponseEntity.noContent().build();
    }
}

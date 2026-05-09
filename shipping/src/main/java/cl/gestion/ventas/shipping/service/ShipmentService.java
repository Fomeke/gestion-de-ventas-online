package cl.gestion.ventas.shipping.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gestion.ventas.shipping.dto.ShipmentRequest;
import cl.gestion.ventas.shipping.dto.ShipmentResponse;
import cl.gestion.ventas.shipping.mapper.ShipmentMapper;
import cl.gestion.ventas.shipping.model.Shipment;
import cl.gestion.ventas.shipping.repository.ShipmentRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShipmentService {
    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentMapper shipmentMapper;

    public List<ShipmentResponse> obtenerEnvios(){
        log.info("Obteniendo todos los envíos");
        List<Shipment> ships = shipmentRepository.findAll();
        return ships.stream().map(shipmentMapper::toResponse).toList();
    }

    public ShipmentResponse obtenerEnvioPorId(Long id){
        log.info("Obteniendo envío con id: {}",id);
        Shipment ship = shipmentRepository.findById(id).get();
        return shipmentMapper.toResponse(ship);
    }

    public ShipmentResponse obtenerEnvioPorNumero(String number){
        log.info("Obteniendo envio con numero de rastreo: {}",number);
        Shipment ship = shipmentRepository.findByTrackingNumber(number).get();
        return shipmentMapper.toResponse(ship);
    }

    public ShipmentResponse crear(ShipmentRequest shipmentRequest){
        log.info("Creando nuevo envío");
        if(shipmentRepository.existsByTrackingNumber(shipmentRequest.getTrackingNumber())){
            throw new IllegalArgumentException("El envío ya existe");
        }
        Shipment ship = shipmentRepository.save(shipmentMapper.fromRequest(shipmentRequest));
        return shipmentMapper.toResponse(ship);
    }

    public void eliminar(String number){
        log.info("Elimiando envío con número de rastreo: {}",number);
        if(!shipmentRepository.existsByTrackingNumber(number)){
            throw new NoSuchElementException("Envío no encontrado");
        }
        shipmentRepository.deleteByTrackingNumber(number);
    }
}

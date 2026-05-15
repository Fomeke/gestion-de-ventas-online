package cl.gestion.ventas.shipping.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.gestion.ventas.shipping.client.OrderClient;
import cl.gestion.ventas.shipping.dto.OrderResponseForShipping;
import cl.gestion.ventas.shipping.dto.OrderStatusUpdate;
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

    @Autowired
    private OrderClient orderClient;

    @Transactional
    public List<ShipmentResponse> obtenerEnvios() {
        log.info("Obteniendo todos los envíos");
        List<Shipment> ships = shipmentRepository.findAll();
        return ships.stream().map(shipmentMapper::toResponse).toList();
    }

    @Transactional
    public ShipmentResponse obtenerEnvioPorId(Long id) {
        log.info("Obteniendo envío con id: {}", id);
        Shipment ship = shipmentRepository.findById(id).get();
        return shipmentMapper.toResponse(ship);
    }

    @Transactional
    public ShipmentResponse obtenerEnvioPorNumero(String number) {
        log.info("Obteniendo envio con numero de rastreo: {}", number);
        Shipment ship = shipmentRepository.findByTrackingNumber(number).get();
        return shipmentMapper.toResponse(ship);
    }

    @Transactional
    public ShipmentResponse crear(ShipmentRequest shipmentRequest) {
        log.info("Verificando que envío exista y esté pagado");

        OrderResponseForShipping order = orderClient.getOrderById(shipmentRequest.getOrderId());

        if (!order.getStatus().equals("PAID")) {
            throw new IllegalStateException("La orden debe estar pagada (PAID) para el envío");
        }

        log.info("Creando nuevo envío");
        if (shipmentRepository.existsByTrackingNumber(shipmentRequest.getTrackingNumber())) {
            throw new IllegalArgumentException("El envío ya existe");
        }

        orderClient.updateState(shipmentRequest.getOrderId(), OrderStatusUpdate.builder().newStatus("SHIPPED").build());

        Shipment ship = shipmentRepository.save(shipmentMapper.fromRequest(shipmentRequest));
        return shipmentMapper.toResponse(ship);
    }

    @Transactional
    public void eliminar(String number) {
        log.info("Elimiando envío con número de rastreo: {}", number);
        if (!shipmentRepository.existsByTrackingNumber(number)) {
            throw new NoSuchElementException("Envío no encontrado");
        }
        shipmentRepository.deleteByTrackingNumber(number);
    }

    @Transactional
    public ShipmentResponse actualizar(String trackingNumber, ShipmentRequest request) {
        Shipment existe = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new NoSuchElementException("Envío no encontrado"));

        if (!request.getTrackingNumber().equalsIgnoreCase(existe.getTrackingNumber())
                && shipmentRepository.existsByTrackingNumber(request.getTrackingNumber())) {
            throw new IllegalArgumentException("El número de rastreo ya existe");
        }

        if (!request.getOrderId().equals(existe.getOrderId())) {
            OrderResponseForShipping order = orderClient.getOrderById(request.getOrderId());
            if (!"PAID".equals(order.getStatus())) {
                throw new IllegalStateException("La orden debe estar pagada (PAID) para asociarla al envío");
            }
        }

        existe.setOrderId(request.getOrderId());
        existe.setTrackingNumber(request.getTrackingNumber());
        existe.setCarrier(request.getCarrier());
        existe.setShippingAddress(request.getShippingAddress());

        Shipment saved = shipmentRepository.save(existe);
        return shipmentMapper.toResponse(saved);
    }
}

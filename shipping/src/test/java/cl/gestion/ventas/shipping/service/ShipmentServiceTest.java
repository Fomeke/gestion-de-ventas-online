package cl.gestion.ventas.shipping.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.gestion.ventas.shipping.client.OrderClient;
import cl.gestion.ventas.shipping.dto.OrderResponseForShipping;
import cl.gestion.ventas.shipping.dto.ShipmentRequest;
import cl.gestion.ventas.shipping.dto.ShipmentResponse;
import cl.gestion.ventas.shipping.mapper.ShipmentMapper;
import cl.gestion.ventas.shipping.model.Shipment;
import cl.gestion.ventas.shipping.repository.ShipmentRepository;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentMapper shipmentMapper;

    @Mock
    private OrderClient orderClient;

    @InjectMocks
    private ShipmentService shipmentService;

    @Test
    public void testObtenerEnvios(){
        Shipment ship = new Shipment();
        ShipmentResponse response = new ShipmentResponse();

        when(shipmentRepository.findAll()).thenReturn(List.of(ship));
        when(shipmentMapper.toResponse(ship)).thenReturn(response);

        List<ShipmentResponse> resultado = shipmentService.obtenerEnvios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(shipmentRepository,times(1)).findAll();
    }

    @Test
    public void testObtenerEnvioPorNumero(){
        String num = "ABC432875";
        Shipment ship = new Shipment();
        ShipmentResponse response = new ShipmentResponse();

        when(shipmentRepository.findByTrackingNumber(num)).thenReturn(Optional.of(ship));
        when(shipmentMapper.toResponse(ship)).thenReturn(response);

        ShipmentResponse resultado = shipmentService.obtenerEnvioPorNumero(num);

        assertNotNull(resultado);
        verify(shipmentRepository,times(1)).findByTrackingNumber(num);
        verify(shipmentMapper,times(1)).toResponse(ship);
        
    }

    @Test
    public void testCrear(){
        ShipmentRequest request = new ShipmentRequest(67L,"ABC-123","BlueExpress","Calle falsa 123");
        Shipment ship = new Shipment();
        Shipment savedShip = new Shipment();
        ShipmentResponse response = new ShipmentResponse();
        OrderResponseForShipping order = OrderResponseForShipping.builder().status("PAID").build();

        when(orderClient.getOrderById(request.getOrderId())).thenReturn(order);
        when(shipmentRepository.existsByTrackingNumber(request.getTrackingNumber())).thenReturn(false);
        when(shipmentMapper.fromRequest(request)).thenReturn(ship);
        when(shipmentRepository.save(ship)).thenReturn(savedShip);
        when(shipmentMapper.toResponse(savedShip)).thenReturn(response);

        ShipmentResponse resultado = shipmentService.crear(request);

        assertNotNull(resultado);
        verify(orderClient,times(1)).getOrderById(request.getOrderId());
        verify(shipmentRepository,times(1)).existsByTrackingNumber(request.getTrackingNumber());
        verify(shipmentRepository,times(1)).save(ship);
    }

    @Test
    public void testEliminar(){
        String number = "ABD-123";
        when(shipmentRepository.existsByTrackingNumber(number)).thenReturn(true);
        doNothing().when(shipmentRepository).deleteByTrackingNumber(number);

        shipmentService.eliminar(number);
        verify(shipmentRepository,times(1)).deleteByTrackingNumber(number);
    }

    @Test
    public void testActualizar(){
        String trknum = "NICE-69";
        Long newOrderId = 10L;

        ShipmentRequest request = new ShipmentRequest(newOrderId,trknum,"FEDEX","San Beka");
        Shipment envio = Shipment.builder().trackingNumber(trknum).orderId(11L).build();
        OrderResponseForShipping order = OrderResponseForShipping.builder().status("PAID").build();
        Shipment envioGuardado = new Shipment();
        ShipmentResponse response = new ShipmentResponse();

        when(shipmentRepository.findByTrackingNumber(trknum)).thenReturn(Optional.of(envio));
        when(orderClient.getOrderById(newOrderId)).thenReturn(order);
        when(shipmentRepository.save(envio)).thenReturn(envioGuardado);
        when(shipmentMapper.toResponse(envioGuardado)).thenReturn(response);

        ShipmentResponse resultado = shipmentService.actualizar(trknum, request);

        assertNotNull(resultado);

        assertEquals(newOrderId, envio.getOrderId());
        assertEquals("FEDEX", envio.getCarrier());
        assertEquals("San Beka", envio.getShippingAddress());

        verify(shipmentRepository,times(1)).findByTrackingNumber(trknum);
        verify(orderClient,times(1)).getOrderById(newOrderId);
        verify(shipmentRepository,times(1)).save(envio);
    }

    @Test
    void testCrear_Error_OrdenNoPagada(){
        Long orderId = 123L;
        ShipmentRequest request = ShipmentRequest.builder().orderId(orderId).trackingNumber("ASS-666").build();

        OrderResponseForShipping order = new OrderResponseForShipping();
        order.setStatus("PENDING"); 

        when(orderClient.getOrderById(orderId)).thenReturn(order);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            shipmentService.crear(request);
        });

        assertEquals("La orden debe estar pagada (PAID) para el envío", exception.getMessage());

        verify(orderClient, times(1)).getOrderById(orderId);
        verify(shipmentRepository, never()).existsByTrackingNumber(anyString());
        verify(orderClient, never()).updateState(anyLong(), any());
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }

    @Test
    void testActualizar_TrkNumExiste(){
        String currentTracking = "TRK-OLD";
        String requestedNewTracking = "TRK-DUPLICATED";
        Long orderId = 55L;

        ShipmentRequest request = ShipmentRequest.builder().trackingNumber(requestedNewTracking).orderId(orderId).build();

        Shipment existingShipment = Shipment.builder().trackingNumber(currentTracking).orderId(orderId).build();

        when(shipmentRepository.findByTrackingNumber(currentTracking)).thenReturn(Optional.of(existingShipment));

        when(shipmentRepository.existsByTrackingNumber(requestedNewTracking)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shipmentService.actualizar(currentTracking, request);
        });

        assertEquals("El número de rastreo ya existe", exception.getMessage());

        verify(shipmentRepository, times(1)).findByTrackingNumber(currentTracking);
        verify(shipmentRepository, times(1)).existsByTrackingNumber(requestedNewTracking);
        
        verifyNoInteractions(orderClient);
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }
}

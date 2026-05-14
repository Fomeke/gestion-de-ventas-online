package cl.gestion.ventas.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.order.dto.OrderRequest;
import cl.gestion.ventas.order.dto.OrderResponse;
import cl.gestion.ventas.order.dto.OrderStatusUpdate;
import cl.gestion.ventas.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(){
        log.info("GET /orders");
        return ResponseEntity.ok(orderService.obtenerOrdenes());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId){
        log.info("GET /orders/{}",userId);
        return ResponseEntity.ok(orderService.obtenerOrdenesPorUserId(userId));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> add(@Valid @RequestBody OrderRequest request){
        log.info("POST /orders");
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.crear(request));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateState(@PathVariable Long orderId, @Valid @RequestBody OrderStatusUpdate request){
        log.info("PUT /orders/{}",orderId);
        return ResponseEntity.ok(orderService.editarEstado(orderId, request));
    }

}

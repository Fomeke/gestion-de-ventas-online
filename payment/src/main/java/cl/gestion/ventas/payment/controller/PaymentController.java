package cl.gestion.ventas.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.payment.dto.PaymentRequest;
import cl.gestion.ventas.payment.dto.PaymentResponse;
import cl.gestion.ventas.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getPagos() {
        log.info("GET api/v1/payment");
        return ResponseEntity.ok(service.obtenerPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPagoPorId(@PathVariable Long id) {
        log.info("GET api/v1/payment/{}", id);
        return ResponseEntity.ok(service.obtenerPagoPorId(id));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> addPago(@Valid @RequestBody PaymentRequest request,
            @RequestHeader("Authorization") String token) {
        log.info("POST api/v1/payment");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.procesarPago(request, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        log.info("DELETE api/v1/payment/{}", id);
        service.eliminarPago(id, token);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePago(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Authorization") String token) {
        log.info("PUT api/v1/payment/{}", id);
        return ResponseEntity.ok(service.actualizarPago(id, request, token));
    }
}

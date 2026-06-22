package cl.gestion.ventas.payment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.gestion.ventas.payment.dto.PaymentRequest;
import cl.gestion.ventas.payment.dto.PaymentResponse;
import cl.gestion.ventas.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApiResponses({
        @ApiResponse(responseCode = "401", description = "No autenticado (Falta token o es invalido)"),
        @ApiResponse(responseCode = "403", description = "No autorizado (No tienes permisos)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
@Slf4j
@RestController
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
@Tag(name = "Payment API", description = "Endpoints para la gestion y procesamiento de pagos")
public class PaymentController {

    private final PaymentService service;

    @Operation(summary = "Obtener todos los pagos", description = "Retorna el historial completo de pagos procesados.")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getPagos() {
        log.info("GET api/v1/payment");
        return ResponseEntity.ok(service.obtenerPagos());
    }

    @Operation(summary = "Buscar pago por ID", description = "Retorna los detalles exactos de una transaccion específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPagoPorId(@PathVariable Long id) {
        log.info("GET api/v1/payment/{}", id);
        return ResponseEntity.ok(service.obtenerPagoPorId(id));
    }

    @Operation(summary = "Procesar nuevo pago", description = "Procesa un pago validando con el microservicio order que la orden esté PENDING y los montos coincidan.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago procesado exitosamente y orden actualizada a PAID"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos, monto incorrecto o la orden no esta PENDING")
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> addPago(@Valid @RequestBody PaymentRequest request,
            @RequestHeader("Authorization") String token) {
        log.info("POST api/v1/payment");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.procesarPago(request, token));
    }

    @Operation(summary = "Eliminar pago", description = "Elimina un registro de pago. Falla si el pago ya tiene un estado SUCCESS para evitar borrar evidencia financiera.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado con exito (Sin contenido)"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar un pago exitoso"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        log.info("DELETE api/v1/payment/{}", id);
        service.eliminarPago(id, token);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar pago", description = "Modifica los datos de una transaccion existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePago(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Authorization") String token) {
        log.info("PUT api/v1/payment/{}", id);
        return ResponseEntity.ok(service.actualizarPago(id, request, token));
    }
}
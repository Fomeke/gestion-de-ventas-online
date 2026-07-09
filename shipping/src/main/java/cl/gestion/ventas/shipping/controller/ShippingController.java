package cl.gestion.ventas.shipping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.shipping.dto.ApiErrorResponse;
import cl.gestion.ventas.shipping.dto.ShipmentRequest;
import cl.gestion.ventas.shipping.dto.ShipmentResponse;
import cl.gestion.ventas.shipping.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/shipments")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Envíos", description = "Endpoints para la creación, seguimiento y gestión de envíos")
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Solicitud inválida - Errores de validación de campos, estado del flujo no permitido o argumento incorrecto", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(responseCode = "403", description = "No autorizado - Token JWT ausente, expirado o inválido", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(responseCode = "404", description = "Envío no encontrado - El ID o número de tracking no coinciden con ningún registro", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(responseCode = "500", description = "Error interno - Error inesperado en el servidor", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
})
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080", "${allowed.origin}"}, allowCredentials = "true")
public class ShippingController {
    private final ShipmentService shipmentService;

    @GetMapping
    @Operation(summary = "Obtener todos los envíos", description = "Retorna una lista completa de los registros de envíos del sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de envíos obtenida exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShipmentResponse.class)))
    public ResponseEntity<List<ShipmentResponse>> getShipments() {
        log.info("GET /shipments");
        return ResponseEntity.ok(shipmentService.obtenerEnvios());
    }

    @GetMapping("shipmentById/{id}")
    @Operation(summary = "Obtener envío por su ID interno", description = "Recupera la información detallada de un envío usando su ID en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Envío encontrado exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShipmentResponse.class)))
    public ResponseEntity<ShipmentResponse> getShipmentById(@Parameter(description = "Identificador único interno del envío", example = "1") @PathVariable Long id) {
        log.info("GET /shipments/{}", id);

        return ResponseEntity.ok(shipmentService.obtenerEnvioPorId(id));
    }

    @GetMapping("/{trackingNum}")
    @Operation(summary = "Buscar envío por número de seguimiento (Tracking Number)", description = "Recupera el estado completo de un envío a través de su código único de tracking.")
    @ApiResponse(responseCode = "200", description = "Envío encontrado exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShipmentResponse.class)))
    public ResponseEntity<ShipmentResponse> getShipmentByTrktrackingNum(@Parameter(description = "Identificador único de seguimiento", example = "SUS-666") @PathVariable String trackingNum) {
        log.info("GET /shipments/{}", trackingNum);
        return ResponseEntity.ok(shipmentService.obtenerEnvioPorNumero(trackingNum));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo envío", description = "Crea una orden de envío con sus detalles correspondientes, únicamente si la orden de compra tiene estado pagado (PAID)")
    @ApiResponse(responseCode = "201", description = "Orden de envío creada exitosamente", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShipmentResponse.class)))
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody ShipmentRequest request) {
        log.info("POST /shipments");
        ShipmentResponse created = shipmentService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{trackingNum}")
    @Operation(summary = "Eliminar un registro de envío", description = "Remueve el envío del sistema a partir de su número de tracking.")
    @ApiResponse(responseCode = "204", description = "Envío removido correctamente")
    public ResponseEntity<Void> deleteShipment(@Parameter(description = "Identificador único de seguimiento", example = "SUS-666") @PathVariable String trackingNum) {
        log.info("DELETE /shipments/{}", trackingNum);
        shipmentService.eliminar(trackingNum);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{trackingNum}")
    @Operation(summary = "Actualizar datos del envío", description = "Permite modificar datos del envío como dirección o n° de seguimiento")
    @ApiResponse(responseCode = "200", description = "Registro de envío actualizado de forma exitosa", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShipmentResponse.class)))
    public ResponseEntity<ShipmentResponse> updateShipment(
            @Parameter(description = "Identificador único de seguimiento", example = "SUS-666") 
            @PathVariable String trackingNum,
            @Valid @RequestBody ShipmentRequest request) {
        log.info("PUT /shipments/{}", trackingNum);
        return ResponseEntity.ok(shipmentService.actualizar(trackingNum, request));
    }
}

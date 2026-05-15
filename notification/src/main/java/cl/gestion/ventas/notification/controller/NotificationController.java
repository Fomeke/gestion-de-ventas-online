package cl.gestion.ventas.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.notification.dto.NotificationRequest;
import cl.gestion.ventas.notification.dto.NotificationResponse;
import cl.gestion.ventas.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {
        log.info("GET api/v1/notification");
        return ResponseEntity.ok(service.listNotificacion());
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> postNotification(@Valid @RequestBody NotificationRequest request,
            @RequestHeader("Authorization") String token) {
        log.info("POST api/v1/notification");

        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearNotificacion(request,token));
    }
}

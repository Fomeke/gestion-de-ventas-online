package cl.gestion.ventas.auth.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.auth.dto.UserRequest;
import cl.gestion.ventas.auth.dto.UserResponse;
import cl.gestion.ventas.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * La clase UserController es un controlador REST que maneja las solicitudes
 * HTTP de los usuarios, tiene métodos para crear, listar y eliminar usuarios
 * interactua con la capa de servicio mediante DTOs y crea logs correspondientes
 * a cada operación en users-service.log.
 */

@RestController
@RequestMapping("/v1/usuarios")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(){
        log.info("GET /usuarios");
        return ResponseEntity.ok(userService.obtenerUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        log.info("GET /usuarios/{}",id);

        return ResponseEntity.ok(userService.obtenerUsuarioPorId(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String username){
        log.info("GET /usuarios/{}",username);
        return ResponseEntity.ok(userService.obtenerUsurarioPorNombre(username));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request){
        log.info("POST /usuarios");
        UserResponse createdUser = userService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request){
        log.info("PUT /usuarios/{}",id);
        return ResponseEntity.ok(userService.modificarUsuario(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        log.info("DELETE /usuarios/{}",id);
        userService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

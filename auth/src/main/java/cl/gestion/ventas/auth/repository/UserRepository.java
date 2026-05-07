package cl.gestion.ventas.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.gestion.ventas.auth.model.User;

/**
 * La interfaz UserRepository hereda los métodos del repositorio de
 * Spring Data JPA para interactuar con la base de datos mediante la entidad
 * User, puede realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y personalizadas como busquedas por atributo o verificacion de si un objeto
 * existe. Esta interfaz sirve como intermediario entre la capa de Servicio y
 * la BD.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    
}

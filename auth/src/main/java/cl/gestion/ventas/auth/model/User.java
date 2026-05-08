package cl.gestion.ventas.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase Users sirve como entidad para implementar en la base de datos 
 * mediante Hibernate y JPA. Contiene atributos para la ID del 
 * usuario, nombre de usuario, nombre legal completo, contraseña para 
 * obtener un JWT, correo y telefono. Las anotaciones Entity y Table 
 * especifican que la clase sera implementada como una entidad en la BD,
 * mientras que Data, NoArgsConstructor, AllArgsConstructor y Builder sirven
 * para implementar implicitamente getters, setters y constructores, con
 * sintaxis especifica (Builder). Cada atributo tiene sus propias anotaciones,
 * como ID que mediante GenerationType.IDENTITY la genera como clave primaria 
 * automaticamente, mientras que otras anotaciones especifican si el atributo
 * tendra la constraint de unico, si podrá ser nulo, largo, y adaptacion del
 * nombre del atributo a snake case. 
 */

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, nullable = false, length = 80)
    private String correo;

    @Column(length = 20)
    private String phone;

}

package com.exe.ConjuntoResidencialArkania.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa los roles del sistema de gestión residencial.
 * Esta clase mapea la tabla 'roles' en la base de datos y define
 * los diferentes tipos de permisos y accesos que pueden tener los usuarios.
 * 
 * Los roles típicos incluyen: ADMINISTRADOR, PROPIETARIO, ARRENDATARIO, 
 * VIGILANTE, CONSERJE, etc.
 * 
 * Un rol puede ser asignado a múltiples usuarios (relación many-to-many).
 */
@Entity
@Table(name = "roles")
@Data // Lombok: genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Lombok: genera constructor sin parámetros
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros
public class RolEntity {

    /**
     * Identificador único del rol en la base de datos.
     * Se genera automáticamente usando IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long rolId;

    /**
     * Nombre único del rol en el sistema.
     * Ejemplos: ADMINISTRADOR, PROPIETARIO, RESIDENTE, VIGILANTE, VISITANTE.
     * Se almacena en mayúsculas para consistencia.
     */
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del rol debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[A-Z_]+$", message = "El nombre del rol debe estar en mayúsculas y usar solo letras y guiones bajos")
    private String nombre;

    /**
     * Descripción detallada del rol y sus responsabilidades.
     * Proporciona información clara sobre qué puede hacer un usuario con este rol.
     */
    @Column(name = "descripcion", length = 255)
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    /**
     * Indica si el rol está activo en el sistema.
     * Por defecto es true. Se puede desactivar para suspender su uso.
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Lista de permisos específicos asociados al rol.
     * Formato JSON: ["READ_USERS", "WRITE_USERS", "DELETE_APARTMENTS", etc.]
     * Permite granularidad en los permisos sin crear tablas adicionales.
     */
    @Column(name = "permisos", columnDefinition = "TEXT")
    private String permisos;

    /**
     * Fecha y hora de creación del rol.
     * Se establece automáticamente al crear el registro.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Fecha y hora de última actualización del rol.
     * Se actualiza automáticamente cada vez que se modifica el registro.
     */
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
}

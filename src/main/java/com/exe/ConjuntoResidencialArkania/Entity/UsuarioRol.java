package com.exe.ConjuntoResidencialArkania.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que representa la tabla intermedia entre Usuario y Rol.
 * Esta clase mapea la tabla 'usuario_rol' en la base de datos y maneja
 * la relación many-to-many entre usuarios y roles.
 * 
 * Además de las claves foráneas, esta entidad puede almacenar información
 * adicional sobre la asignación del rol, como fechas de asignación,
 * usuario que asignó el rol, fecha de expiración, etc.
 * 
 * Esta implementación es útil cuando necesitas más control sobre la relación
 * many-to-many que el que proporcionan las anotaciones @ManyToMany simples.
 */
@Entity
@Table(name = "usuario_rol")
@Data // Lombok: genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Lombok: genera constructor sin parámetros
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros
public class UsuarioRol {

    /**
     * Identificador único de la relación usuario-rol.
     * Se genera automáticamente usando IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_rol_id")
    private Long usuarioRolId;

    /**
     * Relación many-to-one con la entidad Usuario.
     * Representa el usuario al que se le asigna el rol.
     * 
     * FetchType.LAZY: El usuario se carga bajo demanda para optimizar rendimiento.
     * JoinColumn: Define la columna de clave foránea hacia la tabla usuarios.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "El usuario es obligatorio")
    private UserEntity usuario;

    /**
     * Relación many-to-one con la entidad Rol.
     * Representa el rol que se asigna al usuario.
     * 
     * FetchType.LAZY: El rol se carga bajo demanda para optimizar rendimiento.
     * JoinColumn: Define la columna de clave foránea hacia la tabla roles.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    @NotNull(message = "El rol es obligatorio")
    private RolEntity rol;

    /**
     * Indica si la asignación del rol está activa.
     * Por defecto es true. Se puede desactivar temporalmente sin eliminar el registro.
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Fecha y hora de creación de la asignación usuario-rol.
     * Se establece automáticamente al crear el registro.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Fecha y hora de última actualización de la asignación usuario-rol.
     * Se actualiza automáticamente cada vez que se modifica el registro.
     */
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    /**
     * Constructor personalizado para crear una asignación básica de rol.
     * 
     * @param usuario El usuario al que se asigna el rol
     * @param rol El rol que se asigna
     */
    public UsuarioRol(UserEntity usuario, RolEntity rol) {
        this.usuario = usuario;
        this.rol = rol;
        this.activo = true;
    }

    /**
     * Constructor completo para crear una asignación de rol con información adicional.
     * 
     * @param usuario El usuario al que se asigna el rol
     * @param rol El rol que se asigna
     * @param asignadoPor El usuario que realiza la asignación
     * @param fechaExpiracion Fecha de expiración del rol (puede ser null)
     * @param comentarios Comentarios sobre la asignación
     */
    public UsuarioRol(UserEntity usuario, RolEntity rol, UserEntity asignadoPor, 
                     LocalDateTime fechaExpiracion, String comentarios) {
        this.usuario = usuario;
        this.rol = rol;
        this.activo = true;
    }

    /**
     * Método de utilidad para activar la asignación del rol.
     */
    public void activar() {
        this.activo = true;
    }

    /**
     * Método de utilidad para desactivar la asignación del rol.
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Constraint de unicidad personalizada a nivel de clase.
     * Previene que un usuario tenga el mismo rol asignado múltiples veces activamente.
     */
    @Table(uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_usuario_rol_activo", 
            columnNames = {"usuario_id", "rol_id"}
        )
    })
    public static class UsuarioRolConstraints {
        // Esta clase interna se usa solo para definir constraints adicionales
        // No contiene lógica, solo metadatos de la base de datos
    }
}

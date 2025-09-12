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
 * Entidad que representa un usuario del sistema de gestión residencial.
 * Esta clase mapea la tabla 'usuarios' en la base de datos y contiene
 * toda la información personal y de autenticación de un usuario.
 * 
 * Un usuario puede tener múltiples roles (relación many-to-many) y
 * puede estar asociado a uno o más apartamentos como propietario o residente.
 */
@Entity
@Table(name = "usuarios")
@Data // Lombok: genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Lombok: genera constructor sin parámetros
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros
public class UserEntity {

    /**
     * Identificador único del usuario en la base de datos.
     * Se genera automáticamente usando IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    /**
     * Tipo de documento de identidad (CC, CE, TI, etc.).
     * Campo requerido para identificar el tipo de documento del usuario.
     */
    @Column(name = "tipo_documento", nullable = false, length = 5)
    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "^(CC|CE|TI|PP|NIT)$", message = "Tipo de documento debe ser: CC, CE, TI, PP o NIT")
    private String tipoDocumento;

    /**
     * Número de documento de identidad del usuario.
     * Debe ser único en el sistema y es requerido.
     * Se usa como identificador principal del usuario.
     */
    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(min = 6, max = 20, message = "El número de documento debe tener entre 6 y 20 caracteres")
    private String numeroDocumento;


    /**
     * Nombres completos del usuario.
     * Campo requerido para identificación personal.
     */
    @Column(name = "nombres", nullable = false, length = 50)
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 50, message = "Los nombres deben tener entre 2 y 50 caracteres")
    private String nombres;

    /**
     * Apellidos completos del usuario.
     * Campo requerido para identificación personal.
     */
    @Column(name = "apellidos", nullable = false, length = 50)
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 50, message = "Los apellidos deben tener entre 2 y 50 caracteres")
    private String apellidos;

    /**
     * Correo electrónico del usuario.
     * Debe ser único en el sistema y se usa para autenticación y notificaciones.
     */
    @Column(name = "email", nullable = false, unique = true, length = 150)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;

    /**
     * Número de teléfono del usuario.
     * Campo opcional para contacto directo.
     */
    @Column(name = "telefono", length = 15)
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "El teléfono solo puede contener números, +, -, espacios y paréntesis")
    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    private String telefono;

    /**
     * Contraseña encriptada del usuario.
     * Se almacena usando hash para seguridad.
     */
    @Column(name = "password", nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    /**
     * Indica si la cuenta del usuario está activa.
     * Por defecto es true. Se puede desactivar para suspender el acceso.
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Fecha y hora de creación del usuario.
     * Se establece automáticamente al crear el registro.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Fecha y hora de última actualización del usuario.
     * Se actualiza automáticamente cada vez que se modifica el registro.
     */
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
}

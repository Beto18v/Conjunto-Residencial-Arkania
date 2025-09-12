package com.exe.ConjuntoResidencialArkania.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO (Data Transfer Object) para la transferencia de datos de Rol.
 * 
 * Esta clase se utiliza para:
 * - Transferir información de roles entre frontend y backend
 * - Validar datos de entrada al crear o modificar roles
 * - Estructurar respuestas de la API de manera controlada
 * - Evitar exponer directamente la entidad RolEntity
 * 
 * Incluye validaciones específicas para asegurar la integridad
 * de los datos de roles en el sistema.
 */
@Data // Lombok: genera getters, setters, toString, equals y hashCode automáticamente
@NoArgsConstructor // Lombok: genera constructor sin parámetros para deserialización JSON
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros
public class RolDTO {

    /**
     * Identificador único del rol.
     * Se incluye para operaciones de actualización y referencia.
     * Es null cuando se crea un nuevo rol.
     */
    private Long rolId;

    /**
     * Nombre único del rol en el sistema.
     * Debe estar en mayúsculas y usar solo letras y guiones bajos.
     * Ejemplos: ADMINISTRADOR, PROPIETARIO, RESIDENTE, VIGILANTE, VISITANTE.
     */
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del rol debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[A-Z_]+$", 
             message = "El nombre del rol debe estar en mayúsculas y usar solo letras y guiones bajos")
    private String nombre;

    /**
     * Descripción detallada del rol y sus responsabilidades.
     * Proporciona información clara sobre las funciones del rol.
     */
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    /**
     * Estado de activación del rol.
     * true = rol activo y disponible para asignación
     * false = rol inactivo, no se puede asignar a usuarios
     */
    private Boolean activo;

    /**
     * Lista de permisos específicos del rol.
     * Representa los permisos granulares que tiene el rol.
     * Ejemplos: ["READ_USERS", "WRITE_USERS", "DELETE_APARTMENTS"]
     */
    private List<String> permisos;

    /**
     * Número de usuarios que tienen asignado este rol.
     * Campo calculado, útil para estadísticas y validaciones.
     * No se incluye en las operaciones de creación/actualización.
     */
    private Integer numeroUsuarios;

    /**
     * Fecha y hora de creación del rol.
     * Se establece automáticamente al crear el registro.
     */
    private LocalDateTime fechaCreacion;

    /**
     * Fecha y hora de última actualización del rol.
     * Se actualiza automáticamente cada vez que se modifica el registro.
     */
    private LocalDateTime fechaActualizacion;
}

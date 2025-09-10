package com.exe.ConjuntoResidencialArkania.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * Constructor para crear un DTO con datos básicos del rol.
     * Útil para respuestas que no requieren todos los campos.
     * 
     * @param rolId ID del rol
     * @param nombre Nombre del rol
     * @param descripcion Descripción del rol
     * @param activo Estado del rol
     */
    public RolDTO(Long rolId, String nombre, String descripcion, Boolean activo) {
        this.rolId = rolId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    /**
     * Constructor para crear un rol básico sin ID (para creación).
     * 
     * @param nombre Nombre del rol
     * @param descripcion Descripción del rol
     */
    public RolDTO(String nombre, String descripcion) {
        this.nombre = nombre != null ? nombre.toUpperCase() : null;
        this.descripcion = descripcion;
        this.activo = true;
    }

    /**
     * Método para establecer el nombre del rol.
     * Asegura que siempre esté en mayúsculas.
     * 
     * @param nombre Nombre del rol
     */
    public void setNombre(String nombre) {
        this.nombre = nombre != null ? nombre.toUpperCase() : null;
    }

    /**
     * Método de utilidad para verificar si el rol tiene un permiso específico.
     * 
     * @param permiso El permiso a verificar
     * @return true si el rol tiene el permiso, false en caso contrario
     */
    public boolean tienePermiso(String permiso) {
        return permisos != null && permisos.contains(permiso);
    }
}

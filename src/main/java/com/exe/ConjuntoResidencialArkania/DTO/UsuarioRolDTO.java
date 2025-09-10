package com.exe.ConjuntoResidencialArkania.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la transferencia de datos de la relación Usuario-Rol.
 * 
 * Esta clase se utiliza para:
 * - Transferir información sobre las asignaciones de roles a usuarios
 * - Manejar la relación many-to-many entre usuarios y roles con información adicional
 * - Validar datos de entrada para asignación/desasignación de roles
 * - Estructurar respuestas de la API para consultas de roles por usuario
 * 
 * Representa la tabla intermedia usuario_rol con información adicional
 * sobre la asignación del rol, incluyendo fechas y estado de activación.
 */
@Data // Lombok: genera getters, setters, toString, equals y hashCode automáticamente
@NoArgsConstructor // Lombok: genera constructor sin parámetros para deserialización JSON
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros
public class UsuarioRolDTO {

    /**
     * Identificador único de la relación usuario-rol.
     * Se incluye para operaciones de actualización y referencia.
     * Es null cuando se crea una nueva asignación.
     */
    private Long usuarioRolId;

    /**
     * Identificador del usuario al que se asigna el rol.
     * Campo requerido para establecer la relación.
     */
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    /**
     * Número de documento del usuario.
     * Se incluye para facilitar la identificación sin necesidad 
     * de cargar la entidad completa del usuario.
     */
    private String numeroDocumentoUsuario;

    /**
     * Nombre completo del usuario.
     * Campo calculado para mostrar información legible
     * sin exponer todos los datos del usuario.
     */
    private String nombreCompletoUsuario;

    /**
     * Identificador del rol que se asigna al usuario.
     * Campo requerido para establecer la relación.
     */
    @NotNull(message = "El ID del rol es obligatorio")
    private Long rolId;

    /**
     * Nombre del rol asignado.
     * Se incluye para facilitar la identificación sin necesidad
     * de cargar la entidad completa del rol.
     */
    private String nombreRol;

    /**
     * Estado de activación de la asignación del rol.
     * true = asignación activa, el usuario tiene el rol efectivamente
     * false = asignación desactivada temporalmente
     */
    private Boolean activo;

    /**
     * Constructor para crear una asignación básica de rol.
     * Útil para operaciones simples de asignación.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     */
    public UsuarioRolDTO(Long usuarioId, Long rolId) {
        this.usuarioId = usuarioId;
        this.rolId = rolId;
        this.activo = true;
    }

    /**
     * Constructor para crear una asignación con información básica de usuario y rol.
     * 
     * @param usuarioId ID del usuario
     * @param numeroDocumentoUsuario Documento del usuario
     * @param nombreCompletoUsuario Nombre completo del usuario
     * @param rolId ID del rol
     * @param nombreRol Nombre del rol
     * @param activo Estado de la asignación
     */
    public UsuarioRolDTO(Long usuarioId, String numeroDocumentoUsuario, String nombreCompletoUsuario,
                        Long rolId, String nombreRol, Boolean activo) {
        this.usuarioId = usuarioId;
        this.numeroDocumentoUsuario = numeroDocumentoUsuario;
        this.nombreCompletoUsuario = nombreCompletoUsuario;
        this.rolId = rolId;
        this.nombreRol = nombreRol;
        this.activo = activo;
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

}

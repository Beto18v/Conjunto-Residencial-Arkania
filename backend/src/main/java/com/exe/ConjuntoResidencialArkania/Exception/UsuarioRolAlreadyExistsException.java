package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando se intenta crear una asignación usuario-rol duplicada.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Asignación de un rol ya asignado al mismo usuario
 * - Creación de asignaciones duplicadas en el sistema
 * - Violación de restricciones de unicidad en asignaciones
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot.
 */
public class UsuarioRolAlreadyExistsException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public UsuarioRolAlreadyExistsException() {
        super("La asignación usuario-rol ya existe");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public UsuarioRolAlreadyExistsException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public UsuarioRolAlreadyExistsException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para asignación duplicada por IDs.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return Instancia de UsuarioRolAlreadyExistsException con mensaje específico
     */
    public static UsuarioRolAlreadyExistsException porUsuarioYRol(Long usuarioId, Long rolId) {
        return new UsuarioRolAlreadyExistsException("El usuario " + usuarioId + " ya tiene asignado el rol " + rolId);
    }

    /**
     * Constructor de conveniencia para asignación duplicada por documento y nombre.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @param nombreRol Nombre del rol
     * @return Instancia de UsuarioRolAlreadyExistsException con mensaje específico
     */
    public static UsuarioRolAlreadyExistsException porDocumentoYRol(String numeroDocumento, String nombreRol) {
        return new UsuarioRolAlreadyExistsException("El usuario con documento " + numeroDocumento + " ya tiene asignado el rol '" + nombreRol + "'");
    }

    /**
     * Constructor de conveniencia para asignación activa existente.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @param nombreRol Nombre del rol
     * @return Instancia de UsuarioRolAlreadyExistsException con mensaje específico
     */
    public static UsuarioRolAlreadyExistsException asignacionActiva(String numeroDocumento, String nombreRol) {
        return new UsuarioRolAlreadyExistsException("El usuario con documento " + numeroDocumento + " ya tiene una asignación activa del rol '" + nombreRol + "'");
    }

    /**
     * Constructor de conveniencia para reactivación.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @param nombreRol Nombre del rol
     * @return Instancia de UsuarioRolAlreadyExistsException con mensaje específico
     */
    public static UsuarioRolAlreadyExistsException paraReactivacion(String numeroDocumento, String nombreRol) {
        return new UsuarioRolAlreadyExistsException("Ya existe una asignación del rol '" + nombreRol + "' para el usuario con documento " + numeroDocumento + ". Considere reactivar la asignación existente");
    }
}

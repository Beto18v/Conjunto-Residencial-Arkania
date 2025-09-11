package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando no se encuentra una asignación usuario-rol en el sistema.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Búsqueda de asignación por ID inexistente
 * - Búsqueda de asignación por usuario y rol inexistente
 * - Operaciones de actualización sobre asignaciones inexistentes
 * - Intentos de desasignación de roles no asignados
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot.
 */
public class UsuarioRolNotFoundException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public UsuarioRolNotFoundException() {
        super("Asignación usuario-rol no encontrada");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public UsuarioRolNotFoundException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public UsuarioRolNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para búsqueda por ID.
     * 
     * @param usuarioRolId ID de la asignación que no fue encontrada
     * @return Instancia de UsuarioRolNotFoundException con mensaje específico
     */
    public static UsuarioRolNotFoundException porId(Long usuarioRolId) {
        return new UsuarioRolNotFoundException("Asignación usuario-rol con ID " + usuarioRolId + " no encontrada");
    }

    /**
     * Constructor de conveniencia para búsqueda por usuario y rol.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return Instancia de UsuarioRolNotFoundException con mensaje específico
     */
    public static UsuarioRolNotFoundException porUsuarioYRol(Long usuarioId, Long rolId) {
        return new UsuarioRolNotFoundException("No se encontró asignación entre el usuario " + usuarioId + " y el rol " + rolId);
    }

    /**
     * Constructor de conveniencia para búsqueda por documento y nombre de rol.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @param nombreRol Nombre del rol
     * @return Instancia de UsuarioRolNotFoundException con mensaje específico
     */
    public static UsuarioRolNotFoundException porDocumentoYRol(String numeroDocumento, String nombreRol) {
        return new UsuarioRolNotFoundException("El usuario con documento " + numeroDocumento + " no tiene asignado el rol '" + nombreRol + "'");
    }

    /**
     * Constructor de conveniencia para asignación activa no encontrada.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return Instancia de UsuarioRolNotFoundException con mensaje específico
     */
    public static UsuarioRolNotFoundException asignacionActiva(Long usuarioId, Long rolId) {
        return new UsuarioRolNotFoundException("No se encontró asignación activa entre el usuario " + usuarioId + " y el rol " + rolId);
    }

    /**
     * Constructor de conveniencia para operaciones de desasignación.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @param nombreRol Nombre del rol
     * @return Instancia de UsuarioRolNotFoundException con mensaje específico
     */
    public static UsuarioRolNotFoundException paraDesasignacion(String numeroDocumento, String nombreRol) {
        return new UsuarioRolNotFoundException("No se puede desasignar el rol '" + nombreRol + "' del usuario con documento " + numeroDocumento + ". La asignación no existe o ya está inactiva");
    }
}

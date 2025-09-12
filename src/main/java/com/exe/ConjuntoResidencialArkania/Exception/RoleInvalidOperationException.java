package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando una operación sobre rol es inválida.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Intentar eliminar un rol que tiene usuarios asignados
 * - Intentar desactivar un rol crítico del sistema
 * - Operaciones sobre roles con restricciones de negocio
 * - Violación de políticas de gestión de roles
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot.
 */
public class RoleInvalidOperationException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public RoleInvalidOperationException() {
        super("Operación inválida sobre el rol");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public RoleInvalidOperationException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public RoleInvalidOperationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para eliminación con usuarios asignados.
     * 
     * @param nombreRol Nombre del rol que no se puede eliminar
     * @param numeroUsuarios Número de usuarios que tienen el rol asignado
     * @return Instancia de RoleInvalidOperationException con mensaje específico
     */
    public static RoleInvalidOperationException noSePuedeEliminar(String nombreRol, int numeroUsuarios) {
        return new RoleInvalidOperationException("No se puede eliminar el rol '" + nombreRol + "'. Tiene " + numeroUsuarios + " usuario(s) asignado(s)");
    }

    /**
     * Constructor de conveniencia para desactivación de rol crítico.
     * 
     * @param nombreRol Nombre del rol crítico
     * @return Instancia de RoleInvalidOperationException con mensaje específico
     */
    public static RoleInvalidOperationException rolCritico(String nombreRol) {
        return new RoleInvalidOperationException("No se puede desactivar el rol '" + nombreRol + "' porque es crítico para el sistema");
    }

    /**
     * Constructor de conveniencia para operación sobre rol inactivo.
     * 
     * @param nombreRol Nombre del rol inactivo
     * @return Instancia de RoleInvalidOperationException con mensaje específico
     */
    public static RoleInvalidOperationException rolInactivo(String nombreRol) {
        return new RoleInvalidOperationException("No se puede realizar la operación. El rol '" + nombreRol + "' está inactivo");
    }

    /**
     * Constructor de conveniencia para violación de permisos.
     * 
     * @param operacion Operación que se intentó realizar
     * @param nombreRol Nombre del rol
     * @return Instancia de RoleInvalidOperationException con mensaje específico
     */
    public static RoleInvalidOperationException permisoInsuficiente(String operacion, String nombreRol) {
        return new RoleInvalidOperationException("No se puede " + operacion + " el rol '" + nombreRol + "'. Permisos insuficientes");
    }
}

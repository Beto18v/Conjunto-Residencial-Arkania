package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando se intenta crear un rol con datos duplicados.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Creación de rol con nombre ya existente
 * - Actualización que genera nombres duplicados
 * - Violación de restricciones de unicidad del rol
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot.
 */
public class RoleAlreadyExistsException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public RoleAlreadyExistsException() {
        super("El rol ya existe en el sistema");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public RoleAlreadyExistsException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public RoleAlreadyExistsException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para duplicado por nombre.
     * 
     * @param nombre Nombre del rol duplicado
     * @return Instancia de RoleAlreadyExistsException con mensaje específico
     */
    public static RoleAlreadyExistsException porNombre(String nombre) {
        return new RoleAlreadyExistsException("Ya existe un rol con el nombre '" + nombre + "'");
    }

    /**
     * Constructor de conveniencia para operación de actualización.
     * 
     * @param rolId ID del rol que se intenta actualizar
     * @param nombre Nuevo nombre que ya existe
     * @return Instancia de RoleAlreadyExistsException con mensaje específico
     */
    public static RoleAlreadyExistsException enActualizacion(Long rolId, String nombre) {
        return new RoleAlreadyExistsException("No se puede actualizar el rol con ID " + rolId + ". Ya existe otro rol con el nombre '" + nombre + "'");
    }
}

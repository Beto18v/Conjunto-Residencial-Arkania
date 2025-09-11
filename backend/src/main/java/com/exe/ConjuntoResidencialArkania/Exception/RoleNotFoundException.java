package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un rol en el sistema.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Búsqueda de rol por ID inexistente
 * - Búsqueda de rol por nombre inexistente
 * - Operaciones de actualización sobre roles inexistentes
 * - Asignación de roles que no existen
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot.
 */
public class RoleNotFoundException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public RoleNotFoundException() {
        super("Rol no encontrado");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public RoleNotFoundException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public RoleNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para búsqueda por ID.
     * 
     * @param rolId ID del rol que no fue encontrado
     * @return Instancia de RoleNotFoundException con mensaje específico
     */
    public static RoleNotFoundException porId(Long rolId) {
        return new RoleNotFoundException("Rol con ID " + rolId + " no encontrado");
    }

    /**
     * Constructor de conveniencia para búsqueda por nombre.
     * 
     * @param nombre Nombre del rol que no fue encontrado
     * @return Instancia de RoleNotFoundException con mensaje específico
     */
    public static RoleNotFoundException porNombre(String nombre) {
        return new RoleNotFoundException("Rol con nombre '" + nombre + "' no encontrado");
    }

    /**
     * Constructor de conveniencia para rol inactivo.
     * 
     * @param nombre Nombre del rol inactivo
     * @return Instancia de RoleNotFoundException con mensaje específico
     */
    public static RoleNotFoundException rolInactivo(String nombre) {
        return new RoleNotFoundException("El rol '" + nombre + "' existe pero está inactivo");
    }

    /**
     * Constructor de conveniencia para operaciones de asignación.
     * 
     * @param rolId ID del rol para asignación
     * @return Instancia de RoleNotFoundException con mensaje específico
     */
    public static RoleNotFoundException paraAsignacion(Long rolId) {
        return new RoleNotFoundException("No se puede asignar el rol. Rol con ID " + rolId + " no encontrado o inactivo");
    }
}

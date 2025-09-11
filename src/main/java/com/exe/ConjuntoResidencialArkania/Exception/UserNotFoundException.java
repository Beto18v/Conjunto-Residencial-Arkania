package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un usuario en el sistema.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Búsqueda de usuario por ID inexistente
 * - Búsqueda de usuario por número de documento inexistente
 * - Búsqueda de usuario por email inexistente
 * - Operaciones de actualización sobre usuarios inexistentes
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public UserNotFoundException() {
        super("Usuario no encontrado");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public UserNotFoundException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public UserNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para búsqueda por ID.
     * 
     * @param usuarioId ID del usuario que no fue encontrado
     * @return Instancia de UserNotFoundException con mensaje específico
     */
    public static UserNotFoundException porId(Long usuarioId) {
        return new UserNotFoundException("Usuario con ID " + usuarioId + " no encontrado");
    }

    /**
     * Constructor de conveniencia para búsqueda por número de documento.
     * 
     * @param numeroDocumento Número de documento del usuario que no fue encontrado
     * @return Instancia de UserNotFoundException con mensaje específico
     */
    public static UserNotFoundException porNumeroDocumento(String numeroDocumento) {
        return new UserNotFoundException("Usuario con número de documento " + numeroDocumento + " no encontrado");
    }

    /**
     * Constructor de conveniencia para búsqueda por email.
     * 
     * @param email Email del usuario que no fue encontrado
     * @return Instancia de UserNotFoundException con mensaje específico
     */
    public static UserNotFoundException porEmail(String email) {
        return new UserNotFoundException("Usuario con email " + email + " no encontrado");
    }

    /**
     * Constructor de conveniencia para búsqueda por tipo y número de documento.
     * 
     * @param tipoDocumento Tipo de documento del usuario
     * @param numeroDocumento Número de documento del usuario
     * @return Instancia de UserNotFoundException con mensaje específico
     */
    public static UserNotFoundException porTipoYNumeroDocumento(String tipoDocumento, String numeroDocumento) {
        return new UserNotFoundException("Usuario con " + tipoDocumento + " " + numeroDocumento + " no encontrado");
    }
}

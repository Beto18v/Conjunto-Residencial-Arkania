package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando se intenta crear un usuario con datos duplicados.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Creación de usuario con número de documento ya existente
 * - Creación de usuario con email ya registrado
 * - Actualización que genera duplicados de datos únicos
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public UserAlreadyExistsException() {
        super("El usuario ya existe en el sistema");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public UserAlreadyExistsException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public UserAlreadyExistsException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para duplicado por número de documento.
     * 
     * @param numeroDocumento Número de documento duplicado
     * @return Instancia de UserAlreadyExistsException con mensaje específico
     */
    public static UserAlreadyExistsException porNumeroDocumento(String numeroDocumento) {
        return new UserAlreadyExistsException("Ya existe un usuario con el número de documento " + numeroDocumento);
    }

    /**
     * Constructor de conveniencia para duplicado por email.
     * 
     * @param email Email duplicado
     * @return Instancia de UserAlreadyExistsException con mensaje específico
     */
    public static UserAlreadyExistsException porEmail(String email) {
        return new UserAlreadyExistsException("Ya existe un usuario con el email " + email);
    }

    /**
     * Constructor de conveniencia para duplicado por tipo y número de documento.
     * 
     * @param tipoDocumento Tipo de documento duplicado
     * @param numeroDocumento Número de documento duplicado
     * @return Instancia de UserAlreadyExistsException con mensaje específico
     */
    public static UserAlreadyExistsException porTipoYNumeroDocumento(String tipoDocumento, String numeroDocumento) {
        return new UserAlreadyExistsException("Ya existe un usuario con " + tipoDocumento + " " + numeroDocumento);
    }
}

package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando una operación sobre usuario es inválida.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Intentar activar/desactivar un usuario en estado no válido
 * - Operaciones sobre usuarios con datos inconsistentes
 * - Validaciones de negocio específicas de usuarios
 * - Operaciones que violan reglas de negocio del sistema
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot.
 */
public class UserInvalidOperationException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public UserInvalidOperationException() {
        super("Operación inválida sobre el usuario");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public UserInvalidOperationException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public UserInvalidOperationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para operación sobre usuario inactivo.
     * 
     * @param numeroDocumento Número de documento del usuario inactivo
     * @return Instancia de UserInvalidOperationException con mensaje específico
     */
    public static UserInvalidOperationException usuarioInactivo(String numeroDocumento) {
        return new UserInvalidOperationException("No se puede realizar la operación. El usuario con documento " + numeroDocumento + " está inactivo");
    }

    /**
     * Constructor de conveniencia para operación de desactivación inválida.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @param razon Razón por la cual no se puede desactivar
     * @return Instancia de UserInvalidOperationException con mensaje específico
     */
    public static UserInvalidOperationException noSePuedeDesactivar(String numeroDocumento, String razon) {
        return new UserInvalidOperationException("No se puede desactivar el usuario con documento " + numeroDocumento + ": " + razon);
    }

    /**
     * Constructor de conveniencia para datos inconsistentes.
     * 
     * @param campo Campo con datos inconsistentes
     * @return Instancia de UserInvalidOperationException con mensaje específico
     */
    public static UserInvalidOperationException datosInconsistentes(String campo) {
        return new UserInvalidOperationException("Datos inconsistentes en el campo: " + campo);
    }
}

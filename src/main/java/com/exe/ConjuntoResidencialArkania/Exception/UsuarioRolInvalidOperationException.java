package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando una operación sobre asignación usuario-rol es inválida.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Intentar asignar roles mutuamente excluyentes
 * - Asignar roles sin cumplir prerequisitos
 * - Violación de límites de usuarios por rol
 * - Operaciones sobre asignaciones inactivas
 * - Violación de políticas de asignación temporal
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot.
 */
public class UsuarioRolInvalidOperationException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public UsuarioRolInvalidOperationException() {
        super("Operación inválida sobre la asignación usuario-rol");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public UsuarioRolInvalidOperationException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public UsuarioRolInvalidOperationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para roles mutuamente excluyentes.
     * 
     * @param rolActual Nombre del rol que ya tiene el usuario
     * @param rolNuevo Nombre del rol que se intenta asignar
     * @return Instancia de UsuarioRolInvalidOperationException con mensaje específico
     */
    public static UsuarioRolInvalidOperationException rolesExcluyentes(String rolActual, String rolNuevo) {
        return new UsuarioRolInvalidOperationException("No se puede asignar el rol '" + rolNuevo + "' porque el usuario ya tiene el rol '" + rolActual + "' y son mutuamente excluyentes");
    }

    /**
     * Constructor de conveniencia para prerequisitos no cumplidos.
     * 
     * @param rolDeseado Nombre del rol que se intenta asignar
     * @param rolRequerido Nombre del rol prerequisito
     * @return Instancia de UsuarioRolInvalidOperationException con mensaje específico
     */
    public static UsuarioRolInvalidOperationException prerequisitoNoCumplido(String rolDeseado, String rolRequerido) {
        return new UsuarioRolInvalidOperationException("No se puede asignar el rol '" + rolDeseado + "' porque el usuario debe tener primero el rol '" + rolRequerido + "'");
    }

    /**
     * Constructor de conveniencia para límite de usuarios excedido.
     * 
     * @param nombreRol Nombre del rol
     * @param limiteMaximo Límite máximo de usuarios para el rol
     * @return Instancia de UsuarioRolInvalidOperationException con mensaje específico
     */
    public static UsuarioRolInvalidOperationException limiteUsuariosExcedido(String nombreRol, int limiteMaximo) {
        return new UsuarioRolInvalidOperationException("No se puede asignar el rol '" + nombreRol + "' porque ya alcanzó el límite máximo de " + limiteMaximo + " usuario(s)");
    }

    /**
     * Constructor de conveniencia para operación sobre asignación inactiva.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @param nombreRol Nombre del rol
     * @return Instancia de UsuarioRolInvalidOperationException con mensaje específico
     */
    public static UsuarioRolInvalidOperationException asignacionInactiva(String numeroDocumento, String nombreRol) {
        return new UsuarioRolInvalidOperationException("No se puede realizar la operación. La asignación del rol '" + nombreRol + "' para el usuario con documento " + numeroDocumento + " está inactiva");
    }

    /**
     * Constructor de conveniencia para asignación expirada.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @param nombreRol Nombre del rol
     * @return Instancia de UsuarioRolInvalidOperationException con mensaje específico
     */
    public static UsuarioRolInvalidOperationException asignacionExpirada(String numeroDocumento, String nombreRol) {
        return new UsuarioRolInvalidOperationException("La asignación del rol '" + nombreRol + "' para el usuario con documento " + numeroDocumento + " ha expirado y requiere renovación");
    }

    /**
     * Constructor de conveniencia para usuario inactivo.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @return Instancia de UsuarioRolInvalidOperationException con mensaje específico
     */
    public static UsuarioRolInvalidOperationException usuarioInactivo(String numeroDocumento) {
        return new UsuarioRolInvalidOperationException("No se puede asignar roles al usuario con documento " + numeroDocumento + " porque está inactivo");
    }

    /**
     * Constructor de conveniencia para rol inactivo.
     * 
     * @param nombreRol Nombre del rol inactivo
     * @return Instancia de UsuarioRolInvalidOperationException con mensaje específico
     */
    public static UsuarioRolInvalidOperationException rolInactivo(String nombreRol) {
        return new UsuarioRolInvalidOperationException("No se puede asignar el rol '" + nombreRol + "' porque está inactivo en el sistema");
    }
}

package com.exe.ConjuntoResidencialArkania.Exception;

/**
 * Excepción personalizada que se lanza cuando no se encuentra una correspondencia en el sistema.
 * 
 * Esta excepción se utiliza en casos específicos como:
 * - Búsqueda de correspondencia por ID inexistente
 * - Búsqueda de correspondencia por destinatario inexistente
 * - Operaciones de actualización sobre correspondencias inexistentes
 * - Operaciones de eliminación sobre correspondencias inexistentes
 * 
 * Extiende RuntimeException para ser una excepción no verificada,
 * siguiendo las mejores prácticas de Spring Boot y permitiendo un manejo
 * más limpio de errores en la capa de servicio y controlador.
 */
public class CorrespondenciaNotFoundException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public CorrespondenciaNotFoundException() {
        super("Correspondencia no encontrada");
    }

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param mensaje Mensaje descriptivo del error
     */
    public CorrespondenciaNotFoundException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raíz.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción que causó este error
     */
    public CorrespondenciaNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor de conveniencia para búsqueda por ID.
     * 
     * @param idCorrespondencia ID de la correspondencia que no fue encontrada
     * @return Instancia de CorrespondenciaNotFoundException con mensaje específico
     */
    public static CorrespondenciaNotFoundException porId(Long idCorrespondencia) {
        return new CorrespondenciaNotFoundException("Correspondencia con ID " + idCorrespondencia + " no encontrada");
    }

    /**
     * Constructor de conveniencia para búsqueda por destinatario.
     * 
     * @param destinatarioId ID del destinatario para el cual no se encontraron correspondencias
     * @return Instancia de CorrespondenciaNotFoundException con mensaje específico
     */
    public static CorrespondenciaNotFoundException porDestinatario(Long destinatarioId) {
        return new CorrespondenciaNotFoundException("No se encontraron correspondencias para el destinatario con ID " + destinatarioId);
    }

    /**
     * Constructor de conveniencia para búsqueda por estado.
     * 
     * @param estado Estado de la correspondencia para el cual no se encontraron resultados
     * @return Instancia de CorrespondenciaNotFoundException con mensaje específico
     */
    public static CorrespondenciaNotFoundException porEstado(String estado) {
        return new CorrespondenciaNotFoundException("No se encontraron correspondencias con estado " + estado);
    }

    /**
     * Constructor de conveniencia para búsqueda por tipo.
     * 
     * @param tipo Tipo de correspondencia para el cual no se encontraron resultados
     * @return Instancia de CorrespondenciaNotFoundException con mensaje específico
     */
    public static CorrespondenciaNotFoundException porTipo(String tipo) {
        return new CorrespondenciaNotFoundException("No se encontraron correspondencias del tipo " + tipo);
    }
}
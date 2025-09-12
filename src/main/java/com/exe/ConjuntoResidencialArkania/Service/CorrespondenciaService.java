package com.exe.ConjuntoResidencialArkania.Service;

import com.exe.ConjuntoResidencialArkania.DTO.CorrespondenciaDTO;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Estado;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Tipo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los servicios relacionados con la gestión de correspondencia
 * en el conjunto residencial. Proporciona métodos para crear, leer, actualizar y eliminar
 * correspondencias, así como consultas específicas por destinatario, estado, tipo, etc.
 * Esta interfaz sigue el patrón de diseño de separación de responsabilidades,
 * permitiendo que las implementaciones concretas manejen la lógica de negocio.
 * Ahora trabaja directamente con DTOs para evitar conversiones en el controlador.
 */
public interface CorrespondenciaService {

    /**
     * Crea una nueva correspondencia en el sistema.
     * @param correspondencia El DTO de correspondencia a crear.
     * @return El DTO de la correspondencia creada con su ID asignado.
     */
    CorrespondenciaDTO crearCorrespondencia(CorrespondenciaDTO correspondencia);

    /**
     * Lista todas las correspondencias registradas en el sistema.
     * @return Una lista de todos los DTOs de correspondencias.
     */
    List<CorrespondenciaDTO> listarTodas();

    /**
     * Busca una correspondencia por su ID único.
     * @param id El ID de la correspondencia a buscar.
     * @return Un Optional que contiene el DTO de la correspondencia si se encuentra, vacío en caso contrario.
     */
    Optional<CorrespondenciaDTO> buscarPorId(Long id);

    /**
     * Actualiza una correspondencia existente en el sistema.
     * @param correspondencia El DTO de correspondencia con los datos actualizados.
     * @return El DTO de la correspondencia actualizada.
     */
    CorrespondenciaDTO actualizarCorrespondencia(CorrespondenciaDTO correspondencia);

    /**
     * Elimina una correspondencia del sistema por su ID.
     * @param id El ID de la correspondencia a eliminar.
     */
    void eliminarCorrespondencia(Long id);

    /**
     * Busca todas las correspondencias dirigidas a un destinatario específico.
     * @param destinatario El ID del usuario destinatario.
     * @return Una lista de DTOs de correspondencias para el destinatario.
     */
    List<CorrespondenciaDTO> buscarPorDestinatario(Long destinatario);

    /**
     * Busca correspondencias por su estado (Pendiente, Entregada, etc.).
     * @param estado El estado de la correspondencia.
     * @return Una lista de DTOs de correspondencias con el estado especificado.
     */
    List<CorrespondenciaDTO> buscarPorEstado(Estado estado);

    /**
     * Busca correspondencias por su tipo (Paquete, Documento, Otro).
     * @param tipo El tipo de correspondencia.
     * @return Una lista de DTOs de correspondencias del tipo especificado.
     */
    List<CorrespondenciaDTO> buscarPorTipo(Tipo tipo);

    /**
     * Busca correspondencias registradas dentro de un rango de fechas.
     * @param inicio La fecha de inicio del rango.
     * @param fin La fecha de fin del rango.
     * @return Una lista de DTOs de correspondencias en el rango de fechas.
     */
    List<CorrespondenciaDTO> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Busca correspondencias retiradas por un usuario específico.
     * @param idUsuario El ID del usuario que retiró la correspondencia.
     * @return Una lista de DTOs de correspondencias retiradas por el usuario.
     */
    List<CorrespondenciaDTO> buscarPorRetiradoPor(Long idUsuario);

}

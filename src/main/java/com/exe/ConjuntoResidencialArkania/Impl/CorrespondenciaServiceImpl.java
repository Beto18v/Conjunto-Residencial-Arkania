package com.exe.ConjuntoResidencialArkania.Impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

import com.exe.ConjuntoResidencialArkania.DTO.CorrespondenciaDTO;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Estado;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Tipo;
import com.exe.ConjuntoResidencialArkania.Repository.CorrespondenciaRepository;
import com.exe.ConjuntoResidencialArkania.Service.CorrespondenciaService;
import com.exe.ConjuntoResidencialArkania.Exception.CorrespondenciaNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación concreta del servicio de correspondencia.
 * Esta clase maneja toda la lógica de negocio relacionada con la gestión de correspondencias,
 * incluyendo operaciones CRUD y consultas específicas. Utiliza el repositorio para interactuar
 * con la base de datos y sigue las mejores prácticas de inyección de dependencias y separación
 * de responsabilidades. Ahora trabaja directamente con DTOs, manejando la conversión interna.
 * Los métodos están documentados para facilitar el mantenimiento y comprensión del código.
 */
@Service
public class CorrespondenciaServiceImpl implements CorrespondenciaService {

    /**
     * Repositorio de correspondencia inyectado para acceder a la capa de datos.
     * Se utiliza para realizar operaciones de persistencia y consultas en la base de datos.
     */
    private final CorrespondenciaRepository correspondenciaRepository;

    /**
     * Constructor que inyecta el repositorio de correspondencia.
     * Spring Boot maneja automáticamente la inyección de dependencias gracias a la anotación @Autowired.
     * @param correspondenciaRepository El repositorio necesario para las operaciones de datos.
     */
    @Autowired
    public CorrespondenciaServiceImpl(CorrespondenciaRepository correspondenciaRepository) {
        this.correspondenciaRepository = correspondenciaRepository;
    }

    /**
     * Crea una nueva correspondencia en el sistema.
     * Antes de guardar, se pueden agregar validaciones adicionales o lógica de negocio,
     * como verificar permisos o establecer fechas por defecto.
     * @param dto El DTO de correspondencia a crear.
     * @return El DTO de la correspondencia creada con su ID asignado por la base de datos.
     */
    @Override
    public CorrespondenciaDTO crearCorrespondencia(CorrespondenciaDTO dto) {
        // Convertir DTO a Entity
        CorrespondenciaEntity entity = convertirDtoAEntity(dto);
        // Aquí se puede agregar lógica adicional, como validar datos o auditar la creación
        CorrespondenciaEntity savedEntity = correspondenciaRepository.save(entity);
        // Convertir Entity a DTO
        return convertirEntityADto(savedEntity);
    }

    /**
     * Lista todas las correspondencias registradas en el sistema.
     * Este método es útil para vistas administrativas o reportes generales.
     * @return Una lista completa de todos los DTOs de correspondencias en la base de datos.
     */
    @Override
    public List<CorrespondenciaDTO> listarTodas() {
        List<CorrespondenciaEntity> entities = correspondenciaRepository.findAll();
        return entities.stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    /**
     * Busca una correspondencia específica por su ID único.
     * Utiliza el método findById de JpaRepository, que devuelve un Optional para manejar
     * casos donde la correspondencia no existe.
     * @param id El ID único de la correspondencia a buscar.
     * @return Un Optional que contiene el DTO de la correspondencia si se encuentra, vacío en caso contrario.
     */
    @Override
    public Optional<CorrespondenciaDTO> buscarPorId(Long id) {
        Optional<CorrespondenciaEntity> entityOpt = correspondenciaRepository.findById(id);
        return entityOpt.map(this::convertirEntityADto);
    }

    /**
     * Actualiza una correspondencia existente en el sistema.
     * JpaRepository maneja automáticamente si es una inserción o actualización basada en el ID.
     * Se recomienda validar que la correspondencia exista antes de actualizar.
     * @param dto El DTO de correspondencia con los datos actualizados.
     * @return El DTO de la correspondencia actualizada después de ser guardada en la base de datos.
     */
    @Override
    public CorrespondenciaDTO actualizarCorrespondencia(CorrespondenciaDTO dto) {
        // Validación opcional: verificar si existe antes de actualizar
        if (!correspondenciaRepository.existsById(dto.getIdCorrespondencia())) {
            throw CorrespondenciaNotFoundException.porId(dto.getIdCorrespondencia());
        }
        // Convertir DTO a Entity
        CorrespondenciaEntity entity = convertirDtoAEntity(dto);
        CorrespondenciaEntity updatedEntity = correspondenciaRepository.save(entity);
        // Convertir Entity a DTO
        return convertirEntityADto(updatedEntity);
    }

    /**
     * Elimina una correspondencia del sistema por su ID.
     * Antes de eliminar, se pueden agregar verificaciones, como comprobar si hay dependencias
     * o si el usuario tiene permisos para eliminar.
     * @param id El ID de la correspondencia a eliminar.
     */
    @Override
    public void eliminarCorrespondencia(Long id) {
        // Verificación opcional: comprobar existencia antes de eliminar
        if (!correspondenciaRepository.existsById(id)) {
            throw CorrespondenciaNotFoundException.porId(id);
        }
        correspondenciaRepository.deleteById(id);
    }

    /**
     * Busca todas las correspondencias dirigidas a un destinatario específico.
     * Utiliza el método personalizado del repositorio para filtrar por destinatario.
     * @param destinatario El ID del usuario destinatario.
     * @return Una lista de DTOs de correspondencias asociadas al destinatario.
     */
    @Override
    public List<CorrespondenciaDTO> buscarPorDestinatario(Long destinatario) {
        List<CorrespondenciaEntity> entities = correspondenciaRepository.findByDestinatario_usuarioId(destinatario);
        return entities.stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    /**
     * Busca correspondencias por su estado actual (Pendiente, Entregada, etc.).
     * Permite filtrar correspondencias según su estado para reportes o gestión.
     * @param estado El estado de la correspondencia a buscar.
     * @return Una lista de DTOs de correspondencias con el estado especificado.
     */
    @Override
    public List<CorrespondenciaDTO> buscarPorEstado(Estado estado) {
        List<CorrespondenciaEntity> entities = correspondenciaRepository.findByEstado(estado);
        return entities.stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    /**
     * Busca correspondencias por su tipo (Paquete, Documento, Otro).
     * Facilita la categorización y búsqueda de correspondencias por tipo.
     * @param tipo El tipo de correspondencia a buscar.
     * @return Una lista de DTOs de correspondencias del tipo especificado.
     */
    @Override
    public List<CorrespondenciaDTO> buscarPorTipo(Tipo tipo) {
        List<CorrespondenciaEntity> entities = correspondenciaRepository.findByTipo(tipo);
        return entities.stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    /**
     * Busca correspondencias registradas dentro de un rango de fechas.
     * Utiliza el campo fechaRecepcion para filtrar. Es útil para reportes históricos.
     * @param inicio La fecha de inicio del rango (inclusive).
     * @param fin La fecha de fin del rango (inclusive).
     * @return Una lista de DTOs de correspondencias en el rango de fechas especificado.
     */
    @Override
    public List<CorrespondenciaDTO> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        List<CorrespondenciaEntity> entities = correspondenciaRepository.findByFechaRecepcionBetween(inicio, fin);
        return entities.stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    /**
     * Busca correspondencias retiradas por un usuario específico.
     * Permite rastrear qué correspondencias ha retirado un usuario determinado.
     * @param idUsuario El ID del usuario que retiró la correspondencia.
     * @return Una lista de DTOs de correspondencias retiradas por el usuario.
     */
    @Override
    public List<CorrespondenciaDTO> buscarPorRetiradoPor(Long idUsuario) {
        List<CorrespondenciaEntity> entities = correspondenciaRepository.findByRetiradoPor_usuarioId(idUsuario);
        return entities.stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // ========================================
    // MÉTODOS AUXILIARES DE CONVERSIÓN
    // ========================================

    /**
     * Convierte un DTO en una entidad CorrespondenciaEntity.
     * @param dto El DTO a convertir
     * @return La entidad correspondiente
     */
    private CorrespondenciaEntity convertirDtoAEntity(CorrespondenciaDTO dto) {
        CorrespondenciaEntity entity = new CorrespondenciaEntity();
        entity.setIdCorrespondencia(dto.getIdCorrespondencia());
        // Aquí se mapearían los campos relacionados con usuario/apartamento si se dispone de los servicios
        entity.setTipo(dto.getTipo() != null ? Tipo.valueOf(dto.getTipo()) : null);
        entity.setFechaRecepcion(dto.getFechaRecepcion());
        entity.setFechaEntrega(dto.getFechaEntrega());
        entity.setEstado(dto.getEstado() != null ? Estado.valueOf(dto.getEstado()) : null);
        entity.setObservaciones(dto.getObservaciones());
        entity.setCrearCorrespondencia(dto.getCrearCorrespondencia());
        entity.setActualizarCorrespondencia(dto.getActualizarCorrespondencia());
        return entity;
    }

    /**
     * Convierte una entidad CorrespondenciaEntity en un DTO.
     * @param entity La entidad a convertir
     * @return El DTO correspondiente
     */
    private CorrespondenciaDTO convertirEntityADto(CorrespondenciaEntity entity) {
        CorrespondenciaDTO dto = new CorrespondenciaDTO();
        dto.setIdCorrespondencia(entity.getIdCorrespondencia());
        // Aquí se mapearían los campos relacionados con usuario/apartamento si se dispone de los servicios
        dto.setTipo(entity.getTipo() != null ? entity.getTipo().toString() : null);
        dto.setFechaRecepcion(entity.getFechaRecepcion());
        dto.setFechaEntrega(entity.getFechaEntrega());
        dto.setEstado(entity.getEstado() != null ? entity.getEstado().toString() : null);
        dto.setObservaciones(entity.getObservaciones());
        dto.setCrearCorrespondencia(entity.getCrearCorrespondencia());
        dto.setActualizarCorrespondencia(entity.getActualizarCorrespondencia());
        return dto;
    }

}

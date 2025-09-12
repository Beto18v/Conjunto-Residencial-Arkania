package com.exe.ConjuntoResidencialArkania.Controller;

import com.exe.ConjuntoResidencialArkania.DTO.CorrespondenciaDTO;
import com.exe.ConjuntoResidencialArkania.Service.CorrespondenciaService;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Estado;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Tipo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de correspondencias en el sistema de conjunto residencial.
 * 
 * Este controlador expone endpoints para:
 * - CRUD completo de correspondencias
 * - Búsquedas específicas por destinatario, estado, tipo, rango de fechas y retirado por
 * - Operaciones de actualización y eliminación
 * 
 * Todos los endpoints siguen las convenciones REST y manejan
 * validaciones automáticas a través de las anotaciones del DTO.
 * 
 * Base URL: /api/correspondencias
 */
@RestController
@RequestMapping("/api/correspondencias")
@CrossOrigin(origins = "*")
public class CorrespondenciaController {

    @Autowired
    private CorrespondenciaService correspondenciaService;

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    /**
     * Crea una nueva correspondencia en el sistema.
     * @param dto Datos de la correspondencia a crear (validados automáticamente)
     * @return ResponseEntity con la correspondencia creada y código 201 CREATED
     */
    @PostMapping
    public ResponseEntity<CorrespondenciaDTO> crearCorrespondencia(@Valid @RequestBody CorrespondenciaDTO dto) {
        try {
            CorrespondenciaDTO responseDto = correspondenciaService.crearCorrespondencia(dto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Obtiene todas las correspondencias del sistema.
     * @return ResponseEntity con la lista de todas las correspondencias
     */
    @GetMapping
    public ResponseEntity<List<CorrespondenciaDTO>> obtenerTodasLasCorrespondencias() {
        List<CorrespondenciaDTO> dtos = correspondenciaService.listarTodas();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene una correspondencia por su ID.
     * @param id ID de la correspondencia a buscar
     * @return ResponseEntity con la correspondencia encontrada o 404 NOT FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<CorrespondenciaDTO> obtenerCorrespondenciaPorId(@PathVariable Long id) {
        Optional<CorrespondenciaDTO> dtoOpt = correspondenciaService.buscarPorId(id);
        return dtoOpt.map(dto -> ResponseEntity.ok(dto))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Actualiza completamente una correspondencia existente.
     * @param id ID de la correspondencia a actualizar
     * @param dto Nuevos datos de la correspondencia (validados automáticamente)
     * @return ResponseEntity con la correspondencia actualizada o error correspondiente
     */
    @PutMapping("/{id}")
    public ResponseEntity<CorrespondenciaDTO> actualizarCorrespondencia(
            @PathVariable Long id,
            @Valid @RequestBody CorrespondenciaDTO dto) {
        try {
            dto.setIdCorrespondencia(id);
            CorrespondenciaDTO responseDto = correspondenciaService.actualizarCorrespondencia(dto);
            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina lógicamente una correspondencia (marca como eliminada).
     * @param id ID de la correspondencia a eliminar
     * @return ResponseEntity con código 204 NO CONTENT o 404 NOT FOUND
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCorrespondencia(@PathVariable Long id) {
        try {
            correspondenciaService.eliminarCorrespondencia(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================
    // BÚSQUEDAS ESPECÍFICAS
    // ========================================

    /**
     * Busca correspondencias por destinatario.
     * @param destinatarioId ID del usuario destinatario
     * @return ResponseEntity con la lista de correspondencias encontradas
     */
    @GetMapping("/destinatario/{destinatarioId}")
    public ResponseEntity<List<CorrespondenciaDTO>> obtenerPorDestinatario(@PathVariable Long destinatarioId) {
        List<CorrespondenciaDTO> dtos = correspondenciaService.buscarPorDestinatario(destinatarioId);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Busca correspondencias por estado.
     * @param estado Estado de la correspondencia (PENDIENTE, ENTREGADA)
     * @return ResponseEntity con la lista de correspondencias encontradas
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CorrespondenciaDTO>> obtenerPorEstado(@PathVariable Estado estado) {
        List<CorrespondenciaDTO> dtos = correspondenciaService.buscarPorEstado(estado);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Busca correspondencias por tipo.
     * @param tipo Tipo de correspondencia (PAQUETE, DOCUMENTO, OTRO)
     * @return ResponseEntity con la lista de correspondencias encontradas
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CorrespondenciaDTO>> obtenerPorTipo(@PathVariable Tipo tipo) {
        List<CorrespondenciaDTO> dtos = correspondenciaService.buscarPorTipo(tipo);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Busca correspondencias en un rango de fechas.
     * @param inicio Fecha de inicio del rango
     * @param fin Fecha de fin del rango
     * @return ResponseEntity con la lista de correspondencias encontradas
     */
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<CorrespondenciaDTO>> obtenerPorRangoFechas(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {
        List<CorrespondenciaDTO> dtos = correspondenciaService.buscarPorRangoFechas(inicio, fin);
        return ResponseEntity.ok(dtos);
    }    /**
     * Busca correspondencias retiradas por un usuario específico.
     * @param usuarioId ID del usuario que retiró la correspondencia
     * @return ResponseEntity con la lista de correspondencias encontradas
     */
    @GetMapping("/retirado-por/{usuarioId}")
    public ResponseEntity<List<CorrespondenciaDTO>> obtenerPorRetiradoPor(@PathVariable Long usuarioId) {
        List<CorrespondenciaDTO> dtos = correspondenciaService.buscarPorRetiradoPor(usuarioId);
        return ResponseEntity.ok(dtos);
    }

}
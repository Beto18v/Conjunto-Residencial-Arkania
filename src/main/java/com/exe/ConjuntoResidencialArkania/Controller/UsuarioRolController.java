package com.exe.ConjuntoResidencialArkania.Controller;

import com.exe.ConjuntoResidencialArkania.DTO.UsuarioRolDTO;
import com.exe.ConjuntoResidencialArkania.Service.UsuarioRolService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de asignaciones Usuario-Rol en el sistema de conjunto residencial.
 * 
 * Este controlador expone endpoints para:
 * - CRUD completo de asignaciones usuario-rol
 * - Asignación y desasignación de roles a usuarios
 * - Consultas por usuario específico
 * - Consultas por rol específico
 * - Operaciones de activación/desactivación de asignaciones
 * - Consultas estadísticas y de auditoría
 * 
 * Todos los endpoints siguen las convenciones REST y manejan
 * validaciones automáticas a través de las anotaciones del DTO.
 * 
 * Base URL: /api/usuario-roles
 */
@RestController
@RequestMapping("/api/usuario-roles")
@CrossOrigin(origins = "*")
public class UsuarioRolController {

    @Autowired
    private UsuarioRolService usuarioRolService;

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    /**
     * Crea una nueva asignación de rol a usuario.
     * 
     * @param usuarioRolDTO Datos de la asignación a crear (validados automáticamente)
     * @return ResponseEntity con la asignación creada y código 201 CREATED
     */
    @PostMapping
    public ResponseEntity<UsuarioRolDTO> crearAsignacion(@Valid @RequestBody UsuarioRolDTO usuarioRolDTO) {
        try {
            UsuarioRolDTO asignacionCreada = usuarioRolService.crearAsignacion(usuarioRolDTO);
            return new ResponseEntity<>(asignacionCreada, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Asigna un rol específico a un usuario específico.
     * Método de conveniencia para asignaciones simples.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return ResponseEntity con la asignación creada
     */
    @PostMapping("/asignar/{usuarioId}/{rolId}")
    public ResponseEntity<UsuarioRolDTO> asignarRolAUsuario(
            @PathVariable Long usuarioId, 
            @PathVariable Long rolId) {
        try {
            UsuarioRolDTO asignacionCreada = usuarioRolService.asignarRolAUsuario(usuarioId, rolId);
            return new ResponseEntity<>(asignacionCreada, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene una asignación usuario-rol por su ID.
     * 
     * @param usuarioRolId ID de la asignación a buscar
     * @return ResponseEntity con la asignación encontrada o 404 NOT FOUND
     */
    @GetMapping("/{usuarioRolId}")
    public ResponseEntity<UsuarioRolDTO> obtenerAsignacionPorId(@PathVariable Long usuarioRolId) {
        Optional<UsuarioRolDTO> asignacion = usuarioRolService.obtenerAsignacionPorId(usuarioRolId);
        return asignacion.map(a -> ResponseEntity.ok(a))
                        .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las asignaciones usuario-rol del sistema.
     * 
     * @return ResponseEntity con la lista de todas las asignaciones
     */
    @GetMapping
    public ResponseEntity<List<UsuarioRolDTO>> obtenerTodasLasAsignaciones() {
        List<UsuarioRolDTO> asignaciones = usuarioRolService.obtenerTodasLasAsignaciones();
        return ResponseEntity.ok(asignaciones);
    }

    /**
     * Actualiza una asignación usuario-rol existente.
     * 
     * @param usuarioRolId ID de la asignación a actualizar
     * @param usuarioRolDTO Nuevos datos de la asignación
     * @return ResponseEntity con la asignación actualizada o error correspondiente
     */
    @PutMapping("/{usuarioRolId}")
    public ResponseEntity<UsuarioRolDTO> actualizarAsignacion(
            @PathVariable Long usuarioRolId, 
            @Valid @RequestBody UsuarioRolDTO usuarioRolDTO) {
        try {
            UsuarioRolDTO asignacionActualizada = usuarioRolService.actualizarAsignacion(usuarioRolId, usuarioRolDTO);
            return ResponseEntity.ok(asignacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina físicamente una asignación usuario-rol.
     * 
     * @param usuarioRolId ID de la asignación a eliminar
     * @return ResponseEntity con código 204 NO CONTENT o 404 NOT FOUND
     */
    @DeleteMapping("/{usuarioRolId}")
    public ResponseEntity<Void> eliminarAsignacion(@PathVariable Long usuarioRolId) {
        try {
            usuarioRolService.eliminarAsignacion(usuarioRolId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========================================
    // BÚSQUEDAS ESPECÍFICAS
    // ========================================

    /**
     * Obtiene una asignación específica usuario-rol.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return ResponseEntity con la asignación encontrada o 404 NOT FOUND
     */
    @GetMapping("/usuario/{usuarioId}/rol/{rolId}")
    public ResponseEntity<UsuarioRolDTO> obtenerAsignacionPorUsuarioYRol(
            @PathVariable Long usuarioId, 
            @PathVariable Long rolId) {
        Optional<UsuarioRolDTO> asignacion = usuarioRolService.obtenerAsignacionPorUsuarioYRol(usuarioId, rolId);
        return asignacion.map(a -> ResponseEntity.ok(a))
                        .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene una asignación activa específica usuario-rol.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return ResponseEntity con la asignación activa encontrada o 404 NOT FOUND
     */
    @GetMapping("/usuario/{usuarioId}/rol/{rolId}/activa")
    public ResponseEntity<UsuarioRolDTO> obtenerAsignacionActivaPorUsuarioYRol(
            @PathVariable Long usuarioId, 
            @PathVariable Long rolId) {
        Optional<UsuarioRolDTO> asignacion = usuarioRolService.obtenerAsignacionActivaPorUsuarioYRol(usuarioId, rolId);
        return asignacion.map(a -> ResponseEntity.ok(a))
                        .orElse(ResponseEntity.notFound().build());
    }

    // ========================================
    // CONSULTAS POR USUARIO
    // ========================================

    /**
     * Obtiene todas las asignaciones de roles para un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @return ResponseEntity con la lista de asignaciones del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<UsuarioRolDTO>> obtenerAsignacionesPorUsuario(@PathVariable Long usuarioId) {
        List<UsuarioRolDTO> asignaciones = usuarioRolService.obtenerAsignacionesPorUsuario(usuarioId);
        return ResponseEntity.ok(asignaciones);
    }

    /**
     * Obtiene todas las asignaciones activas de roles para un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @return ResponseEntity con la lista de asignaciones activas del usuario
     */
    @GetMapping("/usuario/{usuarioId}/activas")
    public ResponseEntity<List<UsuarioRolDTO>> obtenerAsignacionesActivasPorUsuario(@PathVariable Long usuarioId) {
        List<UsuarioRolDTO> asignaciones = usuarioRolService.obtenerAsignacionesActivasPorUsuario(usuarioId);
        return ResponseEntity.ok(asignaciones);
    }

    // ========================================
    // CONSULTAS POR ROL
    // ========================================

    /**
     * Obtiene todas las asignaciones de un rol específico.
     * 
     * @param rolId ID del rol
     * @return ResponseEntity con la lista de asignaciones del rol
     */
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<UsuarioRolDTO>> obtenerAsignacionesPorRol(@PathVariable Long rolId) {
        List<UsuarioRolDTO> asignaciones = usuarioRolService.obtenerAsignacionesPorRol(rolId);
        return ResponseEntity.ok(asignaciones);
    }

    /**
     * Obtiene todas las asignaciones activas de un rol específico.
     * 
     * @param rolId ID del rol
     * @return ResponseEntity con la lista de asignaciones activas del rol
     */
    @GetMapping("/rol/{rolId}/activas")
    public ResponseEntity<List<UsuarioRolDTO>> obtenerAsignacionesActivasPorRol(@PathVariable Long rolId) {
        List<UsuarioRolDTO> asignaciones = usuarioRolService.obtenerAsignacionesActivasPorRol(rolId);
        return ResponseEntity.ok(asignaciones);
    }

    // ========================================
    // OPERACIONES DE ACTIVACIÓN/DESACTIVACIÓN
    // ========================================

    /**
     * Activa una asignación usuario-rol específica.
     * 
     * @param usuarioRolId ID de la asignación a activar
     * @return ResponseEntity con código 200 OK o 404 NOT FOUND
     */
    @PutMapping("/{usuarioRolId}/activar")
    public ResponseEntity<Void> activarAsignacion(@PathVariable Long usuarioRolId) {
        try {
            usuarioRolService.activarAsignacion(usuarioRolId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Desactiva una asignación usuario-rol específica.
     * 
     * @param usuarioRolId ID de la asignación a desactivar
     * @return ResponseEntity con código 200 OK o 404 NOT FOUND
     */
    @PutMapping("/{usuarioRolId}/desactivar")
    public ResponseEntity<Void> desactivarAsignacion(@PathVariable Long usuarioRolId) {
        try {
            usuarioRolService.desactivarAsignacion(usuarioRolId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Activa la asignación de un rol específico a un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return ResponseEntity con código 200 OK o 404 NOT FOUND
     */
    @PutMapping("/usuario/{usuarioId}/rol/{rolId}/activar")
    public ResponseEntity<Void> activarAsignacionPorUsuarioYRol(
            @PathVariable Long usuarioId, 
            @PathVariable Long rolId) {
        try {
            usuarioRolService.activarAsignacionPorUsuarioYRol(usuarioId, rolId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Desactiva la asignación de un rol específico a un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return ResponseEntity con código 200 OK o 404 NOT FOUND
     */
    @PutMapping("/usuario/{usuarioId}/rol/{rolId}/desactivar")
    public ResponseEntity<Void> desactivarAsignacionPorUsuarioYRol(
            @PathVariable Long usuarioId, 
            @PathVariable Long rolId) {
        try {
            usuarioRolService.desactivarAsignacionPorUsuarioYRol(usuarioId, rolId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Desasigna un rol de un usuario (elimina la asignación).
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return ResponseEntity con código 204 NO CONTENT o 404 NOT FOUND
     */
    @DeleteMapping("/usuario/{usuarioId}/rol/{rolId}")
    public ResponseEntity<Void> desasignarRolDeUsuario(
            @PathVariable Long usuarioId, 
            @PathVariable Long rolId) {
        try {
            usuarioRolService.desasignarRolDeUsuario(usuarioId, rolId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========================================
    // FILTROS POR ESTADO
    // ========================================

    /**
     * Obtiene todas las asignaciones activas del sistema.
     * 
     * @return ResponseEntity con la lista de asignaciones activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<UsuarioRolDTO>> obtenerAsignacionesActivas() {
        List<UsuarioRolDTO> asignacionesActivas = usuarioRolService.obtenerAsignacionesActivas();
        return ResponseEntity.ok(asignacionesActivas);
    }

    /**
     * Obtiene todas las asignaciones inactivas del sistema.
     * 
     * @return ResponseEntity con la lista de asignaciones inactivas
     */
    @GetMapping("/inactivas")
    public ResponseEntity<List<UsuarioRolDTO>> obtenerAsignacionesInactivas() {
        List<UsuarioRolDTO> asignacionesInactivas = usuarioRolService.obtenerAsignacionesInactivas();
        return ResponseEntity.ok(asignacionesInactivas);
    }

    // ========================================
    // VALIDACIONES Y VERIFICACIONES
    // ========================================

    /**
     * Verifica si un usuario tiene un rol específico asignado (activo).
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return ResponseEntity con true/false indicando si tiene el rol
     */
    @GetMapping("/usuario/{usuarioId}/tiene-rol/{rolId}")
    public ResponseEntity<Boolean> usuarioTieneRol(
            @PathVariable Long usuarioId, 
            @PathVariable Long rolId) {
        boolean tieneRol = usuarioRolService.usuarioTieneRol(usuarioId, rolId);
        return ResponseEntity.ok(tieneRol);
    }
}

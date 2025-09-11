package com.exe.ConjuntoResidencialArkania.Controller;

import com.exe.ConjuntoResidencialArkania.DTO.RolDTO;
import com.exe.ConjuntoResidencialArkania.Service.RolService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de roles en el sistema de conjunto residencial.
 * 
 * Este controlador expone endpoints para:
 * - CRUD completo de roles
 * - Búsquedas específicas por nombre
 * - Listados filtrados por estado
 * - Operaciones de activación/desactivación
 * - Gestión de permisos granulares
 * - Consultas estadísticas
 * 
 * Todos los endpoints siguen las convenciones REST y manejan
 * validaciones automáticas a través de las anotaciones del DTO.
 * 
 * Base URL: /api/roles
 */
@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RolController {

    @Autowired
    private RolService rolService;

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    /**
     * Crea un nuevo rol en el sistema.
     * 
     * @param rolDTO Datos del rol a crear (validados automáticamente)
     * @return ResponseEntity con el rol creado y código 201 CREATED
     */
    @PostMapping
    public ResponseEntity<RolDTO> crearRol(@Valid @RequestBody RolDTO rolDTO) {
        try {
            RolDTO rolCreado = rolService.crearRol(rolDTO);
            return new ResponseEntity<>(rolCreado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Obtiene un rol por su ID.
     * 
     * @param rolId ID del rol a buscar
     * @return ResponseEntity con el rol encontrado o 404 NOT FOUND
     */
    @GetMapping("/{rolId}")
    public ResponseEntity<RolDTO> obtenerRolPorId(@PathVariable Long rolId) {
        Optional<RolDTO> rol = rolService.obtenerRolPorId(rolId);
        return rol.map(r -> ResponseEntity.ok(r))
                 .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los roles del sistema.
     * 
     * @return ResponseEntity con la lista de todos los roles
     */
    @GetMapping
    public ResponseEntity<List<RolDTO>> obtenerTodosLosRoles() {
        List<RolDTO> roles = rolService.obtenerTodosLosRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Actualiza completamente un rol existente.
     * 
     * @param rolId ID del rol a actualizar
     * @param rolDTO Nuevos datos del rol (validados automáticamente)
     * @return ResponseEntity con el rol actualizado o error correspondiente
     */
    @PutMapping("/{rolId}")
    public ResponseEntity<RolDTO> actualizarRol(
            @PathVariable Long rolId, 
            @Valid @RequestBody RolDTO rolDTO) {
        try {
            RolDTO rolActualizado = rolService.actualizarRol(rolId, rolDTO);
            return ResponseEntity.ok(rolActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualiza parcialmente un rol existente.
     * Solo actualiza los campos proporcionados en el DTO.
     * 
     * @param rolId ID del rol a actualizar
     * @param rolDTO Datos parciales del rol
     * @return ResponseEntity con el rol actualizado o error correspondiente
     */
    @PatchMapping("/{rolId}")
    public ResponseEntity<RolDTO> actualizarRolParcial(
            @PathVariable Long rolId, 
            @RequestBody RolDTO rolDTO) {
        try {
            RolDTO rolActualizado = rolService.actualizarRolParcial(rolId, rolDTO);
            return ResponseEntity.ok(rolActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina lógicamente un rol (marca como inactivo).
     * 
     * @param rolId ID del rol a eliminar
     * @return ResponseEntity con código 204 NO CONTENT o 404 NOT FOUND
     */
    @DeleteMapping("/{rolId}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long rolId) {
        try {
            rolService.eliminarRol(rolId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========================================
    // BÚSQUEDAS ESPECÍFICAS
    // ========================================

    /**
     * Busca un rol por su nombre único.
     * 
     * @param nombre Nombre del rol (ej: "ADMINISTRADOR", "PROPIETARIO")
     * @return ResponseEntity con el rol encontrado o 404 NOT FOUND
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RolDTO> obtenerRolPorNombre(@PathVariable String nombre) {
        Optional<RolDTO> rol = rolService.obtenerRolPorNombre(nombre);
        return rol.map(r -> ResponseEntity.ok(r))
                 .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca un rol por nombre ignorando mayúsculas y minúsculas.
     * 
     * @param nombre Nombre del rol en cualquier formato
     * @return ResponseEntity con el rol encontrado o 404 NOT FOUND
     */
    @GetMapping("/nombre-ignore-case/{nombre}")
    public ResponseEntity<RolDTO> obtenerRolPorNombreIgnoreCase(@PathVariable String nombre) {
        Optional<RolDTO> rol = rolService.obtenerRolPorNombreIgnoreCase(nombre);
        return rol.map(r -> ResponseEntity.ok(r))
                 .orElse(ResponseEntity.notFound().build());
    }

    // ========================================
    // FILTROS POR ESTADO
    // ========================================

    /**
     * Obtiene todos los roles activos del sistema.
     * 
     * @return ResponseEntity con la lista de roles activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<RolDTO>> obtenerRolesActivos() {
        List<RolDTO> rolesActivos = rolService.obtenerRolesActivos();
        return ResponseEntity.ok(rolesActivos);
    }

    /**
     * Obtiene todos los roles inactivos del sistema.
     * 
     * @return ResponseEntity con la lista de roles inactivos
     */
    @GetMapping("/inactivos")
    public ResponseEntity<List<RolDTO>> obtenerRolesInactivos() {
        List<RolDTO> rolesInactivos = rolService.obtenerRolesInactivos();
        return ResponseEntity.ok(rolesInactivos);
    }

    // ========================================
    // OPERACIONES DE ACTIVACIÓN
    // ========================================

    /**
     * Reactiva un rol previamente eliminado (marca como activo).
     * 
     * @param rolId ID del rol a reactivar
     * @return ResponseEntity con código 200 OK o 404 NOT FOUND
     */
    @PutMapping("/{rolId}/reactivar")
    public ResponseEntity<Void> reactivarRol(@PathVariable Long rolId) {
        try {
            rolService.reactivarRol(rolId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========================================
    // BÚSQUEDAS AVANZADAS
    // ========================================

    /**
     * Busca roles por descripción (búsqueda parcial).
     * 
     * @param descripcion Texto a buscar en la descripción
     * @return ResponseEntity con la lista de roles que coinciden con la búsqueda
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<RolDTO>> buscarRolesPorDescripcion(@RequestParam String descripcion) {
        List<RolDTO> roles = rolService.buscarRolesPorDescripcion(descripcion);
        return ResponseEntity.ok(roles);
    }

    /**
     * Encuentra roles que contienen un permiso específico.
     * 
     * @param permiso Permiso a buscar en la lista de permisos del rol
     * @return ResponseEntity con la lista de roles que contienen el permiso
     */
    @GetMapping("/permiso/{permiso}")
    public ResponseEntity<List<RolDTO>> obtenerRolesPorPermiso(@PathVariable String permiso) {
        List<RolDTO> roles = rolService.obtenerRolesPorPermiso(permiso);
        return ResponseEntity.ok(roles);
    }

    // ========================================
    // GESTIÓN DE PERMISOS
    // ========================================

    /**
     * Agrega un permiso a un rol específico.
     * 
     * @param rolId ID del rol
     * @param permiso Permiso a agregar
     * @return ResponseEntity indicando el resultado de la operación
     */
    @PostMapping("/{rolId}/permisos/{permiso}")
    public ResponseEntity<Void> agregarPermisoARol(
            @PathVariable Long rolId, 
            @PathVariable String permiso) {
        try {
            rolService.agregarPermisoARol(rolId, permiso);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

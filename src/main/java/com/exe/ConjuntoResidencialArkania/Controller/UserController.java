package com.exe.ConjuntoResidencialArkania.Controller;

import com.exe.ConjuntoResidencialArkania.DTO.UserDTO;
import com.exe.ConjuntoResidencialArkania.Service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de usuarios en el sistema de conjunto residencial.
 * 
 * Este controlador expone endpoints para:
 * - CRUD completo de usuarios
 * - Búsquedas específicas por documento y email
 * - Listados filtrados por estado
 * - Operaciones de activación/desactivación
 * - Búsquedas avanzadas por nombre
 * 
 * Todos los endpoints siguen las convenciones REST y manejan
 * validaciones automáticas a través de las anotaciones del DTO.
 * 
 * Base URL: /api/usuarios
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    /**
     * Crea un nuevo usuario en el sistema.
     * 
     * @param userDTO Datos del usuario a crear (validados automáticamente)
     * @return ResponseEntity con el usuario creado y código 201 CREATED
     */
    @PostMapping
    public ResponseEntity<UserDTO> crearUsuario(@Valid @RequestBody UserDTO userDTO) {
        try {
            UserDTO usuarioCreado = userService.crearUsuario(userDTO);
            return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Obtiene un usuario por su ID.
     * 
     * @param usuarioId ID del usuario a buscar
     * @return ResponseEntity con el usuario encontrado o 404 NOT FOUND
     */
    @GetMapping("/{usuarioId}")
    public ResponseEntity<UserDTO> obtenerUsuarioPorId(@PathVariable Long usuarioId) {
        Optional<UserDTO> usuario = userService.obtenerUsuarioPorId(usuarioId);
        return usuario.map(user -> ResponseEntity.ok(user))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtiene todos los usuarios del sistema.
     * 
     * @return ResponseEntity con la lista de todos los usuarios
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> obtenerTodosLosUsuarios() {
        List<UserDTO> usuarios = userService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Actualiza completamente un usuario existente.
     * 
     * @param usuarioId ID del usuario a actualizar
     * @param userDTO Nuevos datos del usuario (validados automáticamente)
     * @return ResponseEntity con el usuario actualizado o error correspondiente
     */
    @PutMapping("/{usuarioId}")
    public ResponseEntity<UserDTO> actualizarUsuario(
            @PathVariable Long usuarioId, 
            @Valid @RequestBody UserDTO userDTO) {
        try {
            UserDTO usuarioActualizado = userService.actualizarUsuario(usuarioId, userDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualiza parcialmente un usuario existente.
     * Solo actualiza los campos proporcionados en el DTO.
     * 
     * @param usuarioId ID del usuario a actualizar
     * @param userDTO Datos parciales del usuario
     * @return ResponseEntity con el usuario actualizado o error correspondiente
     */
    @PatchMapping("/{usuarioId}")
    public ResponseEntity<UserDTO> actualizarUsuarioParcial(
            @PathVariable Long usuarioId, 
            @RequestBody UserDTO userDTO) {
        try {
            UserDTO usuarioActualizado = userService.actualizarUsuarioParcial(usuarioId, userDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina lógicamente un usuario (marca como inactivo).
     * 
     * @param usuarioId ID del usuario a eliminar
     * @return ResponseEntity con código 204 NO CONTENT o 404 NOT FOUND
     */
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long usuarioId) {
        try {
            userService.eliminarUsuario(usuarioId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================
    // BÚSQUEDAS ESPECÍFICAS
    // ========================================

    /**
     * Busca un usuario por su número de documento.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @return ResponseEntity con el usuario encontrado o 404 NOT FOUND
     */
    @GetMapping("/documento/{numeroDocumento}")
    public ResponseEntity<UserDTO> obtenerUsuarioPorDocumento(@PathVariable String numeroDocumento) {
        Optional<UserDTO> usuario = userService.obtenerUsuarioPorDocumento(numeroDocumento);
        return usuario.map(user -> ResponseEntity.ok(user))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Busca un usuario por su email.
     * 
     * @param email Email del usuario
     * @return ResponseEntity con el usuario encontrado o 404 NOT FOUND
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> obtenerUsuarioPorEmail(@PathVariable String email) {
        Optional<UserDTO> usuario = userService.obtenerUsuarioPorEmail(email);
        return usuario.map(user -> ResponseEntity.ok(user))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ========================================
    // FILTROS POR ESTADO
    // ========================================

    /**
     * Obtiene todos los usuarios activos del sistema.
     * 
     * @return ResponseEntity con la lista de usuarios activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<UserDTO>> obtenerUsuariosActivos() {
        List<UserDTO> usuariosActivos = userService.obtenerUsuariosActivos();
        return ResponseEntity.ok(usuariosActivos);
    }

    /**
     * Obtiene todos los usuarios inactivos del sistema.
     * 
     * @return ResponseEntity con la lista de usuarios inactivos
     */
    @GetMapping("/inactivos")
    public ResponseEntity<List<UserDTO>> obtenerUsuariosInactivos() {
        List<UserDTO> usuariosInactivos = userService.obtenerUsuariosInactivos();
        return ResponseEntity.ok(usuariosInactivos);
    }

    // ========================================
    // OPERACIONES DE ACTIVACIÓN
    // ========================================

    /**
     * Reactiva un usuario previamente eliminado (marca como activo).
     * 
     * @param usuarioId ID del usuario a reactivar
     * @return ResponseEntity con código 200 OK o 404 NOT FOUND
     */
    @PutMapping("/{usuarioId}/reactivar")
    public ResponseEntity<Void> reactivarUsuario(@PathVariable Long usuarioId) {
        try {
            userService.reactivarUsuario(usuarioId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================
    // BÚSQUEDAS AVANZADAS
    // ========================================

    /**
     * Busca usuarios por nombres o apellidos (búsqueda parcial).
     * 
     * @param busqueda Texto a buscar en nombres o apellidos
     * @return ResponseEntity con la lista de usuarios que coinciden con la búsqueda
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<UserDTO>> buscarUsuariosPorNombre(@RequestParam String busqueda) {
        List<UserDTO> usuarios = userService.buscarUsuariosPorNombre(busqueda);
        return ResponseEntity.ok(usuarios);
    }
}

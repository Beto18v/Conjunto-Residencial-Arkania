package com.exe.ConjuntoResidencialArkania.Impl;

import com.exe.ConjuntoResidencialArkania.DTO.UsuarioRolDTO;
import com.exe.ConjuntoResidencialArkania.Entity.UsuarioRol;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;
import com.exe.ConjuntoResidencialArkania.Entity.RolEntity;
import com.exe.ConjuntoResidencialArkania.Repository.UsuarioRolRepository;
import com.exe.ConjuntoResidencialArkania.Repository.UserRepository;
import com.exe.ConjuntoResidencialArkania.Repository.RolRepository;
import com.exe.ConjuntoResidencialArkania.Service.UsuarioRolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la gestión de asignaciones Usuario-Rol en el sistema de conjunto residencial.
 * 
 * Esta clase implementa todas las operaciones definidas en UsuarioRolService,
 * manejando la lógica de negocio relacionada con la asignación de roles a usuarios, incluyendo:
 * - CRUD completo de asignaciones usuario-rol
 * - Validaciones de negocio específicas para asignaciones
 * - Operaciones masivas de asignación/desasignación
 * - Conversiones entre DTOs y Entidades
 * - Operaciones estadísticas y de auditoría
 * 
 * Utiliza transacciones para asegurar la consistencia de datos
 * y maneja todas las validaciones de negocio necesarias.
 */
@Service
@Transactional
public class UsuarioRolServiceImpl implements UsuarioRolService {

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    @Override
    public UsuarioRolDTO crearAsignacion(UsuarioRolDTO usuarioRolDTO) {
        // Validar que exista el usuario
        UserEntity usuario = userRepository.findById(usuarioRolDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioRolDTO.getUsuarioId()));

        // Validar que exista el rol
        RolEntity rol = rolRepository.findById(usuarioRolDTO.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + usuarioRolDTO.getRolId()));

        // Validar que no exista ya una asignación activa
        if (existeAsignacionActiva(usuarioRolDTO.getUsuarioId(), usuarioRolDTO.getRolId())) {
            throw new RuntimeException("Ya existe una asignación activa de este rol para el usuario");
        }

        // Crear la nueva asignación
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRol.setActivo(true);

        UsuarioRol asignacionGuardada = usuarioRolRepository.save(usuarioRol);
        return convertirAUsuarioRolDTO(asignacionGuardada);
    }

    @Override
    public UsuarioRolDTO asignarRolAUsuario(Long usuarioId, Long rolId) {
        UsuarioRolDTO usuarioRolDTO = new UsuarioRolDTO();
        usuarioRolDTO.setUsuarioId(usuarioId);
        usuarioRolDTO.setRolId(rolId);
        return crearAsignacion(usuarioRolDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioRolDTO> obtenerAsignacionPorId(Long usuarioRolId) {
        return usuarioRolRepository.findById(usuarioRolId)
                .map(this::convertirAUsuarioRolDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioRolDTO> obtenerAsignacionPorUsuarioYRol(Long usuarioId, Long rolId) {
        return usuarioRolRepository.findByUsuarioIdAndRolId(usuarioId, rolId)
                .map(this::convertirAUsuarioRolDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioRolDTO> obtenerAsignacionActivaPorUsuarioYRol(Long usuarioId, Long rolId) {
        UserEntity usuario = userRepository.findById(usuarioId).orElse(null);
        RolEntity rol = rolRepository.findById(rolId).orElse(null);
        
        if (usuario != null && rol != null) {
            return usuarioRolRepository.findByUsuarioAndRolAndActivoTrue(usuario, rol)
                    .map(this::convertirAUsuarioRolDTO);
        }
        
        return Optional.empty();
    }

    @Override
    public UsuarioRolDTO actualizarAsignacion(Long usuarioRolId, UsuarioRolDTO usuarioRolDTO) {
        UsuarioRol usuarioRol = usuarioRolRepository.findById(usuarioRolId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + usuarioRolId));

        // Actualizar solo el estado activo (otros campos no se deben cambiar)
        if (usuarioRolDTO.getActivo() != null) {
            usuarioRol.setActivo(usuarioRolDTO.getActivo());
        }

        UsuarioRol asignacionActualizada = usuarioRolRepository.save(usuarioRol);
        return convertirAUsuarioRolDTO(asignacionActualizada);
    }

    @Override
    public void eliminarAsignacion(Long usuarioRolId) {
        UsuarioRol usuarioRol = usuarioRolRepository.findById(usuarioRolId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + usuarioRolId));

        usuarioRolRepository.delete(usuarioRol);
    }

    // ========================================
    // OPERACIONES DE ACTIVACIÓN/DESACTIVACIÓN
    // ========================================

    @Override
    public void activarAsignacion(Long usuarioRolId) {
        UsuarioRol usuarioRol = usuarioRolRepository.findById(usuarioRolId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + usuarioRolId));

        usuarioRol.setActivo(true);
        usuarioRolRepository.save(usuarioRol);
    }

    @Override
    public void desactivarAsignacion(Long usuarioRolId) {
        UsuarioRol usuarioRol = usuarioRolRepository.findById(usuarioRolId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + usuarioRolId));

        usuarioRol.setActivo(false);
        usuarioRolRepository.save(usuarioRol);
    }

    @Override
    public void activarAsignacionPorUsuarioYRol(Long usuarioId, Long rolId) {
        UsuarioRol usuarioRol = usuarioRolRepository.findByUsuarioIdAndRolId(usuarioId, rolId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada para usuario ID: " + usuarioId + " y rol ID: " + rolId));

        usuarioRol.setActivo(true);
        usuarioRolRepository.save(usuarioRol);
    }

    @Override
    public void desactivarAsignacionPorUsuarioYRol(Long usuarioId, Long rolId) {
        UsuarioRol usuarioRol = usuarioRolRepository.findByUsuarioIdAndRolId(usuarioId, rolId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada para usuario ID: " + usuarioId + " y rol ID: " + rolId));

        usuarioRol.setActivo(false);
        usuarioRolRepository.save(usuarioRol);
    }

    @Override
    public void desasignarRolDeUsuario(Long usuarioId, Long rolId) {
        desactivarAsignacionPorUsuarioYRol(usuarioId, rolId);
    }

    // ========================================
    // OPERACIONES DE LISTADO Y BÚSQUEDA
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerTodasLasAsignaciones() {
        List<UsuarioRol> asignaciones = usuarioRolRepository.findAll();
        return convertirAUsuarioRolDTOList(asignaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerAsignacionesActivas() {
        List<UsuarioRol> asignaciones = usuarioRolRepository.findAllActive();
        return convertirAUsuarioRolDTOList(asignaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerAsignacionesInactivas() {
        List<UsuarioRol> todasAsignaciones = usuarioRolRepository.findAll();
        List<UsuarioRol> asignacionesInactivas = todasAsignaciones.stream()
                .filter(asignacion -> !asignacion.getActivo())
                .collect(Collectors.toList());
        return convertirAUsuarioRolDTOList(asignacionesInactivas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerAsignacionesPorUsuario(Long usuarioId) {
        UserEntity usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        List<UsuarioRol> asignaciones = usuarioRolRepository.findAllByUsuario(usuario);
        return convertirAUsuarioRolDTOList(asignaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerAsignacionesActivasPorUsuario(Long usuarioId) {
        List<UsuarioRol> asignaciones = usuarioRolRepository.findActiveByUsuarioId(usuarioId);
        return convertirAUsuarioRolDTOList(asignaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerAsignacionesPorRol(Long rolId) {
        RolEntity rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        List<UsuarioRol> asignaciones = usuarioRolRepository.findAllByRol(rol);
        return convertirAUsuarioRolDTOList(asignaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerAsignacionesActivasPorRol(Long rolId) {
        List<UsuarioRol> asignaciones = usuarioRolRepository.findActiveByRolId(rolId);
        return convertirAUsuarioRolDTOList(asignaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerAsignacionesPorFechaCreacion(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<UsuarioRol> todasAsignaciones = usuarioRolRepository.findAll();
        List<UsuarioRol> asignacionesFiltradas = todasAsignaciones.stream()
                .filter(asignacion -> asignacion.getFechaCreacion().isAfter(fechaInicio.minusSeconds(1)) && 
                                    asignacion.getFechaCreacion().isBefore(fechaFin.plusSeconds(1)))
                .collect(Collectors.toList());
        return convertirAUsuarioRolDTOList(asignacionesFiltradas);
    }

    // ========================================
    // OPERACIONES DE VALIDACIÓN Y VERIFICACIÓN
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public boolean usuarioTieneRol(Long usuarioId, Long rolId) {
        return existeAsignacionActiva(usuarioId, rolId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean usuarioTieneRolPorNombre(Long usuarioId, String nombreRol) {
        UserEntity usuario = userRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return false;
        }

        return usuario.getRoles().stream()
                .anyMatch(rol -> nombreRol.equals(rol.getNombre()) && rol.getActivo());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeAsignacion(Long usuarioId, Long rolId) {
        return usuarioRolRepository.findByUsuarioIdAndRolId(usuarioId, rolId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeAsignacionActiva(Long usuarioId, Long rolId) {
        UserEntity usuario = userRepository.findById(usuarioId).orElse(null);
        RolEntity rol = rolRepository.findById(rolId).orElse(null);
        
        if (usuario != null && rol != null) {
            return usuarioRolRepository.findByUsuarioAndRolAndActivoTrue(usuario, rol).isPresent();
        }
        
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeAsignarRol(Long usuarioId, Long rolId) {
        // Verificar que el usuario existe y está activo
        UserEntity usuario = userRepository.findById(usuarioId).orElse(null);
        if (usuario == null || !usuario.getActivo()) {
            return false;
        }

        // Verificar que el rol existe y está activo
        RolEntity rol = rolRepository.findById(rolId).orElse(null);
        if (rol == null || !rol.getActivo()) {
            return false;
        }

        // Verificar que no existe una asignación activa
        return !existeAsignacionActiva(usuarioId, rolId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeDesasignarRol(Long usuarioId, Long rolId) {
        // Verificar que existe una asignación activa
        if (!existeAsignacionActiva(usuarioId, rolId)) {
            return false;
        }

        // Verificar si es un rol crítico que no se puede desasignar
        RolEntity rol = rolRepository.findById(rolId).orElse(null);
        if (rol != null && "ADMINISTRADOR".equals(rol.getNombre())) {
            // Verificar que no es el último administrador
            long cantidadAdministradores = usuarioRolRepository.findActiveByRolId(rolId).size();
            return cantidadAdministradores > 1;
        }

        return true;
    }

    // ========================================
    // OPERACIONES MASIVAS
    // ========================================

    @Override
    public List<UsuarioRolDTO> asignarMultiplesRolesAUsuario(Long usuarioId, List<Long> rolesIds) {
        List<UsuarioRolDTO> asignaciones = new ArrayList<>();

        for (Long rolId : rolesIds) {
            if (puedeAsignarRol(usuarioId, rolId)) {
                UsuarioRolDTO asignacion = asignarRolAUsuario(usuarioId, rolId);
                asignaciones.add(asignacion);
            } else {
                throw new RuntimeException("No se puede asignar el rol ID: " + rolId + " al usuario ID: " + usuarioId);
            }
        }

        return asignaciones;
    }

    @Override
    public void desasignarMultiplesRolesDeUsuario(Long usuarioId, List<Long> rolesIds) {
        for (Long rolId : rolesIds) {
            if (puedeDesasignarRol(usuarioId, rolId)) {
                desasignarRolDeUsuario(usuarioId, rolId);
            } else {
                throw new RuntimeException("No se puede desasignar el rol ID: " + rolId + " del usuario ID: " + usuarioId);
            }
        }
    }

    @Override
    public List<UsuarioRolDTO> asignarRolAMultiplesUsuarios(Long rolId, List<Long> usuariosIds) {
        List<UsuarioRolDTO> asignaciones = new ArrayList<>();

        for (Long usuarioId : usuariosIds) {
            if (puedeAsignarRol(usuarioId, rolId)) {
                UsuarioRolDTO asignacion = asignarRolAUsuario(usuarioId, rolId);
                asignaciones.add(asignacion);
            } else {
                throw new RuntimeException("No se puede asignar el rol ID: " + rolId + " al usuario ID: " + usuarioId);
            }
        }

        return asignaciones;
    }

    @Override
    public void desasignarRolDeMultiplesUsuarios(Long rolId, List<Long> usuariosIds) {
        for (Long usuarioId : usuariosIds) {
            if (puedeDesasignarRol(usuarioId, rolId)) {
                desasignarRolDeUsuario(usuarioId, rolId);
            } else {
                throw new RuntimeException("No se puede desasignar el rol ID: " + rolId + " del usuario ID: " + usuarioId);
            }
        }
    }

    @Override
    public List<UsuarioRolDTO> reemplazarRolesDeUsuario(Long usuarioId, List<Long> nuevosRolesIds) {
        // Obtener roles actuales del usuario
        List<UsuarioRolDTO> rolesActuales = obtenerAsignacionesActivasPorUsuario(usuarioId);
        
        // Desasignar todos los roles actuales
        for (UsuarioRolDTO asignacionActual : rolesActuales) {
            if (puedeDesasignarRol(usuarioId, asignacionActual.getRolId())) {
                desasignarRolDeUsuario(usuarioId, asignacionActual.getRolId());
            }
        }

        // Asignar los nuevos roles
        return asignarMultiplesRolesAUsuario(usuarioId, nuevosRolesIds);
    }

    // ========================================
    // OPERACIONES DE CONVERSIÓN
    // ========================================

    @Override
    public UsuarioRolDTO convertirAUsuarioRolDTO(UsuarioRol usuarioRol) {
        if (usuarioRol == null) {
            return null;
        }

        UsuarioRolDTO usuarioRolDTO = new UsuarioRolDTO();
        usuarioRolDTO.setUsuarioRolId(usuarioRol.getUsuarioRolId());
        usuarioRolDTO.setUsuarioId(usuarioRol.getUsuario().getUsuarioId());
        usuarioRolDTO.setRolId(usuarioRol.getRol().getRolId());
        usuarioRolDTO.setActivo(usuarioRol.getActivo());
        usuarioRolDTO.setFechaCreacion(usuarioRol.getFechaCreacion());
        usuarioRolDTO.setFechaActualizacion(usuarioRol.getFechaActualizacion());

        // Información adicional del usuario
        UserEntity usuario = usuarioRol.getUsuario();
        usuarioRolDTO.setNumeroDocumentoUsuario(usuario.getNumeroDocumento());
        usuarioRolDTO.setNombreCompletoUsuario(usuario.getNombres() + " " + usuario.getApellidos());

        // Información adicional del rol
        RolEntity rol = usuarioRol.getRol();
        usuarioRolDTO.setNombreRol(rol.getNombre());

        return usuarioRolDTO;
    }

    @Override
    public UsuarioRol convertirAUsuarioRol(UsuarioRolDTO usuarioRolDTO) {
        if (usuarioRolDTO == null) {
            return null;
        }

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuarioRolId(usuarioRolDTO.getUsuarioRolId());
        usuarioRol.setActivo(usuarioRolDTO.getActivo() != null ? usuarioRolDTO.getActivo() : true);
        usuarioRol.setFechaCreacion(usuarioRolDTO.getFechaCreacion());
        usuarioRol.setFechaActualizacion(usuarioRolDTO.getFechaActualizacion());

        // Las relaciones con Usuario y Rol se establecen en el contexto específico donde se necesiten

        return usuarioRol;
    }

    @Override
    public List<UsuarioRolDTO> convertirAUsuarioRolDTOList(List<UsuarioRol> usuarioRoles) {
        if (usuarioRoles == null) {
            return new ArrayList<>();
        }

        return usuarioRoles.stream()
                .map(this::convertirAUsuarioRolDTO)
                .collect(Collectors.toList());
    }

    // ========================================
    // OPERACIONES ESTADÍSTICAS
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public long contarTotalAsignaciones() {
        return usuarioRolRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarAsignacionesActivas() {
        return usuarioRolRepository.findAllActive().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarAsignacionesInactivas() {
        List<UsuarioRol> todasAsignaciones = usuarioRolRepository.findAll();
        return todasAsignaciones.stream()
                .filter(asignacion -> !asignacion.getActivo())
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public int contarRolesDeUsuario(Long usuarioId) {
        return usuarioRolRepository.findActiveByUsuarioId(usuarioId).size();
    }

    @Override
    @Transactional(readOnly = true)
    public int contarUsuariosConRol(Long rolId) {
        return usuarioRolRepository.findActiveByRolId(rolId).size();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Integer> obtenerEstadisticasAsignacionesPorRol() {
        List<RolEntity> roles = rolRepository.findAll();
        
        return roles.stream()
                .collect(Collectors.toMap(
                    RolEntity::getRolId,
                    rol -> usuarioRolRepository.findActiveByRolId(rol.getRolId()).size()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Integer> obtenerEstadisticasAsignacionesPorUsuario() {
        List<UserEntity> usuarios = userRepository.findAll();
        
        return usuarios.stream()
                .collect(Collectors.toMap(
                    UserEntity::getUsuarioId,
                    usuario -> usuarioRolRepository.findActiveByUsuarioId(usuario.getUsuarioId()).size()
                ));
    }

    // ========================================
    // OPERACIONES DE AUDITORÍA
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerHistorialAsignacionesUsuario(Long usuarioId) {
        return obtenerAsignacionesPorUsuario(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerHistorialAsignacionesRol(Long rolId) {
        return obtenerAsignacionesPorRol(rolId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolDTO> obtenerAsignacionesModificadasEnPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<UsuarioRol> todasAsignaciones = usuarioRolRepository.findAll();
        List<UsuarioRol> asignacionesFiltradas = todasAsignaciones.stream()
                .filter(asignacion -> asignacion.getFechaActualizacion().isAfter(fechaInicio.minusSeconds(1)) && 
                                    asignacion.getFechaActualizacion().isBefore(fechaFin.plusSeconds(1)))
                .collect(Collectors.toList());
        return convertirAUsuarioRolDTOList(asignacionesFiltradas);
    }
}

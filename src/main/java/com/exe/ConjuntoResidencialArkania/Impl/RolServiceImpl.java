package com.exe.ConjuntoResidencialArkania.Impl;

import com.exe.ConjuntoResidencialArkania.DTO.RolDTO;
import com.exe.ConjuntoResidencialArkania.Entity.RolEntity;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;
import com.exe.ConjuntoResidencialArkania.Repository.RolRepository;
import com.exe.ConjuntoResidencialArkania.Repository.UserRepository;
import com.exe.ConjuntoResidencialArkania.Service.RolService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la gestión de roles en el sistema de conjunto residencial.
 * 
 * Esta clase implementa todas las operaciones definidas en RolService,
 * manejando la lógica de negocio relacionada con roles, incluyendo:
 * - CRUD completo de roles
 * - Gestión de permisos granulares
 * - Validaciones de negocio específicas
 * - Conversiones entre DTOs y Entidades
 * - Operaciones estadísticas y de auditoría
 * 
 * Utiliza transacciones para asegurar la consistencia de datos
 * y maneja todas las validaciones de negocio necesarias.
 */
@Service
@Transactional
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ========================================
    // MÉTODOS AUXILIARES PARA MANEJO DE PERMISOS
    // ========================================

    /**
     * Convierte una lista de permisos a formato JSON para almacenamiento.
     * 
     * @param permisos Lista de permisos
     * @return JSON string de los permisos
     */
    private String convertirPermisosAJson(List<String> permisos) {
        if (permisos == null || permisos.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(permisos);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir permisos a JSON", e);
        }
    }

    /**
     * Convierte un JSON string a lista de permisos.
     * 
     * @param permisosJson JSON string de los permisos
     * @return Lista de permisos
     */
    private List<String> convertirJsonAPermisos(String permisosJson) {
        if (permisosJson == null || permisosJson.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(permisosJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    @Override
    public RolDTO crearRol(RolDTO rolDTO) {
        // Validar que no exista un rol con el mismo nombre
        String nombreMayuscula = rolDTO.getNombre().toUpperCase();
        if (existeRolPorNombre(nombreMayuscula)) {
            throw new RuntimeException("Ya existe un rol con el nombre: " + nombreMayuscula);
        }

        // Convertir DTO a Entity
        RolEntity rolEntity = convertirARolEntity(rolDTO);
        
        // Establecer valores por defecto
        rolEntity.setNombre(nombreMayuscula);
        rolEntity.setActivo(true);
        
        // Convertir permisos a JSON
        if (rolDTO.getPermisos() != null) {
            rolEntity.setPermisos(convertirPermisosAJson(rolDTO.getPermisos()));
        }

        // Guardar el rol
        RolEntity rolGuardado = rolRepository.save(rolEntity);

        return convertirARolDTO(rolGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDTO> obtenerRolPorId(Long rolId) {
        return rolRepository.findById(rolId)
                .map(this::convertirARolDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDTO> obtenerRolPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre.toUpperCase())
                .map(this::convertirARolDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDTO> obtenerRolPorNombreIgnoreCase(String nombre) {
        return rolRepository.findByNombreIgnoreCase(nombre)
                .map(this::convertirARolDTO);
    }

    @Override
    public RolDTO actualizarRol(Long rolId, RolDTO rolDTO) {
        RolEntity rolEntity = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        // Validar cambio de nombre si es diferente
        String nombreMayuscula = rolDTO.getNombre().toUpperCase();
        if (!rolEntity.getNombre().equals(nombreMayuscula)) {
            if (existeRolPorNombre(nombreMayuscula)) {
                throw new IllegalArgumentException("Ya existe un rol con el nombre: " + nombreMayuscula);
            }
        }

        // Actualizar campos
        if (rolDTO.getNombre() != null) {
            rolEntity.setNombre(nombreMayuscula);
        }
        if (rolDTO.getDescripcion() != null) {
            rolEntity.setDescripcion(rolDTO.getDescripcion());
        }
        if (rolDTO.getActivo() != null) {
            rolEntity.setActivo(rolDTO.getActivo());
        }
        if (rolDTO.getPermisos() != null) {
            rolEntity.setPermisos(convertirPermisosAJson(rolDTO.getPermisos()));
        }

        RolEntity rolActualizado = rolRepository.save(rolEntity);
        return convertirARolDTO(rolActualizado);
    }

    @Override
    public RolDTO actualizarRolParcial(Long rolId, RolDTO rolDTO) {
        return actualizarRol(rolId, rolDTO);
    }

    @Override
    public void eliminarRol(Long rolId) {
        RolEntity rolEntity = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        // Validar que no tenga usuarios asignados
        if (!rolEntity.getUsuarios().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el rol porque tiene usuarios asignados");
        }

        // Eliminación lógica
        rolEntity.setActivo(false);
        rolRepository.save(rolEntity);
    }

    @Override
    public void reactivarRol(Long rolId) {
        RolEntity rolEntity = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        rolEntity.setActivo(true);
        rolRepository.save(rolEntity);
    }

    // ========================================
    // OPERACIONES DE LISTADO Y BÚSQUEDA
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> obtenerTodosLosRoles() {
        List<RolEntity> roles = rolRepository.findAll();
        return convertirARolDTOList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> obtenerRolesActivos() {
        List<RolEntity> roles = rolRepository.findAllRolesActivos();
        return convertirARolDTOList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> obtenerRolesInactivos() {
        List<RolEntity> roles = rolRepository.findAllRolesInactivos();
        return convertirARolDTOList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> buscarRolesPorDescripcion(String descripcion) {
        List<RolEntity> roles = rolRepository.findByDescripcionContaining(descripcion);
        return convertirARolDTOList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> obtenerRolesPorPermiso(String permiso) {
        List<RolEntity> roles = rolRepository.findByPermisosContaining(permiso);
        return convertirARolDTOList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> obtenerRolesPorUsuario(Long usuarioId) {
        UserEntity usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
        
        List<RolEntity> roles = rolRepository.findRolesByUsuario(usuario);
        return convertirARolDTOList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> obtenerRolesSinUsuarios() {
        List<RolEntity> roles = rolRepository.findRolesSinUsuarios();
        return convertirARolDTOList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> obtenerRolesConMasDeXUsuarios(int minUsuarios) {
        List<RolEntity> roles = rolRepository.findRolesConMasDeXUsuarios(minUsuarios);
        return convertirARolDTOList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> obtenerRolesPorFechaCreacion(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<RolEntity> roles = rolRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
        return convertirARolDTOList(roles);
    }

    // ========================================
    // OPERACIONES DE VALIDACIÓN Y VERIFICACIÓN
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public boolean existeRolPorNombre(String nombre) {
        return rolRepository.existsByNombre(nombre.toUpperCase());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeRolActivoPorNombre(String nombre) {
        Optional<RolEntity> rol = rolRepository.findByNombre(nombre.toUpperCase());
        return rol.isPresent() && rol.get().getActivo();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeEliminarRol(Long rolId) {
        RolEntity rol = rolRepository.findById(rolId).orElse(null);
        if (rol == null) {
            return false;
        }

        // Verificar si es un rol del sistema crítico
        if ("ADMINISTRADOR".equals(rol.getNombre()) || "PROPIETARIO".equals(rol.getNombre())) {
            return false;
        }

        // Verificar si tiene usuarios asignados
        return rol.getUsuarios().isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean rolTienePermiso(Long rolId, String permiso) {
        RolEntity rol = rolRepository.findById(rolId).orElse(null);
        if (rol == null) {
            return false;
        }

        List<String> permisos = convertirJsonAPermisos(rol.getPermisos());
        return permisos.contains(permiso);
    }

    // ========================================
    // OPERACIONES DE GESTIÓN DE PERMISOS
    // ========================================

    @Override
    public void agregarPermisoARol(Long rolId, String permiso) {
        RolEntity rolEntity = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        List<String> permisos = convertirJsonAPermisos(rolEntity.getPermisos());
        
        if (permisos.contains(permiso)) {
            throw new IllegalArgumentException("El rol ya tiene el permiso: " + permiso);
        }

        permisos.add(permiso);
        rolEntity.setPermisos(convertirPermisosAJson(permisos));
        rolRepository.save(rolEntity);
    }

    @Override
    public void removerPermisoDeRol(Long rolId, String permiso) {
        RolEntity rolEntity = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        List<String> permisos = convertirJsonAPermisos(rolEntity.getPermisos());
        permisos.remove(permiso);
        
        rolEntity.setPermisos(convertirPermisosAJson(permisos));
        rolRepository.save(rolEntity);
    }

    @Override
    public void establecerPermisosDeRol(Long rolId, List<String> permisos) {
        RolEntity rolEntity = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        rolEntity.setPermisos(convertirPermisosAJson(permisos));
        rolRepository.save(rolEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> obtenerPermisosDeRol(Long rolId) {
        RolEntity rolEntity = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        return convertirJsonAPermisos(rolEntity.getPermisos());
    }

    // ========================================
    // OPERACIONES DE GESTIÓN DE USUARIOS
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> obtenerUsuariosConRol(Long rolId) {
        RolEntity rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        return new ArrayList<>(rol.getUsuarios());
    }

    @Override
    @Transactional(readOnly = true)
    public int contarUsuariosConRol(Long rolId) {
        RolEntity rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        return rol.getUsuarios().size();
    }

    // ========================================
    // OPERACIONES DE CONVERSIÓN
    // ========================================

    @Override
    public RolDTO convertirARolDTO(RolEntity rolEntity) {
        if (rolEntity == null) {
            return null;
        }

        RolDTO rolDTO = new RolDTO();
        rolDTO.setRolId(rolEntity.getRolId());
        rolDTO.setNombre(rolEntity.getNombre());
        rolDTO.setDescripcion(rolEntity.getDescripcion());
        rolDTO.setActivo(rolEntity.getActivo());
        rolDTO.setFechaCreacion(rolEntity.getFechaCreacion());
        rolDTO.setFechaActualizacion(rolEntity.getFechaActualizacion());

        // Convertir permisos JSON a lista
        rolDTO.setPermisos(convertirJsonAPermisos(rolEntity.getPermisos()));

        // Establecer número de usuarios
        rolDTO.setNumeroUsuarios(rolEntity.getUsuarios().size());

        return rolDTO;
    }

    @Override
    public RolEntity convertirARolEntity(RolDTO rolDTO) {
        if (rolDTO == null) {
            return null;
        }

        RolEntity rolEntity = new RolEntity();
        rolEntity.setRolId(rolDTO.getRolId());
        rolEntity.setNombre(rolDTO.getNombre() != null ? rolDTO.getNombre().toUpperCase() : null);
        rolEntity.setDescripcion(rolDTO.getDescripcion());
        rolEntity.setActivo(rolDTO.getActivo() != null ? rolDTO.getActivo() : true);
        rolEntity.setFechaCreacion(rolDTO.getFechaCreacion());
        rolEntity.setFechaActualizacion(rolDTO.getFechaActualizacion());

        // La conversión de permisos y usuarios se maneja en el contexto específico donde se necesite
        rolEntity.setUsuarios(new HashSet<>());

        return rolEntity;
    }

    @Override
    public List<RolDTO> convertirARolDTOList(List<RolEntity> rolEntities) {
        if (rolEntities == null) {
            return new ArrayList<>();
        }

        return rolEntities.stream()
                .map(this::convertirARolDTO)
                .collect(Collectors.toList());
    }

    // ========================================
    // OPERACIONES ESTADÍSTICAS
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public long contarTotalRoles() {
        return rolRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarRolesActivos() {
        return rolRepository.findAllRolesActivos().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarRolesInactivos() {
        return rolRepository.findAllRolesInactivos().size();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> obtenerEstadisticasAsignacionRoles() {
        List<RolEntity> roles = rolRepository.findAll();
        
        return roles.stream()
                .collect(Collectors.toMap(
                    RolEntity::getNombre,
                    rol -> rol.getUsuarios().size()
                ));
    }

    // ========================================
    // OPERACIONES ESPECIALES
    // ========================================

    @Override
    public void inicializarRolesPorDefecto() {
        // Crear roles básicos si no existen
        if (!existeRolPorNombre("ADMINISTRADOR")) {
            RolDTO admin = new RolDTO("ADMINISTRADOR", "Administrador del sistema con acceso completo");
            admin.setPermisos(Arrays.asList("ALL_PERMISSIONS"));
            crearRol(admin);
        }

        if (!existeRolPorNombre("PROPIETARIO")) {
            RolDTO propietario = new RolDTO("PROPIETARIO", "Propietario de apartamento en el conjunto");
            propietario.setPermisos(Arrays.asList("READ_PROFILE", "UPDATE_PROFILE", "READ_CORRESPONDENCE"));
            crearRol(propietario);
        }

        if (!existeRolPorNombre("ARRENDATARIO")) {
            RolDTO arrendatario = new RolDTO("ARRENDATARIO", "Arrendatario de apartamento en el conjunto");
            arrendatario.setPermisos(Arrays.asList("READ_PROFILE", "UPDATE_PROFILE"));
            crearRol(arrendatario);
        }

        if (!existeRolPorNombre("VIGILANTE")) {
            RolDTO vigilante = new RolDTO("VIGILANTE", "Personal de seguridad del conjunto");
            vigilante.setPermisos(Arrays.asList("READ_VISITORS", "MANAGE_ACCESS"));
            crearRol(vigilante);
        }

        if (!existeRolPorNombre("VISITANTE")) {
            RolDTO visitante = new RolDTO("VISITANTE", "Visitante temporal del conjunto");
            visitante.setPermisos(Arrays.asList("LIMITED_ACCESS"));
            crearRol(visitante);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarEstructuraPermisos(List<String> permisos) {
        if (permisos == null) {
            return true;
        }

        // Lista de permisos válidos del sistema
        Set<String> permisosValidos = Set.of(
            "ALL_PERMISSIONS",
            "READ_USERS", "WRITE_USERS", "DELETE_USERS",
            "READ_ROLES", "WRITE_ROLES", "DELETE_ROLES",
            "READ_APARTMENTS", "WRITE_APARTMENTS", "DELETE_APARTMENTS",
            "READ_CORRESPONDENCE", "WRITE_CORRESPONDENCE", "DELETE_CORRESPONDENCE",
            "READ_VISITORS", "WRITE_VISITORS", "DELETE_VISITORS",
            "READ_PROFILE", "UPDATE_PROFILE",
            "MANAGE_ACCESS", "LIMITED_ACCESS"
        );

        return permisos.stream().allMatch(permisosValidos::contains);
    }
}

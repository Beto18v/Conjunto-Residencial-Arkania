package com.exe.ConjuntoResidencialArkania.Service;

import com.exe.ConjuntoResidencialArkania.DTO.RolDTO;
import com.exe.ConjuntoResidencialArkania.Entity.RolEntity;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de roles en el sistema de conjunto residencial.
 * 
 * Define las operaciones de negocio disponibles para el manejo de roles,
 * incluyendo CRUD básico, búsquedas específicas, validaciones de negocio,
 * gestión de permisos y operaciones relacionadas con usuarios.
 * 
 * Esta interfaz sigue el patrón de separación de responsabilidades,
 * manteniendo la lógica de negocio separada de la lógica de acceso a datos.
 */
public interface RolService {

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    /**
     * Crea un nuevo rol en el sistema.
     * Valida que no exista otro rol con el mismo nombre.
     * Convierte el nombre a mayúsculas automáticamente.
     * 
     * @param rolDTO Datos del rol a crear
     * @return RolDTO con la información del rol creado (incluyendo ID generado)
     * @throws IllegalArgumentException si los datos son inválidos
     * @throws RuntimeException si ya existe un rol con el mismo nombre
     */
    RolDTO crearRol(RolDTO rolDTO);

    /**
     * Obtiene un rol por su ID.
     * 
     * @param rolId ID del rol a buscar
     * @return Optional con el rol encontrado o vacío si no existe
     */
    Optional<RolDTO> obtenerRolPorId(Long rolId);

    /**
     * Obtiene un rol por su nombre único.
     * 
     * @param nombre Nombre del rol (ej: "ADMINISTRADOR", "PROPIETARIO")
     * @return Optional con el rol encontrado o vacío si no existe
     */
    Optional<RolDTO> obtenerRolPorNombre(String nombre);

    /**
     * Obtiene un rol por nombre ignorando mayúsculas y minúsculas.
     * 
     * @param nombre Nombre del rol en cualquier formato
     * @return Optional con el rol encontrado o vacío si no existe
     */
    Optional<RolDTO> obtenerRolPorNombreIgnoreCase(String nombre);

    /**
     * Actualiza la información de un rol existente.
     * No permite cambiar el nombre si ya está en uso por otro rol.
     * 
     * @param rolId ID del rol a actualizar
     * @param rolDTO Nuevos datos del rol
     * @return RolDTO con la información actualizada
     * @throws RuntimeException si el rol no existe
     * @throws IllegalArgumentException si se intenta cambiar a un nombre ya en uso
     */
    RolDTO actualizarRol(Long rolId, RolDTO rolDTO);

    /**
     * Actualiza parcialmente un rol (solo los campos proporcionados).
     * 
     * @param rolId ID del rol a actualizar
     * @param rolDTO Datos parciales del rol (campos null se ignoran)
     * @return RolDTO con la información actualizada
     * @throws RuntimeException si el rol no existe
     */
    RolDTO actualizarRolParcial(Long rolId, RolDTO rolDTO);

    /**
     * Elimina lógicamente un rol (marca como inactivo).
     * Valida que no tenga usuarios asignados antes de eliminar.
     * 
     * @param rolId ID del rol a eliminar
     * @throws RuntimeException si el rol no existe o tiene usuarios asignados
     */
    void eliminarRol(Long rolId);

    /**
     * Reactiva un rol previamente eliminado (marca como activo).
     * 
     * @param rolId ID del rol a reactivar
     * @throws RuntimeException si el rol no existe
     */
    void reactivarRol(Long rolId);

    // ========================================
    // OPERACIONES DE LISTADO Y BÚSQUEDA
    // ========================================

    /**
     * Obtiene todos los roles del sistema.
     * 
     * @return Lista de todos los roles (activos e inactivos)
     */
    List<RolDTO> obtenerTodosLosRoles();

    /**
     * Obtiene todos los roles activos ordenados por nombre.
     * 
     * @return Lista de roles activos
     */
    List<RolDTO> obtenerRolesActivos();

    /**
     * Obtiene todos los roles inactivos.
     * 
     * @return Lista de roles inactivos
     */
    List<RolDTO> obtenerRolesInactivos();

    /**
     * Busca roles por descripción (búsqueda parcial).
     * 
     * @param descripcion Texto a buscar en la descripción
     * @return Lista de roles que coinciden con la búsqueda
     */
    List<RolDTO> buscarRolesPorDescripcion(String descripcion);

    /**
     * Obtiene roles que contienen un permiso específico.
     * 
     * @param permiso Permiso a buscar en la lista de permisos del rol
     * @return Lista de roles que contienen el permiso especificado
     */
    List<RolDTO> obtenerRolesPorPermiso(String permiso);

    /**
     * Obtiene roles asignados a un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de roles asignados al usuario
     */
    List<RolDTO> obtenerRolesPorUsuario(Long usuarioId);

    /**
     * Obtiene roles que no están asignados a ningún usuario.
     * 
     * @return Lista de roles sin usuarios asignados
     */
    List<RolDTO> obtenerRolesSinUsuarios();

    /**
     * Obtiene roles con más de X usuarios asignados.
     * 
     * @param minUsuarios Número mínimo de usuarios que debe tener el rol
     * @return Lista de roles con más usuarios que el mínimo especificado
     */
    List<RolDTO> obtenerRolesConMasDeXUsuarios(int minUsuarios);

    /**
     * Obtiene roles creados en un rango de fechas.
     * 
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de roles creados en el rango especificado
     */
    List<RolDTO> obtenerRolesPorFechaCreacion(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // ========================================
    // OPERACIONES DE VALIDACIÓN Y VERIFICACIÓN
    // ========================================

    /**
     * Verifica si existe un rol con el nombre especificado.
     * 
     * @param nombre Nombre del rol a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existeRolPorNombre(String nombre);

    /**
     * Verifica si existe un rol activo con el nombre especificado.
     * 
     * @param nombre Nombre del rol a verificar
     * @return true si existe y está activo, false en caso contrario
     */
    boolean existeRolActivoPorNombre(String nombre);

    /**
     * Valida si un rol puede ser eliminado.
     * Verifica que no tenga usuarios asignados y otras reglas de negocio.
     * 
     * @param rolId ID del rol a validar
     * @return true si puede ser eliminado, false en caso contrario
     */
    boolean puedeEliminarRol(Long rolId);

    /**
     * Verifica si un rol tiene un permiso específico.
     * 
     * @param rolId ID del rol
     * @param permiso Permiso a verificar
     * @return true si el rol tiene el permiso, false en caso contrario
     */
    boolean rolTienePermiso(Long rolId, String permiso);

    // ========================================
    // OPERACIONES DE GESTIÓN DE PERMISOS
    // ========================================

    /**
     * Agrega un permiso a un rol.
     * 
     * @param rolId ID del rol
     * @param permiso Permiso a agregar
     * @throws RuntimeException si el rol no existe
     * @throws IllegalArgumentException si el permiso ya existe en el rol
     */
    void agregarPermisoARol(Long rolId, String permiso);

    /**
     * Remueve un permiso de un rol.
     * 
     * @param rolId ID del rol
     * @param permiso Permiso a remover
     * @throws RuntimeException si el rol no existe
     */
    void removerPermisoDeRol(Long rolId, String permiso);

    /**
     * Establece la lista completa de permisos para un rol.
     * Reemplaza todos los permisos existentes.
     * 
     * @param rolId ID del rol
     * @param permisos Lista de permisos a establecer
     * @throws RuntimeException si el rol no existe
     */
    void establecerPermisosDeRol(Long rolId, List<String> permisos);

    /**
     * Obtiene todos los permisos de un rol.
     * 
     * @param rolId ID del rol
     * @return Lista de permisos del rol
     * @throws RuntimeException si el rol no existe
     */
    List<String> obtenerPermisosDeRol(Long rolId);

    // ========================================
    // OPERACIONES DE GESTIÓN DE USUARIOS
    // ========================================

    /**
     * Obtiene todos los usuarios que tienen un rol específico.
     * 
     * @param rolId ID del rol
     * @return Lista de usuarios con el rol asignado
     */
    List<UserEntity> obtenerUsuariosConRol(Long rolId);

    /**
     * Cuenta el número de usuarios que tienen un rol específico.
     * 
     * @param rolId ID del rol
     * @return Número de usuarios con el rol asignado
     */
    int contarUsuariosConRol(Long rolId);

    // ========================================
    // OPERACIONES DE CONVERSIÓN
    // ========================================

    /**
     * Convierte una entidad RolEntity a RolDTO.
     * 
     * @param rolEntity Entidad a convertir
     * @return RolDTO correspondiente
     */
    RolDTO convertirARolDTO(RolEntity rolEntity);

    /**
     * Convierte un RolDTO a RolEntity.
     * 
     * @param rolDTO DTO a convertir
     * @return RolEntity correspondiente
     */
    RolEntity convertirARolEntity(RolDTO rolDTO);

    /**
     * Convierte una lista de entidades RolEntity a una lista de RolDTO.
     * 
     * @param rolEntities Lista de entidades a convertir
     * @return Lista de RolDTO correspondiente
     */
    List<RolDTO> convertirARolDTOList(List<RolEntity> rolEntities);

    // ========================================
    // OPERACIONES ESTADÍSTICAS
    // ========================================

    /**
     * Obtiene el número total de roles registrados.
     * 
     * @return Número total de roles
     */
    long contarTotalRoles();

    /**
     * Obtiene el número de roles activos.
     * 
     * @return Número de roles activos
     */
    long contarRolesActivos();

    /**
     * Obtiene el número de roles inactivos.
     * 
     * @return Número de roles inactivos
     */
    long contarRolesInactivos();

    /**
     * Obtiene estadísticas de asignación de roles.
     * 
     * @return Map con el nombre del rol como clave y el número de usuarios asignados como valor
     */
    java.util.Map<String, Integer> obtenerEstadisticasAsignacionRoles();

    // ========================================
    // OPERACIONES ESPECIALES
    // ========================================

    /**
     * Inicializa los roles por defecto del sistema.
     * Crea roles básicos como ADMINISTRADOR, PROPIETARIO, etc. si no existen.
     */
    void inicializarRolesPorDefecto();

    /**
     * Valida la estructura de permisos de un rol.
     * 
     * @param permisos Lista de permisos a validar
     * @return true si todos los permisos son válidos, false en caso contrario
     */
    boolean validarEstructuraPermisos(List<String> permisos);
}

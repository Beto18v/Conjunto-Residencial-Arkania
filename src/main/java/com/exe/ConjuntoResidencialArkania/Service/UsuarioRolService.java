package com.exe.ConjuntoResidencialArkania.Service;

import com.exe.ConjuntoResidencialArkania.DTO.UsuarioRolDTO;
import com.exe.ConjuntoResidencialArkania.Entity.UsuarioRol;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de asignaciones Usuario-Rol en el sistema de conjunto residencial.
 * 
 * Define las operaciones de negocio disponibles para el manejo de la relación many-to-many
 * entre usuarios y roles, incluyendo asignación, desasignación, consultas específicas,
 * validaciones de negocio y operaciones de auditoría.
 * 
 * Esta interfaz centraliza toda la lógica relacionada con la tabla intermedia usuario_rol,
 * proporcionando control granular sobre las asignaciones de roles.
 */
public interface UsuarioRolService {

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    /**
     * Crea una nueva asignación de rol a un usuario.
     * Valida que no exista ya una asignación activa del mismo rol al usuario.
     * 
     * @param usuarioRolDTO Datos de la asignación a crear
     * @return UsuarioRolDTO con la información de la asignación creada (incluyendo ID generado)
     * @throws IllegalArgumentException si los datos son inválidos
     * @throws RuntimeException si ya existe una asignación activa del mismo rol al usuario
     */
    UsuarioRolDTO crearAsignacion(UsuarioRolDTO usuarioRolDTO);

    /**
     * Asigna un rol específico a un usuario específico.
     * Método de conveniencia que simplifica la asignación básica.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return UsuarioRolDTO con la información de la asignación creada
     * @throws RuntimeException si el usuario o rol no existen
     */
    UsuarioRolDTO asignarRolAUsuario(Long usuarioId, Long rolId);

    /**
     * Obtiene una asignación usuario-rol por su ID.
     * 
     * @param usuarioRolId ID de la asignación a buscar
     * @return Optional con la asignación encontrada o vacío si no existe
     */
    Optional<UsuarioRolDTO> obtenerAsignacionPorId(Long usuarioRolId);

    /**
     * Obtiene una asignación específica usuario-rol.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return Optional con la asignación encontrada o vacío si no existe
     */
    Optional<UsuarioRolDTO> obtenerAsignacionPorUsuarioYRol(Long usuarioId, Long rolId);

    /**
     * Obtiene una asignación activa específica usuario-rol.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return Optional con la asignación activa encontrada o vacío si no existe
     */
    Optional<UsuarioRolDTO> obtenerAsignacionActivaPorUsuarioYRol(Long usuarioId, Long rolId);

    /**
     * Actualiza el estado y información de una asignación usuario-rol.
     * 
     * @param usuarioRolId ID de la asignación a actualizar
     * @param usuarioRolDTO Nuevos datos de la asignación
     * @return UsuarioRolDTO con la información actualizada
     * @throws RuntimeException si la asignación no existe
     */
    UsuarioRolDTO actualizarAsignacion(Long usuarioRolId, UsuarioRolDTO usuarioRolDTO);

    /**
     * Elimina físicamente una asignación usuario-rol.
     * 
     * @param usuarioRolId ID de la asignación a eliminar
     * @throws RuntimeException si la asignación no existe
     */
    void eliminarAsignacion(Long usuarioRolId);

    // ========================================
    // OPERACIONES DE ACTIVACIÓN/DESACTIVACIÓN
    // ========================================

    /**
     * Activa una asignación usuario-rol.
     * 
     * @param usuarioRolId ID de la asignación a activar
     * @throws RuntimeException si la asignación no existe
     */
    void activarAsignacion(Long usuarioRolId);

    /**
     * Desactiva una asignación usuario-rol.
     * 
     * @param usuarioRolId ID de la asignación a desactivar
     * @throws RuntimeException si la asignación no existe
     */
    void desactivarAsignacion(Long usuarioRolId);

    /**
     * Activa una asignación específica usuario-rol.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @throws RuntimeException si la asignación no existe
     */
    void activarAsignacionPorUsuarioYRol(Long usuarioId, Long rolId);

    /**
     * Desactiva una asignación específica usuario-rol.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @throws RuntimeException si la asignación no existe
     */
    void desactivarAsignacionPorUsuarioYRol(Long usuarioId, Long rolId);

    /**
     * Desasigna un rol de un usuario (marca como inactivo).
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @throws RuntimeException si el usuario no tiene el rol asignado
     */
    void desasignarRolDeUsuario(Long usuarioId, Long rolId);

    // ========================================
    // OPERACIONES DE LISTADO Y BÚSQUEDA
    // ========================================

    /**
     * Obtiene todas las asignaciones del sistema.
     * 
     * @return Lista de todas las asignaciones (activas e inactivas)
     */
    List<UsuarioRolDTO> obtenerTodasLasAsignaciones();

    /**
     * Obtiene todas las asignaciones activas del sistema.
     * 
     * @return Lista de todas las asignaciones activas
     */
    List<UsuarioRolDTO> obtenerAsignacionesActivas();

    /**
     * Obtiene todas las asignaciones inactivas del sistema.
     * 
     * @return Lista de todas las asignaciones inactivas
     */
    List<UsuarioRolDTO> obtenerAsignacionesInactivas();

    /**
     * Obtiene todas las asignaciones de roles para un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de asignaciones del usuario (activas e inactivas)
     */
    List<UsuarioRolDTO> obtenerAsignacionesPorUsuario(Long usuarioId);

    /**
     * Obtiene todas las asignaciones activas de roles para un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de asignaciones activas del usuario
     */
    List<UsuarioRolDTO> obtenerAsignacionesActivasPorUsuario(Long usuarioId);

    /**
     * Obtiene todas las asignaciones de un rol específico.
     * 
     * @param rolId ID del rol
     * @return Lista de asignaciones del rol (activas e inactivas)
     */
    List<UsuarioRolDTO> obtenerAsignacionesPorRol(Long rolId);

    /**
     * Obtiene todas las asignaciones activas de un rol específico.
     * 
     * @param rolId ID del rol
     * @return Lista de asignaciones activas del rol
     */
    List<UsuarioRolDTO> obtenerAsignacionesActivasPorRol(Long rolId);

    /**
     * Obtiene asignaciones creadas en un rango de fechas.
     * 
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de asignaciones creadas en el rango especificado
     */
    List<UsuarioRolDTO> obtenerAsignacionesPorFechaCreacion(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // ========================================
    // OPERACIONES DE VALIDACIÓN Y VERIFICACIÓN
    // ========================================

    /**
     * Verifica si un usuario tiene un rol específico asignado (activo).
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return true si el usuario tiene el rol activo, false en caso contrario
     */
    boolean usuarioTieneRol(Long usuarioId, Long rolId);

    /**
     * Verifica si un usuario tiene un rol específico por nombre (activo).
     * 
     * @param usuarioId ID del usuario
     * @param nombreRol Nombre del rol
     * @return true si el usuario tiene el rol activo, false en caso contrario
     */
    boolean usuarioTieneRolPorNombre(Long usuarioId, String nombreRol);

    /**
     * Verifica si existe una asignación específica (activa o inactiva).
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return true si existe la asignación, false en caso contrario
     */
    boolean existeAsignacion(Long usuarioId, Long rolId);

    /**
     * Verifica si existe una asignación activa específica.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return true si existe la asignación activa, false en caso contrario
     */
    boolean existeAsignacionActiva(Long usuarioId, Long rolId);

    /**
     * Valida si se puede asignar un rol a un usuario.
     * Verifica reglas de negocio específicas.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return true si se puede asignar, false en caso contrario
     */
    boolean puedeAsignarRol(Long usuarioId, Long rolId);

    /**
     * Valida si se puede desasignar un rol de un usuario.
     * Verifica reglas de negocio específicas.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return true si se puede desasignar, false en caso contrario
     */
    boolean puedeDesasignarRol(Long usuarioId, Long rolId);

    // ========================================
    // OPERACIONES MASIVAS
    // ========================================

    /**
     * Asigna múltiples roles a un usuario.
     * 
     * @param usuarioId ID del usuario
     * @param rolesIds Lista de IDs de roles a asignar
     * @return Lista de asignaciones creadas
     * @throws RuntimeException si algún rol ya está asignado al usuario
     */
    List<UsuarioRolDTO> asignarMultiplesRolesAUsuario(Long usuarioId, List<Long> rolesIds);

    /**
     * Desasigna múltiples roles de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @param rolesIds Lista de IDs de roles a desasignar
     * @throws RuntimeException si algún rol no está asignado al usuario
     */
    void desasignarMultiplesRolesDeUsuario(Long usuarioId, List<Long> rolesIds);

    /**
     * Asigna un rol a múltiples usuarios.
     * 
     * @param rolId ID del rol
     * @param usuariosIds Lista de IDs de usuarios
     * @return Lista de asignaciones creadas
     * @throws RuntimeException si algún usuario ya tiene el rol asignado
     */
    List<UsuarioRolDTO> asignarRolAMultiplesUsuarios(Long rolId, List<Long> usuariosIds);

    /**
     * Desasigna un rol de múltiples usuarios.
     * 
     * @param rolId ID del rol
     * @param usuariosIds Lista de IDs de usuarios
     * @throws RuntimeException si algún usuario no tiene el rol asignado
     */
    void desasignarRolDeMultiplesUsuarios(Long rolId, List<Long> usuariosIds);

    /**
     * Reemplaza todos los roles de un usuario con una nueva lista.
     * 
     * @param usuarioId ID del usuario
     * @param nuevosRolesIds Lista de IDs de los nuevos roles
     * @return Lista de asignaciones resultantes
     */
    List<UsuarioRolDTO> reemplazarRolesDeUsuario(Long usuarioId, List<Long> nuevosRolesIds);

    // ========================================
    // OPERACIONES DE CONVERSIÓN
    // ========================================

    /**
     * Convierte una entidad UsuarioRol a UsuarioRolDTO.
     * 
     * @param usuarioRol Entidad a convertir
     * @return UsuarioRolDTO correspondiente
     */
    UsuarioRolDTO convertirAUsuarioRolDTO(UsuarioRol usuarioRol);

    /**
     * Convierte un UsuarioRolDTO a UsuarioRol.
     * 
     * @param usuarioRolDTO DTO a convertir
     * @return UsuarioRol correspondiente
     */
    UsuarioRol convertirAUsuarioRol(UsuarioRolDTO usuarioRolDTO);

    /**
     * Convierte una lista de entidades UsuarioRol a una lista de UsuarioRolDTO.
     * 
     * @param usuarioRoles Lista de entidades a convertir
     * @return Lista de UsuarioRolDTO correspondiente
     */
    List<UsuarioRolDTO> convertirAUsuarioRolDTOList(List<UsuarioRol> usuarioRoles);

    // ========================================
    // OPERACIONES ESTADÍSTICAS
    // ========================================

    /**
     * Obtiene el número total de asignaciones en el sistema.
     * 
     * @return Número total de asignaciones
     */
    long contarTotalAsignaciones();

    /**
     * Obtiene el número de asignaciones activas en el sistema.
     * 
     * @return Número de asignaciones activas
     */
    long contarAsignacionesActivas();

    /**
     * Obtiene el número de asignaciones inactivas en el sistema.
     * 
     * @return Número de asignaciones inactivas
     */
    long contarAsignacionesInactivas();

    /**
     * Cuenta el número de roles asignados a un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @return Número de roles asignados al usuario
     */
    int contarRolesDeUsuario(Long usuarioId);

    /**
     * Cuenta el número de usuarios que tienen un rol específico.
     * 
     * @param rolId ID del rol
     * @return Número de usuarios con el rol asignado
     */
    int contarUsuariosConRol(Long rolId);

    /**
     * Obtiene estadísticas de asignaciones por rol.
     * 
     * @return Map con el ID del rol como clave y el número de asignaciones como valor
     */
    java.util.Map<Long, Integer> obtenerEstadisticasAsignacionesPorRol();

    /**
     * Obtiene estadísticas de asignaciones por usuario.
     * 
     * @return Map con el ID del usuario como clave y el número de roles como valor
     */
    java.util.Map<Long, Integer> obtenerEstadisticasAsignacionesPorUsuario();

    // ========================================
    // OPERACIONES DE AUDITORÍA
    // ========================================

    /**
     * Obtiene el historial completo de asignaciones de un usuario.
     * Incluye asignaciones activas e inactivas con fechas de cambio.
     * 
     * @param usuarioId ID del usuario
     * @return Lista con el historial de asignaciones del usuario
     */
    List<UsuarioRolDTO> obtenerHistorialAsignacionesUsuario(Long usuarioId);

    /**
     * Obtiene el historial completo de asignaciones de un rol.
     * Incluye asignaciones activas e inactivas con fechas de cambio.
     * 
     * @param rolId ID del rol
     * @return Lista con el historial de asignaciones del rol
     */
    List<UsuarioRolDTO> obtenerHistorialAsignacionesRol(Long rolId);

    /**
     * Obtiene las asignaciones modificadas en un período específico.
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin Fecha de fin del período
     * @return Lista de asignaciones modificadas en el período
     */
    List<UsuarioRolDTO> obtenerAsignacionesModificadasEnPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}

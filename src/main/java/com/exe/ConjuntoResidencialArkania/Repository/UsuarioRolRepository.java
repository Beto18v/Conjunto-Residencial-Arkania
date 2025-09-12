package com.exe.ConjuntoResidencialArkania.Repository;

import com.exe.ConjuntoResidencialArkania.Entity.UsuarioRol;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;
import com.exe.ConjuntoResidencialArkania.Entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de la relación Usuario-Rol en el sistema de conjunto residencial.
 * Contiene consultas personalizadas para casos de uso específicos del manejo de asignaciones
 * de roles a usuarios.
 * 
 * Incluye métodos para búsqueda de asignaciones, consultas por estado,
 * estadísticas de asignación y auditoría de cambios de roles.
 */
@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Long> {

    /**
     * Busca una asignación específica usuario-rol.
     * Útil para verificar si un usuario tiene un rol específico asignado.
     * 
     * @param usuario Usuario a verificar
     * @param rol Rol a verificar
     * @return Optional con la asignación encontrada o vacío si no existe
     */
    Optional<UsuarioRol> findByUsuarioAndRol(UserEntity usuario, RolEntity rol);

    /**
     * Busca una asignación específica usuario-rol que esté activa.
     * Útil para verificar si un usuario tiene un rol específico activo.
     * 
     * @param usuario Usuario a verificar
     * @param rol Rol a verificar
     * @return Optional con la asignación activa encontrada o vacío si no existe
     */
    Optional<UsuarioRol> findByUsuarioAndRolAndActivoTrue(UserEntity usuario, RolEntity rol);

    /**
     * Encuentra todas las asignaciones de roles para un usuario específico.
     * Útil para mostrar todos los roles de un usuario (activos e inactivos).
     * 
     * @param usuario Usuario del cual obtener las asignaciones
     * @return Lista de asignaciones de roles del usuario
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.usuario = :usuario ORDER BY ur.fechaCreacion DESC")
    List<UsuarioRol> findAllByUsuario(@Param("usuario") UserEntity usuario);

    /**
     * Encuentra todas las asignaciones activas de roles para un usuario específico.
     * Útil para mostrar solo los roles activos de un usuario.
     * 
     * @param usuario Usuario del cual obtener las asignaciones activas
     * @return Lista de asignaciones activas de roles del usuario
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.usuario = :usuario AND ur.activo = true ORDER BY ur.fechaCreacion DESC")
    List<UsuarioRol> findByUsuarioAndActivoTrue(@Param("usuario") UserEntity usuario);

    /**
     * Encuentra todas las asignaciones de un rol específico.
     * Útil para ver todos los usuarios que tienen un rol particular.
     * 
     * @param rol Rol del cual obtener las asignaciones
     * @return Lista de asignaciones del rol
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.rol = :rol ORDER BY ur.fechaCreacion DESC")
    List<UsuarioRol> findAllByRol(@Param("rol") RolEntity rol);

    /**
     * Encuentra todas las asignaciones activas de un rol específico.
     * Útil para ver todos los usuarios que tienen un rol particular activo.
     * 
     * @param rol Rol del cual obtener las asignaciones activas
     * @return Lista de asignaciones activas del rol
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.rol = :rol AND ur.activo = true ORDER BY ur.fechaCreacion DESC")
    List<UsuarioRol> findByRolAndActivoTrue(@Param("rol") RolEntity rol);

    /**
     * Encuentra asignaciones por usuario ID y rol ID.
     * Útil para búsquedas directas cuando se conocen los IDs.
     * 
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return Optional con la asignación encontrada o vacío si no existe
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.usuario.usuarioId = :usuarioId AND ur.rol.rolId = :rolId")
    Optional<UsuarioRol> findByUsuarioIdAndRolId(@Param("usuarioId") Long usuarioId, @Param("rolId") Long rolId);

    /**
     * Encuentra asignaciones activas por usuario ID.
     * Útil para obtener roles activos de un usuario conociendo solo su ID.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de asignaciones activas del usuario
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.usuario.usuarioId = :usuarioId AND ur.activo = true ORDER BY ur.fechaCreacion DESC")
    List<UsuarioRol> findActiveByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Encuentra asignaciones activas por rol ID.
     * Útil para obtener usuarios activos con un rol específico conociendo solo el ID del rol.
     * 
     * @param rolId ID del rol
     * @return Lista de asignaciones activas del rol
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.rol.rolId = :rolId AND ur.activo = true ORDER BY ur.fechaCreacion DESC")
    List<UsuarioRol> findActiveByRolId(@Param("rolId") Long rolId);

    /**
     * Encuentra todas las asignaciones activas en el sistema.
     * Útil para reportes generales y estadísticas del sistema.
     * 
     * @return Lista de todas las asignaciones activas
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.activo = true ORDER BY ur.fechaCreacion DESC")
    List<UsuarioRol> findAllActive();

    /**
     * Encuentra todas las asignaciones inactivas en el sistema.
     * Útil para auditoría y revisión de roles desactivados.
     * 
     * @return Lista de todas las asignaciones inactivas
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.activo = false ORDER BY ur.fechaActualizacion DESC")
    List<UsuarioRol> findAllInactive();

    /**
     * Encuentra asignaciones creadas en un rango de fechas.
     * Útil para reportes de asignación de roles en períodos específicos.
     * 
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de asignaciones creadas en el rango especificado
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ORDER BY ur.fechaCreacion DESC")
    List<UsuarioRol> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                               @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Encuentra asignaciones modificadas en un rango de fechas.
     * Útil para auditoría de cambios en asignaciones de roles.
     * 
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de asignaciones modificadas en el rango especificado
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.fechaActualizacion BETWEEN :fechaInicio AND :fechaFin ORDER BY ur.fechaActualizacion DESC")
    List<UsuarioRol> findByFechaActualizacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                     @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Verifica si existe una asignación específica usuario-rol.
     * Útil para validaciones antes de crear nuevas asignaciones.
     * 
     * @param usuario Usuario a verificar
     * @param rol Rol a verificar
     * @return true si existe la asignación, false en caso contrario
     */
    boolean existsByUsuarioAndRol(UserEntity usuario, RolEntity rol);

    /**
     * Verifica si existe una asignación activa específica usuario-rol.
     * Útil para validaciones de roles activos.
     * 
     * @param usuario Usuario a verificar
     * @param rol Rol a verificar
     * @return true si existe la asignación activa, false en caso contrario
     */
    boolean existsByUsuarioAndRolAndActivoTrue(UserEntity usuario, RolEntity rol);

    /**
     * Cuenta el número total de asignaciones activas en el sistema.
     * Útil para estadísticas generales del sistema.
     * 
     * @return Número de asignaciones activas
     */
    @Query("SELECT COUNT(ur) FROM UsuarioRol ur WHERE ur.activo = true")
    Long countAsignacionesActivas();

    /**
     * Cuenta cuántos usuarios tienen un rol específico asignado (activo).
     * Útil para estadísticas de uso de roles.
     * 
     * @param rolId ID del rol
     * @return Número de usuarios con el rol activo
     */
    @Query("SELECT COUNT(ur) FROM UsuarioRol ur WHERE ur.rol.rolId = :rolId AND ur.activo = true")
    Long countUsuariosByRolIdAndActivoTrue(@Param("rolId") Long rolId);

    /**
     * Cuenta cuántos roles activos tiene un usuario específico.
     * Útil para estadísticas de asignaciones por usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Número de roles activos del usuario
     */
    @Query("SELECT COUNT(ur) FROM UsuarioRol ur WHERE ur.usuario.usuarioId = :usuarioId AND ur.activo = true")
    Long countRolesByUsuarioIdAndActivoTrue(@Param("usuarioId") Long usuarioId);

    /**
     * Obtiene estadísticas de asignaciones: nombre del rol y cantidad de usuarios asignados.
     * Útil para reportes de distribución de roles en el sistema.
     * 
     * @return Lista de arrays con [nombre_rol, cantidad_usuarios_activos]
     */
    @Query("SELECT r.nombre, COUNT(ur) FROM UsuarioRol ur JOIN ur.rol r WHERE ur.activo = true GROUP BY r.rolId, r.nombre ORDER BY COUNT(ur) DESC, r.nombre")
    List<Object[]> getEstadisticasAsignaciones();

    /**
     * Encuentra usuarios con múltiples roles activos.
     * Útil para identificar usuarios con privilegios especiales.
     * 
     * @return Lista de asignaciones de usuarios con más de un rol activo
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.activo = true AND ur.usuario IN " +
           "(SELECT ur2.usuario FROM UsuarioRol ur2 WHERE ur2.activo = true GROUP BY ur2.usuario HAVING COUNT(ur2) > 1) " +
           "ORDER BY ur.usuario.usuarioId, ur.fechaCreacion")
    List<UsuarioRol> findUsuariosConMultiplesRoles();

    /**
     * Encuentra asignaciones de roles para usuarios activos solamente.
     * Útil para reportes que excluyan usuarios desactivados.
     * 
     * @return Lista de asignaciones de usuarios activos
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.activo = true AND ur.usuario.activo = true ORDER BY ur.fechaCreacion DESC")
    List<UsuarioRol> findAsignacionesUsuariosActivos();

    /**
     * Encuentra la asignación más reciente de un usuario específico.
     * Útil para mostrar el último rol asignado a un usuario.
     * 
     * @param usuario Usuario del cual obtener la asignación más reciente
     * @return Optional con la asignación más reciente o vacío si no existe
     */
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.usuario = :usuario ORDER BY ur.fechaCreacion DESC LIMIT 1")
    Optional<UsuarioRol> findMostRecentByUsuario(@Param("usuario") UserEntity usuario);
}

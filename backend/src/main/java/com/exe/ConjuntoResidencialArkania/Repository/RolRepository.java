package com.exe.ConjuntoResidencialArkania.Repository;

import com.exe.ConjuntoResidencialArkania.Entity.RolEntity;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de roles en el sistema de conjunto residencial.
 * Contiene consultas personalizadas para casos de uso específicos del dominio de roles.
 * 
 * Incluye métodos para búsqueda por nombre, estado, consultas de permisos
 * y estadísticas de asignación de roles.
 */
@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {

    /**
     * Busca un rol por su nombre único.
     * Es el método principal de búsqueda ya que el nombre es único e identificativo.
     * 
     * @param nombre Nombre del rol (ej: "ADMINISTRADOR", "PROPIETARIO")
     * @return Optional con el rol encontrado o vacío si no existe
     */
    Optional<RolEntity> findByNombre(String nombre);

    /**
     * Busca un rol por nombre ignorando mayúsculas y minúsculas.
     * Útil para búsquedas flexibles desde el frontend.
     * 
     * @param nombre Nombre del rol en cualquier formato de mayúsculas/minúsculas
     * @return Optional con el rol encontrado o vacío si no existe
     */
    Optional<RolEntity> findByNombreIgnoreCase(String nombre);

    /**
     * Encuentra todos los roles activos ordenados por nombre.
     * Útil para listados y asignación de roles en el sistema.
     * 
     * @return Lista de roles activos
     */
    @Query("SELECT r FROM RolEntity r WHERE r.activo = true ORDER BY r.nombre")
    List<RolEntity> findAllRolesActivos();

    /**
     * Encuentra todos los roles inactivos.
     * Útil para administración y reactivación de roles.
     * 
     * @return Lista de roles inactivos
     */
    @Query("SELECT r FROM RolEntity r WHERE r.activo = false ORDER BY r.fechaActualizacion DESC")
    List<RolEntity> findAllRolesInactivos();

    /**
     * Busca roles por descripción (búsqueda parcial).
     * Útil para funciones de búsqueda avanzada en el frontend.
     * 
     * @param descripcion Texto a buscar en la descripción
     * @return Lista de roles que coinciden con la búsqueda
     */
    @Query("SELECT r FROM RolEntity r WHERE LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')) " +
           "AND r.activo = true ORDER BY r.nombre")
    List<RolEntity> findByDescripcionContaining(@Param("descripcion") String descripcion);

    /**
     * Encuentra roles que contienen un permiso específico.
     * Útil para determinar qué roles tienen un permiso particular.
     * 
     * @param permiso Permiso a buscar en la lista de permisos del rol
     * @return Lista de roles que contienen el permiso especificado
     */
    @Query("SELECT r FROM RolEntity r WHERE r.permisos LIKE CONCAT('%', :permiso, '%') AND r.activo = true ORDER BY r.nombre")
    List<RolEntity> findByPermisosContaining(@Param("permiso") String permiso);

    /**
     * Encuentra roles asignados a un usuario específico.
     * Útil para mostrar los roles de un usuario particular.
     * 
     * @param usuario Usuario del cual obtener los roles
     * @return Lista de roles asignados al usuario
     */
    @Query("SELECT r FROM RolEntity r JOIN r.usuarios u WHERE u = :usuario AND r.activo = true ORDER BY r.nombre")
    List<RolEntity> findRolesByUsuario(@Param("usuario") UserEntity usuario);

    /**
     * Encuentra roles que no están asignados a ningún usuario.
     * Útil para identificar roles sin uso o para limpieza del sistema.
     * 
     * @return Lista de roles sin usuarios asignados
     */
    @Query("SELECT r FROM RolEntity r WHERE r.usuarios IS EMPTY AND r.activo = true ORDER BY r.nombre")
    List<RolEntity> findRolesSinUsuarios();

    /**
     * Encuentra roles con más de X usuarios asignados.
     * Útil para identificar roles populares o con muchas asignaciones.
     * 
     * @param minUsuarios Número mínimo de usuarios que debe tener el rol
     * @return Lista de roles con más usuarios que el mínimo especificado
     */
    @Query("SELECT r FROM RolEntity r WHERE SIZE(r.usuarios) >= :minUsuarios AND r.activo = true ORDER BY SIZE(r.usuarios) DESC")
    List<RolEntity> findRolesConMasDeXUsuarios(@Param("minUsuarios") int minUsuarios);

    /**
     * Encuentra roles creados en un rango de fechas.
     * Útil para reportes y auditoría de creación de roles.
     * 
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de roles creados en el rango especificado
     */
    @Query("SELECT r FROM RolEntity r WHERE r.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ORDER BY r.fechaCreacion DESC")
    List<RolEntity> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                              @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Verifica si existe un rol con el nombre especificado.
     * Útil para validaciones antes de crear roles.
     * 
     * @param nombre Nombre del rol a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);

    /**
     * Verifica si existe un rol activo con el nombre especificado.
     * Útil para validaciones específicas de roles activos.
     * 
     * @param nombre Nombre del rol a verificar
     * @return true si existe y está activo, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM RolEntity r WHERE r.nombre = :nombre AND r.activo = true")
    boolean existsByNombreAndActivo(@Param("nombre") String nombre);

    /**
     * Cuenta el número total de roles activos en el sistema.
     * Útil para estadísticas y dashboard administrativo.
     * 
     * @return Número de roles activos
     */
    @Query("SELECT COUNT(r) FROM RolEntity r WHERE r.activo = true")
    Long countRolesActivos();

    /**
     * Cuenta cuántos usuarios tienen asignado un rol específico.
     * Útil para estadísticas de asignación de roles.
     * 
     * @param rolId ID del rol
     * @return Número de usuarios que tienen el rol asignado
     */
    @Query("SELECT COUNT(u) FROM UserEntity u JOIN u.roles r WHERE r.rolId = :rolId AND u.activo = true")
    Long countUsuariosByRolId(@Param("rolId") Long rolId);

    /**
     * Obtiene estadísticas de roles: nombre del rol y cantidad de usuarios asignados.
     * Útil para reportes de uso de roles en el sistema.
     * 
     * @return Lista de arrays con [nombre_rol, cantidad_usuarios]
     */
    @Query("SELECT r.nombre, COUNT(u) FROM RolEntity r LEFT JOIN r.usuarios u WHERE r.activo = true AND (u.activo = true OR u IS NULL) GROUP BY r.rolId, r.nombre ORDER BY COUNT(u) DESC, r.nombre")
    List<Object[]> getEstadisticasRoles();

    /**
     * Encuentra roles ordenados por número de usuarios asignados (descendente).
     * Útil para identificar los roles más utilizados en el sistema.
     * 
     * @return Lista de roles ordenados por popularidad
     */
    @Query("SELECT r FROM RolEntity r WHERE r.activo = true ORDER BY SIZE(r.usuarios) DESC, r.nombre")
    List<RolEntity> findRolesOrderByUsuariosDesc();

    /**
     * Busca roles que tengan permisos nulos o vacíos.
     * Útil para identificar roles que necesitan configuración de permisos.
     * 
     * @return Lista de roles sin permisos configurados
     */
    @Query("SELECT r FROM RolEntity r WHERE (r.permisos IS NULL OR r.permisos = '') AND r.activo = true ORDER BY r.nombre")
    List<RolEntity> findRolesSinPermisos();
}

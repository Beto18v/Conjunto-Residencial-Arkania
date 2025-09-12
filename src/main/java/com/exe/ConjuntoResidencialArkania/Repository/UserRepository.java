package com.exe.ConjuntoResidencialArkania.Repository;

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
 * Repositorio para la gestión de usuarios en el sistema de conjunto residencial.
 * Contiene consultas personalizadas para casos de uso específicos del dominio de usuarios.
 * 
 * Incluye métodos para búsqueda por documento, email, estados de cuenta,
 * consultas por roles y filtros de actividad.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Busca un usuario por su número de documento.
     * Es el método principal de búsqueda ya que el documento es único e inmutable.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @return Optional con el usuario encontrado o vacío si no existe
     */
    Optional<UserEntity> findByNumeroDocumento(String numeroDocumento);

    /**
     * Busca un usuario por su email.
     * Útil para autenticación y recuperación de contraseña.
     * 
     * @param email Email del usuario
     * @return Optional con el usuario encontrado o vacío si no existe
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Busca un usuario por tipo y número de documento.
     * Útil para validaciones específicas según el tipo de documento.
     * 
     * @param tipoDocumento Tipo de documento (CC, CE, TI, PP, NIT)
     * @param numeroDocumento Número de documento
     * @return Optional con el usuario encontrado o vacío si no existe
     */
    Optional<UserEntity> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);

    /**
     * Encuentra todos los usuarios activos ordenados por nombre completo.
     * Útil para listados generales del sistema.
     * 
     * @return Lista de usuarios activos
     */
    @Query("SELECT u FROM UserEntity u WHERE u.activo = true ORDER BY u.nombres, u.apellidos")
    List<UserEntity> findAllUsuariosActivos();

    /**
     * Encuentra todos los usuarios inactivos.
     * Útil para administración y reactivación de cuentas.
     * 
     * @return Lista de usuarios inactivos
     */
    @Query("SELECT u FROM UserEntity u WHERE u.activo = false ORDER BY u.fechaActualizacion DESC")
    List<UserEntity> findAllUsuariosInactivos();

    /**
     * Busca usuarios por nombres o apellidos (búsqueda parcial).
     * Útil para funciones de búsqueda en el frontend.
     * 
     * @param busqueda Texto a buscar en nombres o apellidos
     * @return Lista de usuarios que coinciden con la búsqueda
     */
    @Query("SELECT u FROM UserEntity u WHERE " +
           "LOWER(CONCAT(u.nombres, ' ', u.apellidos)) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "ORDER BY u.nombres, u.apellidos")
    List<UserEntity> findByNombresOrApellidosContaining(@Param("busqueda") String busqueda);

    /**
     * Encuentra usuarios que tienen un rol específico.
     * Útil para listados por tipo de usuario (administradores, propietarios, etc.).
     * 
     * @param rol Entidad del rol a buscar
     * @return Lista de usuarios con el rol especificado
     */
    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r = :rol AND u.activo = true ORDER BY u.nombres, u.apellidos")
    List<UserEntity> findByRol(@Param("rol") RolEntity rol);

    /**
     * Encuentra usuarios por nombre de rol.
     * Útil cuando solo se conoce el nombre del rol.
     * 
     * @param nombreRol Nombre del rol (ej: "ADMINISTRADOR", "PROPIETARIO")
     * @return Lista de usuarios con el rol especificado
     */
    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.nombre = :nombreRol AND u.activo = true ORDER BY u.nombres, u.apellidos")
    List<UserEntity> findByNombreRol(@Param("nombreRol") String nombreRol);

    /**
     * Encuentra usuarios que no tienen ningún rol asignado.
     * Útil para identificar usuarios que necesitan asignación de roles.
     * 
     * @return Lista de usuarios sin roles
     */
    @Query("SELECT u FROM UserEntity u WHERE u.roles IS EMPTY ORDER BY u.fechaCreacion DESC")
    List<UserEntity> findUsuariosSinRoles();

    /**
     * Encuentra usuarios creados en un rango de fechas.
     * Útil para reportes y estadísticas de registro.
     * 
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de usuarios creados en el rango especificado
     */
    @Query("SELECT u FROM UserEntity u WHERE u.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ORDER BY u.fechaCreacion DESC")
    List<UserEntity> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                               @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Encuentra usuarios con múltiples roles.
     * Útil para identificar usuarios con privilegios especiales.
     * 
     * @return Lista de usuarios con más de un rol
     */
    @Query("SELECT u FROM UserEntity u WHERE SIZE(u.roles) > 1 ORDER BY SIZE(u.roles) DESC, u.nombres")
    List<UserEntity> findUsuariosConMultiplesRoles();

    /**
     * Verifica si existe un usuario con el número de documento especificado.
     * Útil para validaciones antes de crear usuarios.
     * 
     * @param numeroDocumento Número de documento a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByNumeroDocumento(String numeroDocumento);

    /**
     * Verifica si existe un usuario con el email especificado.
     * Útil para validaciones antes de crear usuarios.
     * 
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Cuenta el número total de usuarios activos en el sistema.
     * Útil para estadísticas y dashboard administrativo.
     * 
     * @return Número de usuarios activos
     */
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.activo = true")
    Long countUsuariosActivos();

    /**
     * Cuenta usuarios por tipo de documento.
     * Útil para estadísticas demográficas.
     * 
     * @param tipoDocumento Tipo de documento a contar
     * @return Número de usuarios con el tipo de documento especificado
     */
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.tipoDocumento = :tipoDocumento AND u.activo = true")
    Long countByTipoDocumento(@Param("tipoDocumento") String tipoDocumento);

    /**
     * Encuentra usuarios con teléfono registrado.
     * Útil para comunicaciones directas y contacto de emergencia.
     * 
     * @return Lista de usuarios que tienen teléfono registrado
     */
    @Query("SELECT u FROM UserEntity u WHERE u.telefono IS NOT NULL AND u.telefono != '' AND u.activo = true ORDER BY u.nombres, u.apellidos")
    List<UserEntity> findUsuariosConTelefono();
}

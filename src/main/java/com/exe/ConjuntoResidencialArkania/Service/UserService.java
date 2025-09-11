package com.exe.ConjuntoResidencialArkania.Service;

import com.exe.ConjuntoResidencialArkania.DTO.UserDTO;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de usuarios en el sistema de conjunto residencial.
 * 
 * Define las operaciones de negocio disponibles para el manejo de usuarios,
 * incluyendo CRUD básico, búsquedas específicas, validaciones de negocio
 * y operaciones relacionadas con roles.
 * 
 * Esta interfaz sigue el patrón de separación de responsabilidades,
 * manteniendo la lógica de negocio separada de la lógica de acceso a datos.
 */
public interface UserService {

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    /**
     * Crea un nuevo usuario en el sistema.
     * Valida que no exista otro usuario con el mismo documento o email.
     * Encripta la contraseña antes de almacenar.
     * 
     * @param userDTO Datos del usuario a crear
     * @return UserDTO con la información del usuario creado (incluyendo ID generado)
     * @throws IllegalArgumentException si los datos son inválidos
     * @throws RuntimeException si ya existe un usuario con el mismo documento o email
     */
    UserDTO crearUsuario(UserDTO userDTO);

    /**
     * Obtiene un usuario por su ID.
     * 
     * @param usuarioId ID del usuario a buscar
     * @return Optional con el usuario encontrado o vacío si no existe
     */
    Optional<UserDTO> obtenerUsuarioPorId(Long usuarioId);

    /**
     * Obtiene un usuario por su número de documento.
     * 
     * @param numeroDocumento Número de documento del usuario
     * @return Optional con el usuario encontrado o vacío si no existe
     */
    Optional<UserDTO> obtenerUsuarioPorDocumento(String numeroDocumento);

    /**
     * Obtiene un usuario por su email.
     * Útil para autenticación y recuperación de contraseña.
     * 
     * @param email Email del usuario
     * @return Optional con el usuario encontrado o vacío si no existe
     */
    Optional<UserDTO> obtenerUsuarioPorEmail(String email);

    /**
     * Actualiza la información de un usuario existente.
     * No permite cambiar el número de documento ni el email si ya están en uso por otro usuario.
     * 
     * @param usuarioId ID del usuario a actualizar
     * @param userDTO Nuevos datos del usuario
     * @return UserDTO con la información actualizada
     * @throws RuntimeException si el usuario no existe
     * @throws IllegalArgumentException si se intenta cambiar a un email/documento ya en uso
     */
    UserDTO actualizarUsuario(Long usuarioId, UserDTO userDTO);

    /**
     * Actualiza parcialmente un usuario (solo los campos proporcionados).
     * 
     * @param usuarioId ID del usuario a actualizar
     * @param userDTO Datos parciales del usuario (campos null se ignoran)
     * @return UserDTO con la información actualizada
     * @throws RuntimeException si el usuario no existe
     */
    UserDTO actualizarUsuarioParcial(Long usuarioId, UserDTO userDTO);

    /**
     * Elimina lógicamente un usuario (marca como inactivo).
     * No elimina físicamente para mantener integridad referencial.
     * 
     * @param usuarioId ID del usuario a eliminar
     * @throws RuntimeException si el usuario no existe
     */
    void eliminarUsuario(Long usuarioId);

    /**
     * Reactiva un usuario previamente eliminado (marca como activo).
     * 
     * @param usuarioId ID del usuario a reactivar
     * @throws RuntimeException si el usuario no existe
     */
    void reactivarUsuario(Long usuarioId);

    // ========================================
    // OPERACIONES DE LISTADO Y BÚSQUEDA
    // ========================================

    /**
     * Obtiene todos los usuarios del sistema.
     * 
     * @return Lista de todos los usuarios (activos e inactivos)
     */
    List<UserDTO> obtenerTodosLosUsuarios();

    /**
     * Obtiene todos los usuarios activos ordenados por nombre.
     * 
     * @return Lista de usuarios activos
     */
    List<UserDTO> obtenerUsuariosActivos();

    /**
     * Obtiene todos los usuarios inactivos.
     * 
     * @return Lista de usuarios inactivos
     */
    List<UserDTO> obtenerUsuariosInactivos();

    /**
     * Busca usuarios por nombres o apellidos (búsqueda parcial).
     * 
     * @param busqueda Texto a buscar en nombres o apellidos
     * @return Lista de usuarios que coinciden con la búsqueda
     */
    List<UserDTO> buscarUsuariosPorNombre(String busqueda);

    /**
     * Obtiene usuarios que tienen un rol específico.
     * 
     * @param nombreRol Nombre del rol (ej: "ADMINISTRADOR", "PROPIETARIO")
     * @return Lista de usuarios con el rol especificado
     */
    List<UserDTO> obtenerUsuariosPorRol(String nombreRol);

    /**
     * Obtiene usuarios que no tienen ningún rol asignado.
     * 
     * @return Lista de usuarios sin roles
     */
    List<UserDTO> obtenerUsuariosSinRoles();

    /**
     * Obtiene usuarios con múltiples roles asignados.
     * 
     * @return Lista de usuarios con más de un rol
     */
    List<UserDTO> obtenerUsuariosConMultiplesRoles();

    /**
     * Obtiene usuarios creados en un rango de fechas.
     * 
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de usuarios creados en el rango especificado
     */
    List<UserDTO> obtenerUsuariosPorFechaCreacion(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // ========================================
    // OPERACIONES DE VALIDACIÓN Y VERIFICACIÓN
    // ========================================

    /**
     * Verifica si existe un usuario con el número de documento especificado.
     * 
     * @param numeroDocumento Número de documento a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existeUsuarioPorDocumento(String numeroDocumento);

    /**
     * Verifica si existe un usuario con el email especificado.
     * 
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existeUsuarioPorEmail(String email);

    /**
     * Valida si un usuario puede ser eliminado.
     * Verifica dependencias y reglas de negocio.
     * 
     * @param usuarioId ID del usuario a validar
     * @return true si puede ser eliminado, false en caso contrario
     */
    boolean puedeEliminarUsuario(Long usuarioId);

    // ========================================
    // OPERACIONES DE SEGURIDAD
    // ========================================

    /**
     * Cambia la contraseña de un usuario.
     * Valida la contraseña actual antes de cambiarla.
     * 
     * @param usuarioId ID del usuario
     * @param passwordActual Contraseña actual del usuario
     * @param passwordNueva Nueva contraseña
     * @throws RuntimeException si la contraseña actual no es correcta
     * @throws IllegalArgumentException si la nueva contraseña no cumple los criterios
     */
    void cambiarPassword(Long usuarioId, String passwordActual, String passwordNueva);

    /**
     * Restablece la contraseña de un usuario (para administradores).
     * 
     * @param usuarioId ID del usuario
     * @param passwordNueva Nueva contraseña
     * @throws RuntimeException si el usuario no existe
     */
    void restablecerPassword(Long usuarioId, String passwordNueva);

    /**
     * Valida las credenciales de un usuario para autenticación.
     * 
     * @param email Email del usuario
     * @param password Contraseña sin encriptar
     * @return Optional con el usuario si las credenciales son válidas
     */
    Optional<UserDTO> validarCredenciales(String email, String password);

    // ========================================
    // OPERACIONES DE CONVERSIÓN
    // ========================================

    /**
     * Convierte una entidad UserEntity a UserDTO.
     * 
     * @param userEntity Entidad a convertir
     * @return UserDTO correspondiente
     */
    UserDTO convertirAUserDTO(UserEntity userEntity);

    /**
     * Convierte un UserDTO a UserEntity.
     * 
     * @param userDTO DTO a convertir
     * @return UserEntity correspondiente
     */
    UserEntity convertirAUserEntity(UserDTO userDTO);

    /**
     * Convierte una lista de entidades UserEntity a una lista de UserDTO.
     * 
     * @param userEntities Lista de entidades a convertir
     * @return Lista de UserDTO correspondiente
     */
    List<UserDTO> convertirAUserDTOList(List<UserEntity> userEntities);

    // ========================================
    // OPERACIONES ESTADÍSTICAS
    // ========================================

    /**
     * Obtiene el número total de usuarios registrados.
     * 
     * @return Número total de usuarios
     */
    long contarTotalUsuarios();

    /**
     * Obtiene el número de usuarios activos.
     * 
     * @return Número de usuarios activos
     */
    long contarUsuariosActivos();

    /**
     * Obtiene el número de usuarios inactivos.
     * 
     * @return Número de usuarios inactivos
     */
    long contarUsuariosInactivos();

    /**
     * Obtiene estadísticas de usuarios por tipo de documento.
     * 
     * @return Map con el tipo de documento como clave y el número de usuarios como valor
     */
    java.util.Map<String, Long> obtenerEstadisticasPorTipoDocumento();
}

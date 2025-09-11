package com.exe.ConjuntoResidencialArkania.Impl;

import com.exe.ConjuntoResidencialArkania.DTO.UserDTO;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;
import com.exe.ConjuntoResidencialArkania.Entity.RolEntity;
import com.exe.ConjuntoResidencialArkania.Repository.UserRepository;
import com.exe.ConjuntoResidencialArkania.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la gestión de usuarios en el sistema de conjunto residencial.
 * 
 * Esta clase implementa todas las operaciones definidas en UserService,
 * manejando la lógica de negocio relacionada con usuarios, incluyendo:
 * - CRUD completo de usuarios
 * - Validaciones de negocio específicas
 * - Gestión de seguridad y contraseñas
 * - Conversiones entre DTOs y Entidades
 * - Operaciones estadísticas y de auditoría
 * 
 * Utiliza transacciones para asegurar la consistencia de datos
 * y maneja todas las validaciones de negocio necesarias.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // ========================================
    // MÉTODOS AUXILIARES DE ENCRIPTACIÓN
    // ========================================

    /**
     * Encripta una contraseña usando SHA-256.
     * Nota: En producción se recomienda usar BCrypt o Argon2.
     * 
     * @param password Contraseña en texto plano
     * @return Contraseña encriptada
     */
    private String encriptarPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

    /**
     * Valida si una contraseña coincide con su versión encriptada.
     * 
     * @param passwordTextoPlano Contraseña en texto plano
     * @param passwordEncriptada Contraseña encriptada almacenada
     * @return true si coinciden, false en caso contrario
     */
    private boolean validarPassword(String passwordTextoPlano, String passwordEncriptada) {
        return encriptarPassword(passwordTextoPlano).equals(passwordEncriptada);
    }

    // ========================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================

    @Override
    public UserDTO crearUsuario(UserDTO userDTO) {
        // Validar que no exista un usuario con el mismo documento
        if (existeUsuarioPorDocumento(userDTO.getNumeroDocumento())) {
            throw new RuntimeException("Ya existe un usuario con el número de documento: " + userDTO.getNumeroDocumento());
        }

        // Validar que no exista un usuario con el mismo email
        if (existeUsuarioPorEmail(userDTO.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + userDTO.getEmail());
        }

        // Validar que la contraseña esté presente para usuarios nuevos
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria para usuarios nuevos");
        }

        // Convertir DTO a Entity
        UserEntity userEntity = convertirAUserEntity(userDTO);
        
        // Encriptar la contraseña
        userEntity.setPassword(encriptarPassword(userDTO.getPassword()));
        
        // Establecer valores por defecto
        userEntity.setActivo(true);
        userEntity.setRoles(new HashSet<>());

        // Guardar el usuario
        UserEntity usuarioGuardado = userRepository.save(userEntity);

        return convertirAUserDTO(usuarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> obtenerUsuarioPorId(Long usuarioId) {
        return userRepository.findById(usuarioId)
                .map(this::convertirAUserDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> obtenerUsuarioPorDocumento(String numeroDocumento) {
        return userRepository.findByNumeroDocumento(numeroDocumento)
                .map(this::convertirAUserDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> obtenerUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertirAUserDTO);
    }

    @Override
    public UserDTO actualizarUsuario(Long usuarioId, UserDTO userDTO) {
        UserEntity userEntity = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        // Validar cambio de email si es diferente
        if (!userEntity.getEmail().equals(userDTO.getEmail())) {
            if (existeUsuarioPorEmail(userDTO.getEmail())) {
                throw new IllegalArgumentException("Ya existe un usuario con el email: " + userDTO.getEmail());
            }
        }

        // Validar cambio de documento si es diferente
        if (!userEntity.getNumeroDocumento().equals(userDTO.getNumeroDocumento())) {
            if (existeUsuarioPorDocumento(userDTO.getNumeroDocumento())) {
                throw new IllegalArgumentException("Ya existe un usuario con el documento: " + userDTO.getNumeroDocumento());
            }
        }

        // Actualizar campos (excepto password que se maneja por separado)
        if (userDTO.getTipoDocumento() != null) {
            userEntity.setTipoDocumento(userDTO.getTipoDocumento());
        }
        if (userDTO.getNumeroDocumento() != null) {
            userEntity.setNumeroDocumento(userDTO.getNumeroDocumento());
        }
        if (userDTO.getNombres() != null) {
            userEntity.setNombres(userDTO.getNombres());
        }
        if (userDTO.getApellidos() != null) {
            userEntity.setApellidos(userDTO.getApellidos());
        }
        if (userDTO.getEmail() != null) {
            userEntity.setEmail(userDTO.getEmail());
        }
        if (userDTO.getTelefono() != null) {
            userEntity.setTelefono(userDTO.getTelefono());
        }
        if (userDTO.getActivo() != null) {
            userEntity.setActivo(userDTO.getActivo());
        }

        UserEntity usuarioActualizado = userRepository.save(userEntity);
        return convertirAUserDTO(usuarioActualizado);
    }

    @Override
    public UserDTO actualizarUsuarioParcial(Long usuarioId, UserDTO userDTO) {
        return actualizarUsuario(usuarioId, userDTO);
    }

    @Override
    public void eliminarUsuario(Long usuarioId) {
        UserEntity userEntity = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        // Eliminación lógica
        userEntity.setActivo(false);
        userRepository.save(userEntity);
    }

    @Override
    public void reactivarUsuario(Long usuarioId) {
        UserEntity userEntity = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        userEntity.setActivo(true);
        userRepository.save(userEntity);
    }

    // ========================================
    // OPERACIONES DE LISTADO Y BÚSQUEDA
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerTodosLosUsuarios() {
        List<UserEntity> usuarios = userRepository.findAll();
        return convertirAUserDTOList(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerUsuariosActivos() {
        List<UserEntity> usuarios = userRepository.findAllUsuariosActivos();
        return convertirAUserDTOList(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerUsuariosInactivos() {
        List<UserEntity> usuarios = userRepository.findAllUsuariosInactivos();
        return convertirAUserDTOList(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> buscarUsuariosPorNombre(String busqueda) {
        List<UserEntity> usuarios = userRepository.findByNombresOrApellidosContaining(busqueda);
        return convertirAUserDTOList(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerUsuariosPorRol(String nombreRol) {
        List<UserEntity> usuarios = userRepository.findByNombreRol(nombreRol);
        return convertirAUserDTOList(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerUsuariosSinRoles() {
        List<UserEntity> usuarios = userRepository.findUsuariosSinRoles();
        return convertirAUserDTOList(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerUsuariosConMultiplesRoles() {
        List<UserEntity> usuarios = userRepository.findUsuariosConMultiplesRoles();
        return convertirAUserDTOList(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerUsuariosPorFechaCreacion(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<UserEntity> usuarios = userRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
        return convertirAUserDTOList(usuarios);
    }

    // ========================================
    // OPERACIONES DE VALIDACIÓN Y VERIFICACIÓN
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuarioPorDocumento(String numeroDocumento) {
        return userRepository.existsByNumeroDocumento(numeroDocumento);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeEliminarUsuario(Long usuarioId) {
        UserEntity usuario = userRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return false;
        }

        // Verificar si es un usuario administrador crítico
        boolean esAdministrador = usuario.getRoles().stream()
                .anyMatch(rol -> "ADMINISTRADOR".equals(rol.getNombre()));

        // Si es administrador, verificar que no sea el único
        if (esAdministrador) {
            long cantidadAdministradores = userRepository.findByNombreRol("ADMINISTRADOR").size();
            return cantidadAdministradores > 1;
        }

        return true;
    }

    // ========================================
    // OPERACIONES DE SEGURIDAD
    // ========================================

    @Override
    public void cambiarPassword(Long usuarioId, String passwordActual, String passwordNueva) {
        UserEntity userEntity = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        // Validar contraseña actual
        if (!validarPassword(passwordActual, userEntity.getPassword())) {
            throw new RuntimeException("La contraseña actual no es correcta");
        }

        // Validar nueva contraseña
        if (passwordNueva == null || passwordNueva.length() < 8) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres");
        }

        // Encriptar y guardar nueva contraseña
        userEntity.setPassword(encriptarPassword(passwordNueva));
        userRepository.save(userEntity);
    }

    @Override
    public void restablecerPassword(Long usuarioId, String passwordNueva) {
        UserEntity userEntity = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        // Validar nueva contraseña
        if (passwordNueva == null || passwordNueva.length() < 8) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres");
        }

        // Encriptar y guardar nueva contraseña
        userEntity.setPassword(encriptarPassword(passwordNueva));
        userRepository.save(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> validarCredenciales(String email, String password) {
        Optional<UserEntity> usuarioOpt = userRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            UserEntity usuario = usuarioOpt.get();
            if (usuario.getActivo() && validarPassword(password, usuario.getPassword())) {
                return Optional.of(convertirAUserDTO(usuario));
            }
        }
        
        return Optional.empty();
    }

    // ========================================
    // OPERACIONES DE CONVERSIÓN
    // ========================================

    @Override
    public UserDTO convertirAUserDTO(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsuarioId(userEntity.getUsuarioId());
        userDTO.setTipoDocumento(userEntity.getTipoDocumento());
        userDTO.setNumeroDocumento(userEntity.getNumeroDocumento());
        userDTO.setNombres(userEntity.getNombres());
        userDTO.setApellidos(userEntity.getApellidos());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setTelefono(userEntity.getTelefono());
        userDTO.setActivo(userEntity.getActivo());
        userDTO.setFechaCreacion(userEntity.getFechaCreacion());
        userDTO.setFechaActualizacion(userEntity.getFechaActualizacion());

        // Convertir roles a Set<String>
        if (userEntity.getRoles() != null) {
            Set<String> rolesNombres = userEntity.getRoles().stream()
                    .map(RolEntity::getNombre)
                    .collect(Collectors.toSet());
            userDTO.setRoles(rolesNombres);
        }

        // No incluir password por seguridad
        userDTO.setPassword(null);

        return userDTO;
    }

    @Override
    public UserEntity convertirAUserEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsuarioId(userDTO.getUsuarioId());
        userEntity.setTipoDocumento(userDTO.getTipoDocumento());
        userEntity.setNumeroDocumento(userDTO.getNumeroDocumento());
        userEntity.setNombres(userDTO.getNombres());
        userEntity.setApellidos(userDTO.getApellidos());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setTelefono(userDTO.getTelefono());
        userEntity.setActivo(userDTO.getActivo() != null ? userDTO.getActivo() : true);
        userEntity.setFechaCreacion(userDTO.getFechaCreacion());
        userEntity.setFechaActualizacion(userDTO.getFechaActualizacion());

        // La conversión de roles se maneja en el contexto específico donde se necesite
        userEntity.setRoles(new HashSet<>());

        return userEntity;
    }

    @Override
    public List<UserDTO> convertirAUserDTOList(List<UserEntity> userEntities) {
        if (userEntities == null) {
            return new ArrayList<>();
        }

        return userEntities.stream()
                .map(this::convertirAUserDTO)
                .collect(Collectors.toList());
    }

    // ========================================
    // OPERACIONES ESTADÍSTICAS
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public long contarTotalUsuarios() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarUsuariosActivos() {
        return userRepository.findAllUsuariosActivos().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarUsuariosInactivos() {
        return userRepository.findAllUsuariosInactivos().size();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> obtenerEstadisticasPorTipoDocumento() {
        List<UserEntity> usuarios = userRepository.findAll();
        
        return usuarios.stream()
                .collect(Collectors.groupingBy(
                    UserEntity::getTipoDocumento,
                    Collectors.counting()
                ));
    }
}

package com.exe.ConjuntoResidencialArkania.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa los roles del sistema de gestión residencial.
 * Esta clase mapea la tabla 'roles' en la base de datos y define
 * los diferentes tipos de permisos y accesos que pueden tener los usuarios.
 * 
 * Los roles típicos incluyen: ADMINISTRADOR, PROPIETARIO, ARRENDATARIO, 
 * VIGILANTE, CONSERJE, etc.
 * 
 * Un rol puede ser asignado a múltiples usuarios (relación many-to-many).
 */
@Entity
@Table(name = "roles")
@Data // Lombok: genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Lombok: genera constructor sin parámetros
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros
public class RolEntity {

    /**
     * Identificador único del rol en la base de datos.
     * Se genera automáticamente usando IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long rolId;

    /**
     * Nombre único del rol en el sistema.
     * Ejemplos: ADMINISTRADOR, PROPIETARIO, RESIDENTE, VIGILANTE, VISITANTE.
     * Se almacena en mayúsculas para consistencia.
     */
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del rol debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[A-Z_]+$", message = "El nombre del rol debe estar en mayúsculas y usar solo letras y guiones bajos")
    private String nombre;

    /**
     * Descripción detallada del rol y sus responsabilidades.
     * Proporciona información clara sobre qué puede hacer un usuario con este rol.
     */
    @Column(name = "descripcion", length = 255)
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    /**
     * Indica si el rol está activo en el sistema.
     * Por defecto es true. Se puede desactivar para suspender su uso.
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Lista de permisos específicos asociados al rol.
     * Formato JSON: ["READ_USERS", "WRITE_USERS", "DELETE_APARTMENTS", etc.]
     * Permite granularidad en los permisos sin crear tablas adicionales.
     */
    @Column(name = "permisos", columnDefinition = "TEXT")
    private String permisos;

    /**
     * Relación many-to-many con usuarios a través de la tabla intermedia usuario_rol.
     * Un rol puede ser asignado a múltiples usuarios.
     * 
     * mappedBy: Indica que la relación es manejada por la propiedad 'roles' en UserEntity.
     * FetchType.LAZY: Los usuarios se cargan bajo demanda para optimizar rendimiento.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<UserEntity> usuarios = new HashSet<>();

    /**
     * Constructor personalizado para crear un rol con datos básicos.
     * Útil para la inicialización de roles del sistema.
     * 
     * @param nombre Nombre único del rol
     * @param descripcion Descripción del rol
     * @param nivelJerarquia Nivel jerárquico del rol
     * @param esSistema Si es un rol predefinido del sistema
     */
    public RolEntity(String nombre, String descripcion) {
        this.nombre = nombre.toUpperCase(); // Asegurar que esté en mayúsculas
        this.descripcion = descripcion;
        this.activo = true;
        this.usuarios = new HashSet<>();
    }

    /**
     * Método de utilidad para agregar un usuario al rol.
     * Maneja la relación bidireccional entre Rol y Usuario.
     * 
     * @param usuario El usuario a agregar al rol
     */
    public void agregarUsuario(UserEntity usuario) {
        this.usuarios.add(usuario);
        usuario.getRoles().add(this);
    }

    /**
     * Método de utilidad para remover un usuario del rol.
     * Maneja la relación bidireccional entre Rol y Usuario.
     * 
     * @param usuario El usuario a remover del rol
     */
    public void removerUsuario(UserEntity usuario) {
        this.usuarios.remove(usuario);
        usuario.getRoles().remove(this);
    }

    /**
     * Método de utilidad para activar el rol.
     */
    public void activar() {
        this.activo = true;
    }

    /**
     * Método de utilidad para verificar si el rol tiene un permiso específico.
     * 
     * @param permiso El permiso a verificar
     * @return true si el rol tiene el permiso, false en caso contrario
     */
    public boolean tienePermiso(String permiso) {
        if (this.permisos == null || this.permisos.isEmpty()) {
            return false;
        }
        return this.permisos.contains(permiso);
    }


    /**
     * Método de utilidad para contar cuántos usuarios tienen este rol.
     * 
     * @return Número de usuarios con este rol
     */
    public int contarUsuarios() {
        return this.usuarios.size();
    }


    /**
     * Sobrescribe el método setNombre para asegurar que siempre esté en mayúsculas.
     * 
     * @param nombre El nombre del rol
     */
    public void setNombre(String nombre) {
        this.nombre = nombre != null ? nombre.toUpperCase() : null;
    }
}

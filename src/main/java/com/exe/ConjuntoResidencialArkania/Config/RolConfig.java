package com.exe.ConjuntoResidencialArkania.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Configuración específica para la gestión de roles en el sistema de conjunto residencial.
 * 
 * Esta clase centraliza todas las configuraciones relacionadas con roles, incluyendo:
 * - Definición de roles predeterminados del sistema
 * - Configuración de permisos granulares por rol
 * - Patrones de validación para nombres de roles
 * - Configuración de jerarquías de roles
 * - Configuración de políticas de asignación de roles
 * 
 * Utiliza Spring Configuration para inyectar estas configuraciones
 * en toda la aplicación de manera consistente.
 */
@Configuration
public class RolConfig {

    // ========================================
    // CONFIGURACIÓN DE ROLES PREDETERMINADOS
    // ========================================

    /**
     * Lista de roles predeterminados del sistema.
     * Estos roles se pueden crear automáticamente durante la inicialización.
     * 
     * @return Lista de nombres de roles predeterminados
     */
    @Bean
    public List<String> rolesPredeterminados() {
        return Arrays.asList(
            "ADMINISTRADOR",
            "PROPIETARIO", 
            "ARRENDATARIO",
            "RESIDENTE",
            "VIGILANTE",
            "CONSERJE",
            "VISITANTE",
            "PROVEEDOR"
        );
    }

    /**
     * Descripciones de los roles predeterminados.
     * 
     * @return Mapa con descripciones de cada rol predeterminado
     */
    @Bean
    public Map<String, String> descripcionesRolesPredeterminados() {
        Map<String, String> descripciones = new HashMap<>();
        descripciones.put("ADMINISTRADOR", "Administrador del sistema con acceso completo a todas las funcionalidades");
        descripciones.put("PROPIETARIO", "Propietario de apartamento con derechos sobre su propiedad y áreas comunes");
        descripciones.put("ARRENDATARIO", "Persona que arrienda un apartamento con permisos limitados");
        descripciones.put("RESIDENTE", "Persona que reside en el conjunto pero no es propietario ni arrendatario principal");
        descripciones.put("VIGILANTE", "Personal de seguridad del conjunto residencial");
        descripciones.put("CONSERJE", "Personal de mantenimiento y servicios generales");
        descripciones.put("VISITANTE", "Persona temporal con acceso limitado al conjunto");
        descripciones.put("PROVEEDOR", "Empresa o persona que presta servicios al conjunto residencial");
        return descripciones;
    }

    // ========================================
    // CONFIGURACIÓN DE PERMISOS POR ROL
    // ========================================

    /**
     * Permisos asignados al rol ADMINISTRADOR.
     * 
     * @return Lista de permisos para administradores
     */
    @Bean
    public List<String> permisosAdministrador() {
        return Arrays.asList(
            "MANAGE_USERS", "MANAGE_ROLES", "MANAGE_APARTMENTS", 
            "MANAGE_PARKING", "MANAGE_COMMON_AREAS", "MANAGE_RESERVATIONS",
            "MANAGE_CORRESPONDENCE", "MANAGE_REQUESTS", "MANAGE_NEWS",
            "VIEW_REPORTS", "MANAGE_SYSTEM", "MANAGE_SECURITY",
            "APPROVE_REQUESTS", "MANAGE_PAYMENTS", "MANAGE_MAINTENANCE"
        );
    }

    /**
     * Permisos asignados al rol PROPIETARIO.
     * 
     * @return Lista de permisos para propietarios
     */
    @Bean
    public List<String> permisosPropietario() {
        return Arrays.asList(
            "VIEW_OWN_APARTMENT", "MANAGE_OWN_RESIDENTS", "VIEW_OWN_CORRESPONDENCE",
            "CREATE_REQUESTS", "VIEW_OWN_REQUESTS", "RESERVE_COMMON_AREAS",
            "VIEW_ANNOUNCEMENTS", "VIEW_OWN_PAYMENTS", "MANAGE_OWN_PARKING",
            "VOTE_ASSEMBLIES", "VIEW_MEETING_MINUTES", "CONTACT_ADMINISTRATION"
        );
    }

    /**
     * Permisos asignados al rol ARRENDATARIO.
     * 
     * @return Lista de permisos para arrendatarios
     */
    @Bean
    public List<String> permisosArrendatario() {
        return Arrays.asList(
            "VIEW_OWN_APARTMENT", "VIEW_OWN_CORRESPONDENCE", "CREATE_REQUESTS",
            "VIEW_OWN_REQUESTS", "RESERVE_COMMON_AREAS", "VIEW_ANNOUNCEMENTS",
            "CONTACT_ADMINISTRATION"
        );
    }

    /**
     * Permisos asignados al rol RESIDENTE.
     * 
     * @return Lista de permisos para residentes
     */
    @Bean
    public List<String> permisosResidente() {
        return Arrays.asList(
            "VIEW_APARTMENT_INFO", "VIEW_ANNOUNCEMENTS", "CREATE_BASIC_REQUESTS",
            "VIEW_OWN_REQUESTS", "CONTACT_ADMINISTRATION"
        );
    }

    /**
     * Permisos asignados al rol VIGILANTE.
     * 
     * @return Lista de permisos para vigilantes
     */
    @Bean
    public List<String> permisosVigilante() {
        return Arrays.asList(
            "MANAGE_VISITORS", "VIEW_RESIDENTS", "CREATE_INCIDENTS",
            "VIEW_SECURITY_REPORTS", "MANAGE_ACCESS_CONTROL",
            "VIEW_EMERGENCY_CONTACTS", "REGISTER_DELIVERIES"
        );
    }

    /**
     * Permisos asignados al rol CONSERJE.
     * 
     * @return Lista de permisos para conserjes
     */
    @Bean
    public List<String> permisosConserje() {
        return Arrays.asList(
            "MANAGE_MAINTENANCE", "VIEW_MAINTENANCE_REQUESTS", "CREATE_WORK_ORDERS",
            "MANAGE_COMMON_AREA_MAINTENANCE", "VIEW_RESIDENT_CONTACTS",
            "REGISTER_DELIVERIES", "MANAGE_CLEANING_SCHEDULES"
        );
    }

    /**
     * Permisos asignados al rol VISITANTE.
     * 
     * @return Lista de permisos para visitantes
     */
    @Bean
    public List<String> permisosVisitante() {
        return Arrays.asList(
            "VIEW_PUBLIC_AREAS", "CONTACT_HOST", "VIEW_BASIC_INFO"
        );
    }

    /**
     * Permisos asignados al rol PROVEEDOR.
     * 
     * @return Lista de permisos para proveedores
     */
    @Bean
    public List<String> permisosProveedor() {
        return Arrays.asList(
            "VIEW_ASSIGNED_WORK", "UPDATE_WORK_STATUS", "UPLOAD_WORK_EVIDENCE",
            "CONTACT_ADMINISTRATION", "VIEW_WORK_SCHEDULES"
        );
    }

    // ========================================
    // CONFIGURACIÓN DE VALIDACIONES Y PATRONES
    // ========================================

    /**
     * Patrón para validar nombres de roles.
     * Los nombres deben estar en mayúsculas y usar solo letras y guiones bajos.
     * 
     * @return Pattern compilado para validación de nombres de rol
     */
    @Bean
    public Pattern patronNombreRol() {
        return Pattern.compile("^[A-Z_]+$");
    }

    /**
     * Longitud mínima para nombres de roles.
     * 
     * @return Longitud mínima requerida
     */
    @Bean
    public Integer longitudMinimaNombreRol() {
        return 3;
    }

    /**
     * Longitud máxima para nombres de roles.
     * 
     * @return Longitud máxima permitida
     */
    @Bean
    public Integer longitudMaximaNombreRol() {
        return 50;
    }

    /**
     * Longitud máxima para descripciones de roles.
     * 
     * @return Longitud máxima permitida para descripciones
     */
    @Bean
    public Integer longitudMaximaDescripcionRol() {
        return 255;
    }

    // ========================================
    // CONFIGURACIÓN DE JERARQUÍAS Y POLÍTICAS
    // ========================================

    /**
     * Jerarquía de roles del sistema (de mayor a menor nivel).
     * Útil para validaciones de permisos y restricciones.
     * 
     * @return Lista ordenada de roles por jerarquía
     */
    @Bean
    public List<String> jerarquiaRoles() {
        return Arrays.asList(
            "ADMINISTRADOR",
            "PROPIETARIO",
            "ARRENDATARIO", 
            "RESIDENTE",
            "VIGILANTE",
            "CONSERJE",
            "PROVEEDOR",
            "VISITANTE"
        );
    }

    /**
     * Roles que pueden ser asignados por administradores.
     * 
     * @return Lista de roles que puede asignar un administrador
     */
    @Bean
    public List<String> rolesAsignablesPorAdministrador() {
        return Arrays.asList(
            "PROPIETARIO", "ARRENDATARIO", "RESIDENTE", 
            "VIGILANTE", "CONSERJE", "PROVEEDOR", "VISITANTE"
        );
    }

    /**
     * Roles que pueden ser asignados por propietarios.
     * 
     * @return Lista de roles que puede asignar un propietario
     */
    @Bean
    public List<String> rolesAsignablesPorPropietario() {
        return Arrays.asList("RESIDENTE", "VISITANTE");
    }

    /**
     * Roles que requieren aprobación administrativa para ser asignados.
     * 
     * @return Lista de roles que requieren aprobación
     */
    @Bean
    public List<String> rolesRequierenAprobacion() {
        return Arrays.asList("ADMINISTRADOR", "VIGILANTE", "CONSERJE", "PROVEEDOR");
    }

    // ========================================
    // CONFIGURACIÓN DE ESTADOS Y VALORES POR DEFECTO
    // ========================================

    /**
     * Estado por defecto para nuevos roles.
     * 
     * @return true si los roles son activos por defecto
     */
    @Bean
    public Boolean rolActivoPorDefecto() {
        return true;
    }

    /**
     * Rol por defecto para nuevos usuarios del sistema.
     * 
     * @return Nombre del rol asignado por defecto
     */
    @Bean
    public String rolPorDefectoNuevosUsuarios() {
        return "VISITANTE";
    }

    /**
     * Número máximo de roles que puede tener un usuario simultáneamente.
     * 
     * @return Número máximo de roles por usuario
     */
    @Bean
    public Integer maxRolesPorUsuario() {
        return 3;
    }

    /**
     * Indica si se permite la auto-asignación de roles.
     * 
     * @return true si se permite auto-asignación
     */
    @Bean
    public Boolean permitirAutoAsignacionRoles() {
        return false;
    }

    /**
     * Tiempo de expiración para asignaciones temporales de roles (en días).
     * 
     * @return Días de validez para roles temporales
     */
    @Bean
    public Integer diasExpiracionRolesTemporales() {
        return 30;
    }

    /**
     * Indica si se debe auditar cambios en roles.
     * 
     * @return true si se debe mantener auditoría
     */
    @Bean
    public Boolean auditarCambiosRoles() {
        return true;
    }

    /**
     * Indica si se debe notificar cambios de roles a los usuarios.
     * 
     * @return true si se debe notificar
     */
    @Bean
    public Boolean notificarCambiosRoles() {
        return true;
    }
}

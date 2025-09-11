package com.exe.ConjuntoResidencialArkania.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Configuración específica para la gestión de asignaciones Usuario-Rol en el sistema de conjunto residencial.
 * 
 * Esta clase centraliza todas las configuraciones relacionadas con la relación many-to-many
 * entre usuarios y roles, incluyendo:
 * - Políticas de asignación y desasignación de roles
 * - Configuración de restricciones de asignación
 * - Configuración de auditoría y notificaciones
 * - Configuración de validaciones de negocio
 * - Configuración de estados y transiciones
 * 
 * Utiliza Spring Configuration para inyectar estas configuraciones
 * en toda la aplicación de manera consistente.
 */
@Configuration
public class UsuarioRolConfig {

    // ========================================
    // CONFIGURACIÓN DE POLÍTICAS DE ASIGNACIÓN
    // ========================================

    /**
     * Número máximo de usuarios que pueden tener el mismo rol simultáneamente.
     * Útil para roles exclusivos como ADMINISTRADOR.
     * 
     * @return Mapa con límites por rol
     */
    @Bean
    public Map<String, Integer> limiteUsuariosPorRol() {
        Map<String, Integer> limites = new HashMap<>();
        limites.put("ADMINISTRADOR", 5);      // Máximo 5 administradores
        limites.put("PROPIETARIO", 1000);     // Hasta 1000 propietarios
        limites.put("ARRENDATARIO", 1000);    // Hasta 1000 arrendatarios
        limites.put("RESIDENTE", 2000);       // Hasta 2000 residentes
        limites.put("VIGILANTE", 10);         // Máximo 10 vigilantes
        limites.put("CONSERJE", 5);           // Máximo 5 conserjes
        limites.put("VISITANTE", 10000);      // Hasta 10000 visitantes
        limites.put("PROVEEDOR", 100);        // Hasta 100 proveedores
        return limites;
    }

    /**
     * Roles que son mutuamente excluyentes (un usuario no puede tener ambos).
     * 
     * @return Mapa de roles con sus respectivos roles excluyentes
     */
    @Bean
    public Map<String, List<String>> rolesExcluyentes() {
        Map<String, List<String>> exclusiones = new HashMap<>();
        exclusiones.put("PROPIETARIO", Arrays.asList("ARRENDATARIO"));
        exclusiones.put("ARRENDATARIO", Arrays.asList("PROPIETARIO"));
        exclusiones.put("VISITANTE", Arrays.asList("PROPIETARIO", "ARRENDATARIO", "RESIDENTE"));
        return exclusiones;
    }

    /**
     * Roles que requieren tener otros roles como prerequisito.
     * 
     * @return Mapa de roles con sus prerequisitos
     */
    @Bean
    public Map<String, List<String>> rolesPrerequisitos() {
        Map<String, List<String>> prerequisitos = new HashMap<>();
        // Un residente puede requerir ser primero propietario o arrendatario
        prerequisitos.put("RESIDENTE", Arrays.asList("PROPIETARIO", "ARRENDATARIO"));
        return prerequisitos;
    }

    /**
     * Roles que pueden ser asignados automáticamente en ciertas circunstancias.
     * 
     * @return Lista de roles de asignación automática
     */
    @Bean
    public List<String> rolesAsignacionAutomatica() {
        return Arrays.asList("VISITANTE", "RESIDENTE");
    }

    // ========================================
    // CONFIGURACIÓN DE RESTRICCIONES TEMPORALES
    // ========================================

    /**
     * Roles que tienen expiración automática (en días).
     * 
     * @return Mapa con días de expiración por rol
     */
    @Bean
    public Map<String, Integer> diasExpiracionPorRol() {
        Map<String, Integer> expiraciones = new HashMap<>();
        expiraciones.put("VISITANTE", 7);      // Visitantes expiran en 7 días
        expiraciones.put("PROVEEDOR", 90);     // Proveedores expiran en 90 días
        return expiraciones;
    }

    /**
     * Roles que requieren renovación periódica.
     * 
     * @return Lista de roles que requieren renovación
     */
    @Bean
    public List<String> rolesRequierenRenovacion() {
        return Arrays.asList("VIGILANTE", "CONSERJE", "PROVEEDOR");
    }

    /**
     * Frecuencia de renovación por rol (en días).
     * 
     * @return Mapa con días entre renovaciones por rol
     */
    @Bean
    public Map<String, Integer> diasRenovacionPorRol() {
        Map<String, Integer> renovaciones = new HashMap<>();
        renovaciones.put("VIGILANTE", 365);    // Renovación anual
        renovaciones.put("CONSERJE", 365);     // Renovación anual
        renovaciones.put("PROVEEDOR", 180);    // Renovación semestral
        return renovaciones;
    }

    // ========================================
    // CONFIGURACIÓN DE AUDITORÍA Y NOTIFICACIONES
    // ========================================

    /**
     * Roles cuyas asignaciones requieren auditoría especial.
     * 
     * @return Lista de roles que requieren auditoría especial
     */
    @Bean
    public List<String> rolesRequierenAuditoriaEspecial() {
        return Arrays.asList("ADMINISTRADOR", "VIGILANTE", "CONSERJE");
    }

    /**
     * Roles cuyas asignaciones requieren aprobación de múltiples niveles.
     * 
     * @return Lista de roles que requieren aprobación múltiple
     */
    @Bean
    public List<String> rolesRequierenAprobacionMultiple() {
        return Arrays.asList("ADMINISTRADOR");
    }

    /**
     * Roles que requieren notificación inmediata al ser asignados.
     * 
     * @return Lista de roles que requieren notificación inmediata
     */
    @Bean
    public List<String> rolesNotificacionInmediata() {
        return Arrays.asList("ADMINISTRADOR", "VIGILANTE", "CONSERJE");
    }

    /**
     * Roles que requieren confirmación del usuario para ser asignados.
     * 
     * @return Lista de roles que requieren confirmación
     */
    @Bean
    public List<String> rolesRequierenConfirmacionUsuario() {
        return Arrays.asList("PROPIETARIO", "ARRENDATARIO", "VIGILANTE", "CONSERJE");
    }

    // ========================================
    // CONFIGURACIÓN DE ESTADOS Y TRANSICIONES
    // ========================================

    /**
     * Estado por defecto para nuevas asignaciones usuario-rol.
     * 
     * @return true si las asignaciones son activas por defecto
     */
    @Bean
    public Boolean asignacionActivaPorDefecto() {
        return true;
    }

    /**
     * Roles que inician en estado inactivo hasta ser aprobados.
     * 
     * @return Lista de roles que inician inactivos
     */
    @Bean
    public List<String> rolesInicianInactivos() {
        return Arrays.asList("ADMINISTRADOR", "VIGILANTE", "CONSERJE", "PROVEEDOR");
    }

    /**
     * Tiempo de gracia antes de desactivar asignaciones expiradas (en días).
     * 
     * @return Días de gracia antes de desactivación
     */
    @Bean
    public Integer diasGraciaAntesDesactivacion() {
        return 7;
    }

    /**
     * Indica si se debe mantener historial de asignaciones eliminadas.
     * 
     * @return true si se debe mantener historial
     */
    @Bean
    public Boolean mantenerHistorialAsignaciones() {
        return true;
    }

    // ========================================
    // CONFIGURACIÓN DE VALIDACIONES DE NEGOCIO
    // ========================================

    /**
     * Número máximo de asignaciones simultáneas del mismo rol a un usuario.
     * 
     * @return Número máximo de asignaciones iguales
     */
    @Bean
    public Integer maxAsignacionesIgualesPorUsuario() {
        return 1; // Un usuario no puede tener el mismo rol asignado múltiples veces
    }

    /**
     * Número máximo de cambios de rol permitidos por usuario por mes.
     * 
     * @return Número máximo de cambios mensuales
     */
    @Bean
    public Integer maxCambiosRolPorUsuarioPorMes() {
        return 5;
    }

    /**
     * Roles que no pueden ser removidos una vez asignados.
     * 
     * @return Lista de roles permanentes
     */
    @Bean
    public List<String> rolesPermanentes() {
        return Arrays.asList("PROPIETARIO"); // Un propietario siempre es propietario
    }

    /**
     * Roles que pueden ser auto-removidos por el usuario.
     * 
     * @return Lista de roles de auto-remoción
     */
    @Bean
    public List<String> rolesAutoRemovibles() {
        return Arrays.asList("VISITANTE");
    }

    // ========================================
    // CONFIGURACIÓN DE OPERACIONES MASIVAS
    // ========================================

    /**
     * Número máximo de asignaciones que se pueden procesar en una operación masiva.
     * 
     * @return Número máximo de asignaciones por lote
     */
    @Bean
    public Integer maxAsignacionesPorLote() {
        return 100;
    }

    /**
     * Tiempo máximo de espera para operaciones de asignación masiva (en segundos).
     * 
     * @return Segundos máximos de espera
     */
    @Bean
    public Integer timeoutOperacionesMasivas() {
        return 300; // 5 minutos
    }

    /**
     * Indica si se debe usar procesamiento asíncrono para operaciones masivas.
     * 
     * @return true si se debe usar procesamiento asíncrono
     */
    @Bean
    public Boolean usarProcesamientoAsincrono() {
        return true;
    }

    // ========================================
    // CONFIGURACIÓN DE REPORTES Y ESTADÍSTICAS
    // ========================================

    /**
     * Indica si se debe generar reportes automáticos de asignaciones.
     * 
     * @return true si se deben generar reportes automáticos
     */
    @Bean
    public Boolean generarReportesAutomaticos() {
        return true;
    }

    /**
     * Frecuencia de generación de reportes automáticos (en días).
     * 
     * @return Días entre reportes automáticos
     */
    @Bean
    public Integer diasEntreReportesAutomaticos() {
        return 30; // Reportes mensuales
    }

    /**
     * Indica si se debe calcular estadísticas en tiempo real.
     * 
     * @return true si se deben calcular estadísticas en tiempo real
     */
    @Bean
    public Boolean calcularEstadisticasTiempoReal() {
        return false; // Para optimizar rendimiento
    }

    /**
     * Número de días para mantener estadísticas históricas.
     * 
     * @return Días de retención de estadísticas
     */
    @Bean
    public Integer diasRetencionEstadisticas() {
        return 365; // Un año de estadísticas
    }
}

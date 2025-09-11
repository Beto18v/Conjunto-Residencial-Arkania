package com.exe.ConjuntoResidencialArkania.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Configuración específica para la gestión de usuarios en el sistema de conjunto residencial.
 * 
 * Esta clase centraliza todas las configuraciones relacionadas con usuarios, incluyendo:
 * - Configuración de encriptación de contraseñas
 * - Patrones de validación para documentos
 * - Configuración de tipos de documento permitidos
 * - Configuración de políticas de contraseñas
 * - Configuración de formatos de datos de usuario
 * 
 * Utiliza Spring Configuration para inyectar estas configuraciones
 * en toda la aplicación de manera consistente.
 */
@Configuration
public class UserConfig {

    // ========================================
    // CONFIGURACIÓN DE SEGURIDAD Y CONTRASEÑAS
    // ========================================

    /**
     * Configuración del algoritmo de encriptación para contraseñas.
     * Se utiliza SHA-256 como algoritmo base para mantener compatibilidad
     * con la implementación actual del sistema.
     * 
     * @return Nombre del algoritmo de encriptación
     */
    @Bean
    public String algoritmoEncriptacion() {
        return "SHA-256";
    }

    // ========================================
    // CONFIGURACIÓN DE TIPOS DE DOCUMENTO
    // ========================================

    /**
     * Lista de tipos de documento válidos en el sistema.
     * 
     * @return Lista inmutable de tipos de documento permitidos
     */
    @Bean
    public List<String> tiposDocumentoPermitidos() {
        return Arrays.asList("CC", "CE", "TI", "PP", "NIT");
    }

    /**
     * Descripciones legibles de los tipos de documento.
     * 
     * @return Lista de descripciones correspondientes a los tipos de documento
     */
    @Bean
    public List<String> descripcionesTiposDocumento() {
        return Arrays.asList(
            "Cédula de Ciudadanía",
            "Cédula de Extranjería", 
            "Tarjeta de Identidad",
            "Pasaporte",
            "Número de Identificación Tributaria"
        );
    }

    // ========================================
    // CONFIGURACIÓN DE PATRONES DE VALIDACIÓN
    // ========================================

    /**
     * Patrón para validar números de cédula de ciudadanía colombiana.
     * 
     * @return Pattern compilado para validación de CC
     */
    @Bean
    public Pattern patronCedulaCiudadania() {
        return Pattern.compile("^[1-9]\\d{6,9}$"); // 7 a 10 dígitos, no empieza en 0
    }

    /**
     * Patrón para validar números de cédula de extranjería.
     * 
     * @return Pattern compilado para validación de CE
     */
    @Bean
    public Pattern patronCedulaExtranjeria() {
        return Pattern.compile("^[1-9]\\d{6,9}$"); // Similar a CC pero puede tener formato especial
    }

    /**
     * Patrón para validar tarjetas de identidad (menores de edad).
     * 
     * @return Pattern compilado para validación de TI
     */
    @Bean
    public Pattern patronTarjetaIdentidad() {
        return Pattern.compile("^[1-9]\\d{9,10}$"); // 10 u 11 dígitos para TI
    }

    /**
     * Patrón para validar números de pasaporte.
     * 
     * @return Pattern compilado para validación de PP
     */
    @Bean
    public Pattern patronPasaporte() {
        return Pattern.compile("^[A-Z]{2}[0-9]{6,8}$"); // 2 letras seguidas de 6-8 números
    }

    /**
     * Patrón para validar NIT (empresas).
     * 
     * @return Pattern compilado para validación de NIT
     */
    @Bean
    public Pattern patronNIT() {
        return Pattern.compile("^[1-9]\\d{8}-[0-9]{1}$"); // 9 dígitos-1 dígito verificador
    }

    // ========================================
    // CONFIGURACIÓN DE POLÍTICAS DE CONTRASEÑAS
    // ========================================

    /**
     * Configuración de longitud mínima para contraseñas.
     * 
     * @return Longitud mínima requerida
     */
    @Bean
    public Integer longitudMinimaPassword() {
        return 8;
    }

    /**
     * Configuración de longitud máxima para contraseñas.
     * 
     * @return Longitud máxima permitida
     */
    @Bean
    public Integer longitudMaximaPassword() {
        return 100;
    }

    /**
     * Indica si se requieren números en las contraseñas.
     * 
     * @return true si son requeridos números
     */
    @Bean
    public Boolean passwordRequiereNumeros() {
        return true;
    }

    /**
     * Indica si se requieren caracteres especiales en las contraseñas.
     * 
     * @return true si son requeridos caracteres especiales
     */
    @Bean
    public Boolean passwordRequiereCaracteresEspeciales() {
        return true;
    }

    /**
     * Indica si se requieren letras mayúsculas en las contraseñas.
     * 
     * @return true si son requeridas mayúsculas
     */
    @Bean
    public Boolean passwordRequiereMayusculas() {
        return true;
    }

    /**
     * Indica si se requieren letras minúsculas en las contraseñas.
     * 
     * @return true si son requeridas minúsculas
     */
    @Bean
    public Boolean passwordRequiereMinusculas() {
        return true;
    }

    // ========================================
    // CONFIGURACIÓN DE FORMATOS Y LÍMITES
    // ========================================

    /**
     * Longitud máxima para nombres de usuario.
     * 
     * @return Longitud máxima permitida para nombres
     */
    @Bean
    public Integer longitudMaximaNombres() {
        return 50;
    }

    /**
     * Longitud máxima para apellidos de usuario.
     * 
     * @return Longitud máxima permitida para apellidos
     */
    @Bean
    public Integer longitudMaximaApellidos() {
        return 50;
    }

    /**
     * Longitud máxima para emails de usuario.
     * 
     * @return Longitud máxima permitida para emails
     */
    @Bean
    public Integer longitudMaximaEmail() {
        return 150;
    }

    /**
     * Longitud máxima para números de teléfono.
     * 
     * @return Longitud máxima permitida para teléfonos
     */
    @Bean
    public Integer longitudMaximaTelefono() {
        return 15;
    }

    /**
     * Patrón para validar formato de email.
     * 
     * @return Pattern compilado para validación de email
     */
    @Bean
    public Pattern patronEmail() {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Patrón para validar formato de teléfono colombiano.
     * 
     * @return Pattern compilado para validación de teléfono
     */
    @Bean
    public Pattern patronTelefono() {
        return Pattern.compile("^(\\+57)?[3][0-9]{9}$|^[6][0-9]{7}$"); // Móvil o fijo colombiano
    }

    // ========================================
    // CONFIGURACIÓN DE ESTADOS Y VALORES POR DEFECTO
    // ========================================

    /**
     * Estado por defecto para nuevos usuarios.
     * 
     * @return true si los usuarios son activos por defecto
     */
    @Bean
    public Boolean usuarioActivoPorDefecto() {
        return true;
    }

    /**
     * Indica si se debe verificar email al crear usuario.
     * 
     * @return true si se requiere verificación de email
     */
    @Bean
    public Boolean requiereVerificacionEmail() {
        return false; // Cambiar a true en producción si se implementa verificación
    }

    /**
     * Tiempo de expiración para tokens de verificación (en horas).
     * 
     * @return Horas de validez del token
     */
    @Bean
    public Integer horasExpiracionTokenVerificacion() {
        return 24;
    }

    /**
     * Número máximo de intentos de login fallidos antes de bloquear cuenta.
     * 
     * @return Número máximo de intentos
     */
    @Bean
    public Integer maxIntentosFallidosLogin() {
        return 5;
    }

    /**
     * Tiempo de bloqueo de cuenta en minutos tras exceder intentos fallidos.
     * 
     * @return Minutos de bloqueo
     */
    @Bean
    public Integer minutosBloqueoTrasIntentosFallidos() {
        return 30;
    }
}

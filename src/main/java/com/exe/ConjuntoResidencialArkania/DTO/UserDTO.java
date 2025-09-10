package com.exe.ConjuntoResidencialArkania.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO (Data Transfer Object) para la transferencia de datos de Usuario.
 * 
 * Esta clase se utiliza para:
 * - Transferir datos entre el frontend y backend
 * - Evitar exponer directamente las entidades UserEntity
 * - Validar datos de entrada en las peticiones HTTP
 * - Estructurar respuestas JSON de manera controlada
 * 
 * No incluye información sensible como contraseñas hasheadas
 * y proporciona validaciones específicas para cada campo.
 */
@Data // Lombok: genera getters, setters, toString, equals y hashCode automáticamente
@NoArgsConstructor // Lombok: genera constructor sin parámetros para deserialización JSON
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros
public class UserDTO {

    /**
     * Identificador único del usuario.
     * Se incluye para operaciones de actualización y referencia.
     * Es null cuando se crea un nuevo usuario.
     */
    private Long usuarioId;

    /**
     * Tipo de documento de identidad del usuario.
     * Valores permitidos: CC (Cédula), CE (Cédula Extranjera), 
     * TI (Tarjeta Identidad), PP (Pasaporte), NIT (Para empresas).
     */
    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "^(CC|CE|TI|PP|NIT)$", 
             message = "Tipo de documento debe ser: CC, CE, TI, PP o NIT")
    private String tipoDocumento;

    /**
     * Número de documento de identidad del usuario.
     * Debe ser único en el sistema y se usa como identificador principal.
     */
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(min = 6, max = 20, message = "El número de documento debe tener entre 6 y 20 caracteres")
    private String numeroDocumento;

    /**
     * Nombres del usuario.
     * Campo requerido para identificación personal.
     */
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 50, message = "Los nombres deben tener entre 2 y 50 caracteres")
    private String nombres;

    /**
     * Apellidos del usuario.
     * Campo requerido para identificación personal.
     */
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 50, message = "Los apellidos deben tener entre 2 y 50 caracteres")
    private String apellidos;

    /**
     * Correo electrónico del usuario.
     * Se usa para autenticación y notificaciones del sistema.
     * Debe ser único en el sistema.
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;

    /**
     * Número de teléfono del usuario.
     * Campo opcional para comunicación directa.
     */
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", 
             message = "El teléfono solo puede contener números, +, -, espacios y paréntesis")
    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    private String telefono;

    /**
     * Contraseña del usuario (solo para creación/actualización).
     * No se incluye en las respuestas por seguridad.
     * Se valida solo cuando se proporciona (crear o cambiar contraseña).
     */
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    /**
     * Estado de activación de la cuenta del usuario.
     * true = cuenta activa, false = cuenta suspendida.
     */
    private Boolean activo;

    /**
     * Lista de nombres de roles asignados al usuario.
     * Se usa una representación simplificada (solo nombres) 
     * en lugar de objetos RolDTO completos para evitar referencias circulares.
     */
    private Set<String> roles;

    /**
     * Constructor para crear un DTO con datos básicos del usuario.
     * Útil para respuestas que no requieren todos los campos.
     * 
     * @param usuarioId ID del usuario
     * @param numeroDocumento Número de documento
     * @param nombres Nombres del usuario
     * @param apellidos Apellidos del usuario
     * @param email Email del usuario
     * @param activo Estado de la cuenta
     */
    public UserDTO(Long usuarioId, String numeroDocumento, String nombres, 
                   String apellidos, String email, Boolean activo) {
        this.usuarioId = usuarioId;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.activo = activo;
    }

    /**
     * Método de utilidad para obtener el nombre completo del usuario.
     * 
     * @return String con nombres y apellidos concatenados
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}

package com.exe.ConjuntoResidencialArkania.DTO;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * DTO para transferir datos de CorrespondenciaEntity
 * hacia las capas superiores (controllers o clientes).
 */
@Data
public class CorrespondenciaDTO {
    
    private Long idCorrespondencia;

    // Solo los IDs de usuarios para evitar exponer la entidad completa
    private Long registradoPor;
    private Long destinatario;
    private Long retiradoPor;
    
    // Información básica adicional de usuarios (si se quiere mostrar en la UI)
    private String registradoPorNombre;
    private String destinatarioNombre;
    private String retiradoPorNombre;

    // Relación con apartamento
    private Long apartamentoId;

    private String tipo;
    private LocalDateTime fechaRecepcion;
    private LocalDateTime fechaEntrega;
    private String estado;
    private String observaciones;
    private LocalDateTime crearCorrespondencia;
    private LocalDateTime actualizarCorrespondencia;

}

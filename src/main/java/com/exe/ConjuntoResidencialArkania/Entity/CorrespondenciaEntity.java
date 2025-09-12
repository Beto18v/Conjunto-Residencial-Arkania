package com.exe.ConjuntoResidencialArkania.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad que representa la correspondencia recibida en el conjunto residencial.
 * Maneja la información de paquetes, cartas y documentos que llegan a portería
 * y su estado de entrega a los residentes.
 */
@Entity
@Table(name = "correspondencias")
@Data

public class CorrespondenciaEntity {
    
    // Identificador único de la correspondencia
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCorrespondencia;
    
    // Quien registra en portería (personal de seguridad).
    @ManyToOne
    @JoinColumn(name = "registradoPor", nullable = false)
    private UserEntity registradoPor;
    
    // A quien va dirigida la correspondencia.
    @ManyToOne
    @JoinColumn(name = "destinatario", nullable = false)
    private UserEntity destinatario;

    // Quien recoge la correspondencia (puede ser el mismo destinatario o alguien autorizado).
    @ManyToOne
    @JoinColumn(name = "retiradoPor")
    private UserEntity retiradoPor;

    // Relacion con apartamento (apartamento del destinatario, opcional)
    @ManyToOne
    @JoinColumn(name = "apartamento")
    private ApartamentoEntity apartamento;
    
    // Tipo de la correspondencia
    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo; // Paquete, documento, etc.

    @Column(name = "fechaRecepcion", nullable = false)
    private LocalDateTime fechaRecepcion;

    @Column(name = "fechaEntrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado; // Pendiente, Entregado.

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    private LocalDateTime crearCorrespondencia;
    private LocalDateTime actualizarCorrespondencia;

    // Enun para el tipo de la correspondencia
    public enum Tipo {
        PAQUETE,
        DOCUMENTO,
        OTRO
    }

    // Enum para el estado de la correspondencia
    public enum Estado {
        PENDIENTE,
        ENTREGADA
    }

}

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
    
    // Relacion con Usuario (quien recibe la correspondencia en porteria)
    @ManyToOne
    @JoinColumn(name = "recibido_por", nullable = false)
    private UserEntity recibidoPor;

    // Relacion con Usuario (a quien va dirigida la correspondencia)
    @ManyToOne
    @JoinColumn(name = "destinatario", nullable = false)
    private UserEntity destinatario;

    // Relacion con apartamento (apartamento del destinatario, opcional)
    @ManyToOne
    @JoinColumn(name = "apartamento")
    private ApartamentoEntity apartamento;
    
    // Detalles de la correspondencia
    @Column(nullable = false)
    private String tipo; // Paquete, carta, documento, etc.

    @Column(nullable = false)
    private LocalDateTime fechaRecepcion;

    @Column(nullable = false)
    private LocalDateTime fechaEntrega;

    @Column(nullable = false)
    private String estado; // Pendiente, Entregado.

}

package com.exe.ConjuntoResidencialArkania.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "correspondencias")
@Data

public class CorrespondenciaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCorrespondencia;
    
    // Relacion con Usuario (quien recibe la correspondencia en porteria)
    @ManyToOne
    @JoinColumn(name = "recibido_por_id")
    private UserEntity recibidoPor;
    
    // Relacion con Usuario (a quien va dirigida la correspondencia)
    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private UserEntity destinatario;

    // Relacion con apartamento (apartamento del destinatario, opcional)
    @ManyToOne
    @JoinColumn(name = "idApartamento", nullable = true)
    private ApartamentoEntity apartamento;
    
    @Column(nullable = false)
    private String tipo; // Paquete, carta, documento, etc.

    @Column(nullable = false)
    private LocalDateTime fechaRecepcion;

    @Column(nullable = true)
    private LocalDateTime fechaEntrega;

    @Column(nullable = false)
    private String estado; // Pendiente, Entregado.

}

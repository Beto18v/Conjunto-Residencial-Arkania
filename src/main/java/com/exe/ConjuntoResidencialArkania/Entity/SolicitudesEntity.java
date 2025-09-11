package com.exe.ConjuntoResidencialArkania.Entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor // Lombok: genera constructor sin parámetros para deserialización JSON
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros

public class SolicitudesEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_solicitud") 
    @NotNull(message = "El id de la solicitud es obligatorio") 
    long idSolicitud;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserEntity usuario;


    @NotNull(message = "El tipo de solicitud es obligatorio")
    @Enumerated(jakarta.persistence.EnumType.STRING)
    @Column(name = "tipo_solicitud", nullable = false, length = 50)
    TipoSolicitud tipoSolicitud;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    @Column(name = "descripcion", nullable = false, length = 500)
    String descripcion;

    @NotNull(message = "El estado de la solicitud es obligatorio")
    @Enumerated(jakarta.persistence.EnumType.STRING)
    @Column(name = "estado_solicitud", nullable = false, length = 50)
    EstadoSolicitud estadoSolicitud;

    @NotNull(message = "La fecha de creación es obligatoria")
    @Column(name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_resolucion")
    LocalDateTime fechaResolucion;
    
    public enum EstadoSolicitud {
        pendiente,
        en_proceso,
        resuelta, 
        rechazada
        // Agrega más estados según sea necesario, como "cancelada", etc.
    }
    public enum TipoSolicitud {
        mantenimiento,
        queja,
        reserva,
        consulta
    }
    
    //Logica para verificar que estado y tipo de solicitud sean lleanados
    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
    if (tipoSolicitud == null) {
        throw new IllegalArgumentException("El tipo de solicitud es obligatorio");
    }
    this.tipoSolicitud = tipoSolicitud;
}
    public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
        if (estadoSolicitud == null) {
            throw new IllegalArgumentException("El estado de la solicitud es obligatorio");
        }
        this.estadoSolicitud = estadoSolicitud;
    }
}

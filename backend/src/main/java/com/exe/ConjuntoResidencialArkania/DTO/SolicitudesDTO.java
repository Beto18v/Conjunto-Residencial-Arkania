package com.exe.ConjuntoResidencialArkania.DTO;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor // Lombok: genera constructor sin parámetros para deserialización JSON
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros

public class SolicitudesDTO {   
    long idSolicitud;

    long usuarioId;

    @NotNull(message = "El tipo de solicitud es obligatorio")
    TipoSolicitud tipoSolicitud;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    String descripcion;

    @NotNull(message = "El estado de la solicitud es obligatorio")
    EstadoSolicitud estadoSolicitud;

    @NotNull(message = "La fecha de creación es obligatoria")
    LocalDateTime fechaCreacion;

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

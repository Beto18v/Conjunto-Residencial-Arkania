package com.exe.ConjuntoResidencialArkania.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  

public class AreasComunesDTO {
    long idAreaComun;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    String descripcion;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(min = 5, max = 200, message = "La ubicación debe tener entre 5 y 200 caracteres")
    String ubicacion;

    @Max(value = 1000, message = "La capacidad máxima es 1000")
    @Min(value = 1, message = "La capacidad mínima es 1")
    Integer capacidadMaxima;

    @NotBlank(message = "El horario de funcionamiento es obligatorio")
    @Size(min = 5, max = 200, message = "El horario de funcionamiento debe tener entre 5 y 200 caracteres")
    String horarioFuncionamiento;
    
    @Pattern(regexp = "^(|activa|inactiva)$", message = "Estado debe ser: activa o inactiva")
    EstadoArea estado;

    public enum EstadoArea {
        activa,
        inactiva
    }
     public void setEstado(EstadoArea estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado del área común es obligatorio");
        }
        this.estado = estado;
    }
}

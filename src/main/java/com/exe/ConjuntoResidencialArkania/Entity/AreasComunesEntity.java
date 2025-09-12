package com.exe.ConjuntoResidencialArkania.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "areas_comunes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreasComunesEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_area_comun")
    private Long idAreaComun;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    @Column(name = "descripcion", nullable = false, length = 1000)
    String descripcion;
    
    @NotBlank(message = "La ubicación es obligatoria")
    @Size(min = 5, max = 200, message = "La ubicación debe tener entre 5 y 200 caracteres")
    @Column(name = "ubicacion", nullable = false, length = 200)
    String ubicacion;

    @Max(value = 1000, message = "La capacidad máxima es 1000")
    @Min(value = 1, message = "La capacidad mínima es 1")
    @Column(name = "capacidad_maxima", nullable = false)
    Integer capacidadMaxima;

    @NotBlank(message = "El horario de funcionamiento es obligatorio")
    @Size(min = 5, max = 200, message = "El horario de funcionamiento debe tener entre 5 y 200 caracteres")
    @Column(name = "horario_funcionamiento", nullable = false)
    String horarioFuncionamiento;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
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


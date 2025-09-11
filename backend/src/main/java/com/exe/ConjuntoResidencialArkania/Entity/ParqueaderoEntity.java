package com.exe.ConjuntoResidencialArkania.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parqueaderos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParqueaderoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parqueadero_id")
    private Long parqueaderoId;

    @Column(name = "tipo_rol", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de rol es obligatorio.")
    private TipoRolParqueadero tipoRol; //RESIDENTE o VISITANTE

    @Column(name = "numero", nullable = false, unique = true, length = 15)
    @NotBlank(message = "El número del parqueadero es obligatorio.")
    @Size(min = 1, max = 15, message = "El número debe tener entre 1 y 15 caracteres.")
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UserEntity usuario;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoParqueadero estado = EstadoParqueadero.LIBRE;

    public enum TipoRolParqueadero {
        RESIDENTE,
        VISITANTE
    }

    public enum EstadoParqueadero {
        LIBRE,
        OCUPADO,
        INACTIVO
    }
}

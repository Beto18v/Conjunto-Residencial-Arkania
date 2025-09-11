package com.exe.ConjuntoResidencialArkania.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apartamentos")
@Data
@NoArgsConstructor //genera constructor sin parámetros
@AllArgsConstructor //genera constructor con todos los parámetros
public class ApartamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartamento_id")
    private Long apartamentoId;

    @Column(name = "numero", nullable = false, length = 10)
    @NotBlank(message = "Por favor ingrese el número del apartamento.")
    @Size(min = 1, max = 10, message = "El número del apartamento debe tener entre 1 y 10 caracteres")
    private String numero;

    @Column(name = "torre", nullable = false, length = 20)
    @NotBlank(message = "Por favor ingrese la torre del apartamento.")
    @Size(min = 1, max = 20, message = "La torre del apartamento debe tener entre 1 y 20 caracteres")
    private String torre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "propietario_id", nullable = false)
    private UserEntity propietario;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoApartamento estado = EstadoApartamento.LIBRE;

    public enum EstadoApartamento {
        LIBRE,
        OCUPADO,
        INACTIVO
    }
}

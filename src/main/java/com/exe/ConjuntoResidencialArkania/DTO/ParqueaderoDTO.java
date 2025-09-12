package com.exe.ConjuntoResidencialArkania.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // constructor vacío (para deserialización JSON en Spring)
@AllArgsConstructor // constructor con todos los atributos
public class ParqueaderoDTO {

    private Long parqueaderoId;
    private String tipoRol;
    private String numero;
    private Long usuarioId;
    private String estado;
    public ParqueaderoDTO(Long parqueaderoId, String tipoRol, String numero, String estado) {
        this.parqueaderoId = parqueaderoId;
        this.tipoRol = tipoRol;
        this.numero = numero;
        this.estado = estado;
    }
}

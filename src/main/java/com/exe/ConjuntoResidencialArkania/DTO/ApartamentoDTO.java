package com.exe.ConjuntoResidencialArkania.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartamentoDTO {

    private Long apartamentoId;
    private String numero;
    private String torre;
    private Long propietarioId;
    private String estado;
    public ApartamentoDTO(Long apartamentoId, String numero, String torre, String estado) {
        this.apartamentoId = apartamentoId;
        this.numero = numero;
        this.torre = torre;
        this.estado = estado;
    }
}

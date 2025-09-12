package com.exe.ConjuntoResidencialArkania.Service;

import com.exe.ConjuntoResidencialArkania.DTO.ApartamentoDTO;
import java.util.List;
import java.util.Optional;

public interface ApartamentoService {

    List<ApartamentoDTO> obtenerTodos();
    Optional<ApartamentoDTO> obtenerPorId(Long id);
    ApartamentoDTO crear(ApartamentoDTO apartamentoDTO);
    ApartamentoDTO actualizar(Long id, ApartamentoDTO apartamentoDTO);
    void eliminar(Long id);
}
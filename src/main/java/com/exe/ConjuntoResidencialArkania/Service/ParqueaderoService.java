package com.exe.ConjuntoResidencialArkania.Service;

import com.exe.ConjuntoResidencialArkania.DTO.ParqueaderoDTO;
import java.util.List;
import java.util.Optional;

public interface ParqueaderoService {

    List<ParqueaderoDTO> obtenerTodos();
    Optional<ParqueaderoDTO> obtenerPorId(Long id);
    ParqueaderoDTO crear(ParqueaderoDTO parqueaderoDTO);
    ParqueaderoDTO actualizar(Long id, ParqueaderoDTO parqueaderoDTO);
    void eliminar(Long id);
}
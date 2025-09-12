package main.java.com.exe.ConjuntoResidencialArkania.Service;

import java.util.List;
import java.util.Optional;

import com.exe.ConjuntoResidencialArkania.DTO.AreasComunesDTO;

public interface AreasComunesService {
    List<AreasComunesDTO> listarAreasComunes(); 

    // Buscar Area por ID
    Optional<AreasComunesDTO> findById(Long idAreaComun); 

    // Guardar Area
    AreasComunesDTO guardarAreasComunes(AreasComunesDTO areasComunesDTO); // Insert into personas (nombre, email) values (?, ?)

    // Actualizar Area
    AreasComunesDTO actualizarAreaComun(Long idAreaComun, AreasComunesDTO areasComunesDTO); // Update personas set nombre=?, email=? where id=?

    // Eliminar Area por ID
    void eliminarAreasComunes(Long idAreaComun);
}

package main.java.com.exe.ConjuntoResidencialArkania.Service;

import java.util.List;
import java.util.Optional;

import com.exe.ConjuntoResidencialArkania.DTO.SolicitudesDTO;

public interface SolicitudesService {
    List<solicitudesDTO> listarSolicitudes();

    Optional<SolicitudesDTO> findById(Long idSolicitud);

    SolicitudesDTO guardarSolicitud(SolicitudesDTO solicitudDTO);

    SolicitudesDTO editarSolicitud(Long idSolicitud, SolicitudesDTO solicitudDto);

    void eliminarSolicitud(Long idSolicitud);
}

package main.java.com.exe.ConjuntoResidencialArkania.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.exe.ConjuntoResidencialArkania.DTO.SolicitudesDTO;
import com.exe.ConjuntoResidencialArkania.Entity.SolicitudesEntity;

public interface SolicitudesService {
    List<solicitudesDTO> listarSolicitudes();

    Optional<SolicitudesDTO> findById(Long idSolicitud);

    SolicitudesDTO guardarSolicitud(SolicitudesDTO solicitudDTO);

    SolicitudesDTO editarSolicitud(Long idSolicitud, SolicitudesDTO solicitudDto);

    void eliminarSolicitud(Long idSolicitud);

    //Metodos del repository

    List<SolicitudesDTO> listarPorFechaCreacion(LocalDateTime inicio, LocalDateTime fin);

    List<SolicitudesDTO> listarPorFechaResolucion(LocalDateTime inicio, LocalDateTime fin);

    List<SolicitudesDTO> listarPorEstados(List<SolicitudesEntity.EstadoSolicitud> estados);

    List<SolicitudesDTO> listarPorTipos(List<SolicitudesEntity.TipoSolicitud> tipos);

    List<SolicitudesDTO> buscarPorDescripcion(String descripcion);

    List<SolicitudesDTO> filtrarPorEstadosYTipos(
        List<SolicitudesEntity.EstadoSolicitud> estados,
        List<SolicitudesEntity.TipoSolicitud> tipos
    );
}

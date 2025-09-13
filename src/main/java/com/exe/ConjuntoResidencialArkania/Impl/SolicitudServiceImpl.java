package main.java.com.exe.ConjuntoResidencialArkania.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.exe.ConjuntoResidencialArkania.DTO.SolicitudesDTO;
import com.exe.ConjuntoResidencialArkania.Entity.SolicitudesEntity;
import com.exe.ConjuntoResidencialArkania.Repository.SolicitudRepository;

import main.java.com.exe.ConjuntoResidencialArkania.Service.SolicitudesService;

@Service
public class SolicitudServiceImpl implements SolicitudesService {
    public final SolicitudRepository solicitudRepository;
    public final ModelMapper modelMapper;

    @Autowired
    public SolicitudServiceImpl(SolicitudRepository solicitudRepository, ModelMapper modelMapper) {
        this.solicitudRepository = solicitudRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SolicitudesDTO> listarSolicitudes() {
        List<SolicitudesEntity> solicitudes = solicitudRepository.findAll();
        return solicitudes.stream().map(solicitud -> modelMapper.map(solicitud, SolicitudesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SolicitudesDTO> findById(Long idSolicitud) {
        return solicitudRepository.findById(idSolicitud)
                .map(solicitud -> modelMapper.map(solicitud, SolicitudesDTO.class));
    }

    @Override
    public SolicitudesDTO guardarSolicitud(SolicitudesDTO solicitudDTO) {
        SolicitudesEntity solicitud = modelMapper.map(solicitudDTO, SolicitudesEntity.class);
        solicitud = solicitudRepository.save(solicitud);
        return modelMapper.map(solicitud, SolicitudesDTO.class);
    }

    @Override
    public SolicitudesDTO editarSolicitud(Long idSolicitud, SolicitudesDTO solicitudDto) {
        SolicitudesEntity solExistente = solicitudRepository.findById(idSolicitud).orElseThrow(
                () -> new IllegalArgumentException("La solicitud con el ID: " + idSolicitud + " no encontrada"));
        solExistente.setEstadoSolicitud(solicitudDto.getEstadoSolicitud());
        solExistente.setDescripcion(solicitudDto.getDescripcion());

        SolicitudesEntity solActualizada = solicitudRepository.save(solExistente);
        return modelMapper.map(solActualizada, SolicitudesDTO.class);
    }

    @Override
    public void eliminarSolicitud(Long idSolicitud) {
        solicitudRepository.deleteById(idSolicitud);
    }

    // Repository metodos
    @Override
    public List<SolicitudesDTO> listarPorFechaCreacion(LocalDateTime inicio, LocalDateTime fin) {
        List<SolicitudesEntity> entidades = solicitudRepository.findByFechaCreacion(inicio, fin);
        return entidades.stream()
                .map(e -> modelMapper.map(e, SolicitudesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudesDTO> listarPorFechaResolucion(LocalDateTime inicio, LocalDateTime fin) {
        List<SolicitudesEntity> entidades = solicitudRepository.findByFechaResolucion(inicio, fin);
        return entidades.stream()
                .map(e -> modelMapper.map(e, SolicitudesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudesDTO> listarPorEstados(List<SolicitudesEntity.EstadoSolicitud> estados) {
        List<SolicitudesEntity> entidades = solicitudRepository.findByEstadoSolicitudInOrderByFechaCreacionAsc(estados);
        return entidades.stream()
                .map(e -> modelMapper.map(e, SolicitudesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudesDTO> listarPorTipos(List<SolicitudesEntity.TipoSolicitud> tipos) {
        List<SolicitudesEntity> entidades = solicitudRepository.findByTipoSolicitudInOrderByFechaCreacionAsc(tipos);
        return entidades.stream()
                .map(e -> modelMapper.map(e, SolicitudesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudesDTO> buscarPorDescripcion(String descripcion) {
        List<SolicitudesEntity> entidades = solicitudRepository.findByDescripcionContainingIgnoreCase(descripcion);
        return entidades.stream()
                .map(e -> modelMapper.map(e, SolicitudesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudesDTO> filtrarPorEstadosYTipos(
            List<SolicitudesEntity.EstadoSolicitud> estados,
            List<SolicitudesEntity.TipoSolicitud> tipos) {

        List<SolicitudesEntity> entidades = solicitudRepository.findAll().stream()
                .filter(e -> estados.contains(e.getEstadoSolicitud()) && tipos.contains(e.getTipoSolicitud()))
                .collect(Collectors.toList());

        return entidades.stream()
                .map(e -> modelMapper.map(e, SolicitudesDTO.class))
                .collect(Collectors.toList());
    }
}

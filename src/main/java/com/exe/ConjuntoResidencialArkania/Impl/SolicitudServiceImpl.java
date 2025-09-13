package main.java.com.exe.ConjuntoResidencialArkania.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.exe.ConjuntoResidencialArkania.DTO.SolicitudesDTO;
import com.exe.ConjuntoResidencialArkania.Entity.SolicitudesEntity;
import com.exe.ConjuntoResidencialArkania.Repository.SolicitudRepository;

@Service
public class SolicitudServiceImpl implements SolicitudesService{
    public final SolicitudRepository solicitudRepository;
    public final ModelMapper modelMapper;

    @Autowired
    public SolicitudServiceImpl(SolicitudRepository solicitudRepository, ModelMapper modelMapper){
        this.solicitudRepository = solicitudRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SolicitudesDTO> listarSolicitudes(){
        List<SolicitudesEntity> solicitudes = solicitudRepository.findAll();
        return solicitudes.stream().map(solicitud->modelMapper.map(solicitud, SolicitudesDTO.class))
        .collect(Collectors.toList());
    }

    @Override
    public Optional<SolicitudesDTO> findById(Long idSolicitud){
        return solicitudRepository.findById(idSolicitud).
        map(solicitud->modelMapper.map(solicitud, SolicitudesDTO.class));
    }
    
    @Override
     public SolicitudesDTO guardarSolicitud(SolicitudesDTO solicitudDTO){
        SolicitudesEntity solicitud = modelMapper.map(solicitudDTO, SolicitudesEntity.class);
        solicitud = solicitudRepository.save(solicitud);
        return modelMapper.map(solicitud, SolicitudesDTO.class);
    }

    @Override
    public SolicitudesDTO editarSolicitud(Long idSolicitud, SolicitudesDTO solicitudDto){
        SolicitudesEntity solExistente = solicitudRepository.findById(idSolicitud).
        orElseThrow(() -> new IllegalArgumentException("La solicitud con el ID: " + idSolicitud + " no encontrada"));
        solExistente.setEstadoSolicitud(solicitudDto.getEstadoSolicitud());
        solExistente.setDescripcion(solicitudDto.getDescripcion());

        SolicitudesEntity solActualizada = solicitudRepository.save(solExistente);
        return modelMapper.map(solActualizada, SolicitudesDTO.class);
    }


    
    @Override
    public void eliminarSolicitud(Long idSolicitud){
        solicitudRepository.deleteById(idSolicitud);
    }
}

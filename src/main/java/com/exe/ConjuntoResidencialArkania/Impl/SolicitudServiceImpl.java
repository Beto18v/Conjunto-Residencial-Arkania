package main.java.com.exe.ConjuntoResidencialArkania.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.exe.ConjuntoResidencialArkania.DTO.AreasComunesDTO;
import com.exe.ConjuntoResidencialArkania.DTO.SolicitudesDTO;
import com.exe.ConjuntoResidencialArkania.Entity.AreasComunesEntity;
import com.exe.ConjuntoResidencialArkania.Entity.SolicitudesEntity;
import com.exe.ConjuntoResidencialArkania.Repository.AreasComunesRepository;
import com.exe.ConjuntoResidencialArkania.Repository.SolicitudRepository;
@Service
public class SolicitudServiceImpl extends SolicitudesService{
    public final SolicitudRepository solicitudRepository;
    public final ModelMapper modelMapper;

    @Autowired
    public solicitudesServiceImpl(SolicitudRepository solicitudRepository, ModelMapper modelMapper){
        this.solicitudRepository = solicitudRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SolicitudesDTO> listarSolicitudes(){
        List<SolicitudesEntity> solicitudes = SolicitudRepository.findAll();
        return solicitudes.stream().map(solicitud->modelMapper.map(solicitud, SolicitudesDTO.class))
        .collect(Collectors.toList());
    }

    @Override
    public Long<SolicitudesDTO> findById(Long idSolicitud){
        return SolicitudesRepository.findById(idSolicitud).
        map(solicitud->modelMapper.map(solicitud, SolicitudesDTO.class));
    }
    
    @Override
     public SolicitudesDTO guardarSolicitud(SolicitudesDTO solicitudDTO){
        SolicitudesEntity = modelMapper.map(area, SolicitudesDTO.class);
        solicitud = AreasComunesRepository.save(area);
        return modelMapper.map(area, AreasComunesDTO.class);
    }

    @Override
    public SolicitudesDTO editarSolicitud(Long idSolicitud, SolicitudesDTO solicitudDto){
        SolicitudesEntity solExistente = SolicitudRepository.findById(idSolicitud).
        orElseThrow(() -> new IllegalArgumentException("Area con el ID: " + idSolicitud + " no encontrada"));
        solExistente.setEstadoSolicitud(solicitudDto.getEstadoSolicitud());
        solExistente.setDescripcion(solicitudDto.getDescripcion());

        SolicitudesEntity solActualizada = SolicitudRepository.save(solExistente);
        return modelMapper.map(solActualizada, SolicitudesDTO.class);
    }


    
    @Override
    public void eliminarSolicitud(Long idAreaComun){
        AreasComunesDTO.deletedById(idAreaComun);
    }
}

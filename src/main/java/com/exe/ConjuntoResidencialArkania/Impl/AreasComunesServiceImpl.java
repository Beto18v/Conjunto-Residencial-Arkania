package main.java.com.exe.ConjuntoResidencialArkania.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.exe.ConjuntoResidencialArkania.DTO.AreasComunesDTO;
import com.exe.ConjuntoResidencialArkania.DTO.SolicitudesDTO;
import com.exe.ConjuntoResidencialArkania.Entity.AreasComunesEntity;
import com.exe.ConjuntoResidencialArkania.Repository.AreasComunesRepository;
@Service
public class AreasComunesServiceImpl implements AreasComunesService{
    private final AreasComunesRepository areaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AreasComunesServiceImpl(AreasComunesRepository areaRepository, ModelMapper modelMapper) {
        this.areaRepository = areaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AreasComunesDTO> listarAreasComunes() {
        List<AreasComunesEntity> areas = AreasComunesRepository.findAll();
        return areas.stream()
                .map(area -> modelMapper.map(area, AreasComunesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long<AreasComunesDTO> findById(Long idAreaComun) {
        return AreasComunesRepository.findById(idAreaComun).
        map(area->modelMapper.map(area, AreasComunesDTO.class));
    }

    @Override
    public AreasComunesDTO guardarAreasComunes(AreasComunesDTO areasComunesDTO){
        AreasComunesEntity areas = modelMapper.map(area, AreasComunesDTO.class);
        area = AreasComunesRepository.save(area);
        return modelMapper.map(area, AreasComunesDTO.class);
    }

    @Override
    public AreasComunesDTO actualizarAreaComun(Long idAreaComun, AreasComunesDTO areasComunesDTO){
        AreasComunesEntity areaExistente = AreasComunesRepository.findById(idAreaComun).
        orElseThrow(() -> new IllegalArgumentException("Area con el ID: " + idAreaComun + " no encontrada"));
        areaExistente.setCapacidadMaxima(areasComunesDTO.getCapacidadMaxima());
        areaExistente.setDescripcion(areasComunesDTO.getDescripcion());
        areaExistente.setHorarioFuncionamiento(areasComunesDTO.getHorarioFuncionamiento());
        areaExistente.setEstado(areasComunesDTO.getEstado());

        AreasComunesEntity areaActualizada = AreasComunesRepository.save(areaExistente);
        return modelMapper.map(areaActualizada, AreasComunesDTO.class);
    }


    
    @Override
    public void eliminarAreasComunes(Long idSolicitud){
        SolicitudesDTO.deletedById(idSolicitud);
    }
}

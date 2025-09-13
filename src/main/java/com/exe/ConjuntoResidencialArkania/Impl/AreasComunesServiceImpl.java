package main.java.com.exe.ConjuntoResidencialArkania.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.exe.ConjuntoResidencialArkania.DTO.AreasComunesDTO;
import com.exe.ConjuntoResidencialArkania.DTO.SolicitudesDTO;
import com.exe.ConjuntoResidencialArkania.Entity.AreasComunesEntity;
import com.exe.ConjuntoResidencialArkania.Repository.AreasComunesRepository;

import main.java.com.exe.ConjuntoResidencialArkania.Service.AreasComunesService;
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
        List<AreasComunesEntity> areas = areaRepository.findAll();
        return areas.stream()
                .map(area -> modelMapper.map(area, AreasComunesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AreasComunesDTO> findById(Long idAreaComun) {
        
        return areaRepository.findById(idAreaComun).
        map(area->modelMapper.map(area, AreasComunesDTO.class));
    }

    @Override
    public AreasComunesDTO guardarAreasComunes(AreasComunesDTO areasComunesDTO){
        AreasComunesEntity area = modelMapper.map(areasComunesDTO, AreasComunesEntity.class);
        area = areaRepository.save(area);
        return modelMapper.map(area, AreasComunesDTO.class);
    }

    @Override
    public AreasComunesDTO actualizarAreaComun(Long idAreaComun, AreasComunesDTO areasComunesDTO){
        AreasComunesEntity areaExistente = areaRepository.findById(idAreaComun).
        orElseThrow(() -> new IllegalArgumentException("Area con el ID: " + idAreaComun + " no encontrada"));
        areaExistente.setCapacidadMaxima(areasComunesDTO.getCapacidadMaxima());
        areaExistente.setDescripcion(areasComunesDTO.getDescripcion());
        areaExistente.setHorarioFuncionamiento(areasComunesDTO.getHorarioFuncionamiento());
        areaExistente.setEstado(areasComunesDTO.getEstado());

        AreasComunesEntity areaActualizada = areaRepository.save(areaExistente);
        return modelMapper.map(areaActualizada, AreasComunesDTO.class);
    }


    
    @Override
    public void eliminarAreasComunes(Long idAreaComun){
        areaRepository.deleteById(idAreaComun);
    }
}

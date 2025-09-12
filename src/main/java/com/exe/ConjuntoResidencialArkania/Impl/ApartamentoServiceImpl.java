package com.exe.ConjuntoResidencialArkania.Impl;

import com.exe.ConjuntoResidencialArkania.DTO.ApartamentoDTO;
import com.exe.ConjuntoResidencialArkania.Entity.ApartamentoEntity;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;
import com.exe.ConjuntoResidencialArkania.Repository.ApartamentoRepository;
import com.exe.ConjuntoResidencialArkania.Repository.UserRepository;
import com.exe.ConjuntoResidencialArkania.Service.ApartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartamentoServiceImpl implements ApartamentoService {

    private final ApartamentoRepository apartamentoRepository;
    private final UserRepository userRepository;

    @Override
    public List<ApartamentoDTO> obtenerTodos() {
        return apartamentoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ApartamentoDTO> obtenerPorId(Long id) {
        return apartamentoRepository.findById(id)
                .map(this::convertirADTO);
    }

    @Override
    public ApartamentoDTO crear(ApartamentoDTO apartamentoDTO) {
        ApartamentoEntity entity = convertirAEntity(apartamentoDTO);
        ApartamentoEntity guardado = apartamentoRepository.save(entity);
        return convertirADTO(guardado);
    }

    @Override
    public ApartamentoDTO actualizar(Long id, ApartamentoDTO apartamentoDTO) {
        ApartamentoEntity entity = apartamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apartamento no encontrado"));

        entity.setNumero(apartamentoDTO.getNumero());
        entity.setTorre(apartamentoDTO.getTorre());
        entity.setEstado(ApartamentoEntity.EstadoApartamento.valueOf(apartamentoDTO.getEstado()));

        if (apartamentoDTO.getPropietarioId() != null) {
            UserEntity propietario = userRepository.findById(apartamentoDTO.getPropietarioId())
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
            entity.setPropietario(propietario);
        }

        ApartamentoEntity actualizado = apartamentoRepository.save(entity);
        return convertirADTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        apartamentoRepository.deleteById(id);
    }

    private ApartamentoDTO convertirADTO(ApartamentoEntity entity) {
        return new ApartamentoDTO(
                entity.getApartamentoId(),
                entity.getNumero(),
                entity.getTorre(),
                entity.getPropietario() != null ? entity.getPropietario().getUsuarioId() : null,
                entity.getEstado().toString()
        );
    }

    private ApartamentoEntity convertirAEntity(ApartamentoDTO dto) {
        ApartamentoEntity entity = new ApartamentoEntity();
        entity.setNumero(dto.getNumero());
        entity.setTorre(dto.getTorre());
        entity.setEstado(ApartamentoEntity.EstadoApartamento.valueOf(dto.getEstado()));

        if (dto.getPropietarioId() != null) {
            UserEntity propietario = userRepository.findById(dto.getPropietarioId())
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
            entity.setPropietario(propietario);
        }

        return entity;
    }
}
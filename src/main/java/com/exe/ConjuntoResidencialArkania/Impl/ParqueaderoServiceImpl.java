package com.exe.ConjuntoResidencialArkania.Impl;

import com.exe.ConjuntoResidencialArkania.DTO.ParqueaderoDTO;
import com.exe.ConjuntoResidencialArkania.Entity.ParqueaderoEntity;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;
import com.exe.ConjuntoResidencialArkania.Repository.ParqueaderoRepository;
import com.exe.ConjuntoResidencialArkania.Repository.UserRepository;
import com.exe.ConjuntoResidencialArkania.Service.ParqueaderoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParqueaderoServiceImpl implements ParqueaderoService {

    private final ParqueaderoRepository parqueaderoRepository;
    private final UserRepository userRepository;

    @Override
    public List<ParqueaderoDTO> obtenerTodos() {
        return parqueaderoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ParqueaderoDTO> obtenerPorId(Long id) {
        return parqueaderoRepository.findById(id)
                .map(this::convertirADTO);
    }

    @Override
    public ParqueaderoDTO crear(ParqueaderoDTO parqueaderoDTO) {
        ParqueaderoEntity entity = convertirAEntity(parqueaderoDTO);
        ParqueaderoEntity guardado = parqueaderoRepository.save(entity);
        return convertirADTO(guardado);
    }

    @Override
    public ParqueaderoDTO actualizar(Long id, ParqueaderoDTO parqueaderoDTO) {
        ParqueaderoEntity entity = parqueaderoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parqueadero no encontrado"));

        entity.setTipoRol(ParqueaderoEntity.TipoRolParqueadero.valueOf(parqueaderoDTO.getTipoRol()));
        entity.setNumero(parqueaderoDTO.getNumero());
        entity.setEstado(ParqueaderoEntity.EstadoParqueadero.valueOf(parqueaderoDTO.getEstado()));

        if (parqueaderoDTO.getUsuarioId() != null) {
            UserEntity usuario = userRepository.findById(parqueaderoDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            entity.setUsuario(usuario);
        }

        ParqueaderoEntity actualizado = parqueaderoRepository.save(entity);
        return convertirADTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        parqueaderoRepository.deleteById(id);
    }

    private ParqueaderoDTO convertirADTO(ParqueaderoEntity entity) {
        return new ParqueaderoDTO(
                entity.getParqueaderoId(),
                entity.getTipoRol().toString(),
                entity.getNumero(),
                entity.getUsuario() != null ? entity.getUsuario().getUsuarioId() : null,
                entity.getEstado().toString()
        );
    }

    private ParqueaderoEntity convertirAEntity(ParqueaderoDTO dto) {
        ParqueaderoEntity entity = new ParqueaderoEntity();
        entity.setTipoRol(ParqueaderoEntity.TipoRolParqueadero.valueOf(dto.getTipoRol()));
        entity.setNumero(dto.getNumero());
        entity.setEstado(ParqueaderoEntity.EstadoParqueadero.valueOf(dto.getEstado()));

        if (dto.getUsuarioId() != null) {
            UserEntity usuario = userRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            entity.setUsuario(usuario);
        }

        return entity;
    }
}
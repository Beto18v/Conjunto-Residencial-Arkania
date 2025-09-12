package com.exe.ConjuntoResidencialArkania.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exe.ConjuntoResidencialArkania.Entity.AreasComunesEntity;
import com.exe.ConjuntoResidencialArkania.Entity.SolicitudesEntity;

public interface AreasComunesRepository extends JpaRepository<AreasComunesEntity, Long> {
    // Obtener areas comunes con capacidad mayor a un valor dado, ordenadas por nombre
    List<AreasComunesEntity> findByEstadoAndCapacidadMaximaGreaterThanOrderByNombreAsc(
            AreasComunesEntity.EstadoArea estado, int capacidad);
    //Busca una palabara en nombre o descripcion
    List<AreasComunesEntity> findByNombreContainingIgnoreCase(
            String nombre);

    List<AreasComunesEntity> findByDescripcionContainingIgnoreCase(
            String descripcion);

    List<AreasComunesEntity> findByDescripcionContainingIgnoreCaseOrNombreContainingIgnoreCase(
            String descripcion, String nombre);

    //Arroja cuantas areas activas o inactivas hay 
    long activas = areasComunesRepository.countByEstado(AreasComunesEntity.EstadoArea.activa);
    long inactivas = areasComunesRepository.countByEstado(AreasComunesEntity.EstadoArea.inactiva);

}

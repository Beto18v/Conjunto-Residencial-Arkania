package com.exe.ConjuntoResidencialArkania.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exe.ConjuntoResidencialArkania.Entity.AreasComunesEntity;

public interface AreasComunesRepository extends JpaRepository<AreasComunesEntity, Long> {
    //Obtener areas comunes activas con capacidad mayor a un valor dado, ordenadas por nombre
    @Query("SELECT a FROM AreasComunesEntity a " +
        "WHERE a.estado = 'activa' AND a.capacidadMaxima > :capacidad " +
        "ORDER BY a.nombre ASC")
    List<AreasComunesEntity> findAllActiveAreasOrderedByName(@Param("capacidad") int capacidad);


}

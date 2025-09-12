package com.exe.ConjuntoResidencialArkania.Repository;

import com.exe.ConjuntoResidencialArkania.Entity.SolicitudesEntity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudesEntity, Long> {

    // Busca en un rango de fechas solicitudes ya solucionadas (Con fecha de
    // resolucion ya definida)
    @Query("SELECT s FROM SolicitudesEntity s " +
            "WHERE s.fechaResolucion BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY s.fechaResolucion ASC")
    List<SolicitudesEntity> findByFechaResolucion(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    // Busca en un rango de fechas las solicitudes creadas (Fecha de creacion)
    @Query("SELECT s FROM SolicitudesEntity s " +
            "WHERE s.fechaCreacion BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY s.fechaCreacion ASC")
    List<SolicitudesEntity> findByFechaCreacion(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    // Busca segun Tipo y Estado de la solicitud. Busca segun uno o varios tipos o estados
    // Ejm. Busca pendiente y en_proceso o solo busca aceptada.
    List<SolicitudesEntity> findByEstadoSolicitudInOrderByFechaCreacionAsc(
        List<SolicitudesEntity.EstadoSolicitud> estados);

    List<SolicitudesEntity> findByTipoSolicitudInOrderByFechaCreacionAsc(List<SolicitudesEntity.TipoSolicitud> tipos);

    //Busca una palabra clave en la descripcion
    List<SolicitudesEntity> findByDescripcionContainingIgnoreCase(String descripcion);
}

package com.exe.ConjuntoResidencialArkania.Repository;

import com.exe.ConjuntoResidencialArkania.Entity.SolicitudesEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudesEntity, Long> {
    @Query("SELECT todasSolicitudesUsuario FROM SolicitudesEntity todasSolicitudesUsuario WHERE todasSolicitudesUsuario.usuario.id = :usuarioId")
    List<SolicitudesEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);

}

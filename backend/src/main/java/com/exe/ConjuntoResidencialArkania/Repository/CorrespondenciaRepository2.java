package com.exe.ConjuntoResidencialArkania.Repository;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Estado;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Tipo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CorrespondenciaRepository2 extends JpaRepository<CorrespondenciaEntity, Long> {
    
    // Buscar todas las correspondencias por destinatario
    List<CorrespondenciaEntity> findByDestinatario_IdUsuario(Long destinatarioId);

    // Buscar correspondencias por estado (Pendiente, Entregada, etc.)
    List<CorrespondenciaEntity> findByEstado(Estado estado);

    // Buscar correspondencias por tipo (Paquete, Documento, Otro)
    List<CorrespondenciaEntity> findByTipo(Tipo tipo);

    // Buscar correspondencias registradas en un rango de fechas
    List<CorrespondenciaEntity> findByFechaRecepcionBetween(LocalDateTime inicio, LocalDateTime fin);

    // Buscar correspondencias entregadas por un usuario específico (retiradas por)
    List<CorrespondenciaEntity> findByRetiradoPor_IdUsuario(Long usuarioId);

}

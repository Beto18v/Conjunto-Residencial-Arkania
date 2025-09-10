package com.exe.ConjuntoResidencialArkania.Repository;

import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity;
import com.exe.ConjuntoResidencialArkania.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la gestión de correspondencia en el conjunto residencial.
 * Contiene consultas personalizadas para casos de uso específicos del dominio.
 */
@Repository
public interface CorrespondenciaRepository extends JpaRepository<CorrespondenciaEntity, Long> {

    // Encuentra todas las correspondencias pendientes de entrega. Útil para mostrar al personal de portería qué está pendiente.
    @Query("SELECT c FROM CorrespondenciaEntity c WHERE c.estado = 'PENDIENTE' ORDER BY c.fechaRecepcion ASC")
    List<CorrespondenciaEntity> findAllPendientes();

    // Encuentra correspondencias por destinatario que están pendientes. Útil para mostrar a un residente qué correspondencia tiene pendiente.
    @Query("SELECT c FROM CorrespondenciaEntity c WHERE c.destinatario = :destinatario AND c.estado = 'PENDIENTE' ORDER BY c.fechaRecepcion ASC")
    List<CorrespondenciaEntity> findPendientesByDestinatario(@Param("destinatario") UserEntity destinatario);

    // Encuentra correspondencias por destinatario (todas, sin importar estado). Útil para historial completo de un residente.
    @Query("SELECT c FROM CorrespondenciaEntity c WHERE c.destinatario = :destinatario ORDER BY c.fechaRecepcion DESC")
    List<CorrespondenciaEntity> findAllByDestinatario(@Param("destinatario") UserEntity destinatario);

    // Encuentra correspondencias registradas por un usuario específico. Útil para auditoría del personal de portería.
    @Query("SELECT c FROM CorrespondenciaEntity c WHERE c.registradoPor = :registradoPor ORDER BY c.fechaRecepcion DESC")
    List<CorrespondenciaEntity> findByRegistradoPor(@Param("registradoPor") UserEntity registradoPor);

    // Encuentra correspondencias entregadas por una persona específica. Útil para saber quién retiró correspondencia.
    @Query("SELECT c FROM CorrespondenciaEntity c WHERE c.retiradoPor = :retiradoPor ORDER BY c.fechaEntrega DESC")
    List<CorrespondenciaEntity> findByRetiradoPor(@Param("retiradoPor") UserEntity retiradoPor);

    // Encuentra correspondencias por tipo y estado. Útil para filtrar por tipo de correspondencia (paquetes, documentos, etc.).
    @Query("SELECT c FROM CorrespondenciaEntity c WHERE c.tipo = :tipo AND c.estado = :estado ORDER BY c.fechaRecepcion ASC")
    List<CorrespondenciaEntity> findByTipoAndEstado(@Param("tipo") CorrespondenciaEntity.Tipo tipo, 
                                                    @Param("estado") CorrespondenciaEntity.Estado estado);

    // Encuentra correspondencias antigas pendientes (más de X días). Útil para identificar correspondencia que lleva mucho tiempo sin retirar.
    @Query("SELECT c FROM CorrespondenciaEntity c WHERE c.estado = 'PENDIENTE' AND c.fechaRecepcion < :fechaLimite ORDER BY c.fechaRecepcion ASC")
    List<CorrespondenciaEntity> findCorrespondenciaAntigua(@Param("fechaLimite") LocalDateTime fechaLimite);

}

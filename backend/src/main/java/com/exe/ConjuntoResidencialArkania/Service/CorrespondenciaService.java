package com.exe.ConjuntoResidencialArkania.Service;

import java.time.LocalDateTime;

import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Estado;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Tipo;
import com.exe.ConjuntoResidencialArkania.Repository.CorrespondenciaRepository2;

import java.util.List;
import java.util.Optional;

public class CorrespondenciaService {
    
    private final CorrespondenciaRepository2 correspondenciaRepository2;

    public CorrespondenciaService(CorrespondenciaRepository2 correspondenciaRepository2) {
        this.correspondenciaRepository2 = correspondenciaRepository2;
    }

    // Create
    public CorrespondenciaEntity crearCorrespondencia(CorrespondenciaEntity correspondencia) {
        return correspondenciaRepository2.save(correspondencia);
    }
    
    // Read All
    public List<CorrespondenciaEntity> listarTodas() {
        return correspondenciaRepository2.findAll();
    }

    // Read One
    public Optional<CorrespondenciaEntity> buscarPorId(Long id) {
        return correspondenciaRepository2.findById(id);
    }

    // Update
    public CorrespondenciaEntity actualizarCorrespondencia(CorrespondenciaEntity correspondencia) {
        return correspondenciaRepository2.save(correspondencia);
    }

    // Delete
    public void eliminarCorrespondencia(Long id) {
        correspondenciaRepository2.deleteById(id);
    }

    // Buscar por destinatario
    public List<CorrespondenciaEntity> buscarPorDestinatario(Long destinatarioId) {
        return correspondenciaRepository2.findByDestinatario_IdUsuario(destinatarioId);
    }

    // Buscar por estado
    public List<CorrespondenciaEntity> buscarPorEstado(Estado estado) {
        return correspondenciaRepository2.findByEstado(estado);
    }

    // Buscar por tipo
    public List<CorrespondenciaEntity> buscarPorTipo(Tipo tipo) {
        return correspondenciaRepository2.findByTipo(tipo);
    }

    // Buscar por rango de fechas
    public List<CorrespondenciaEntity> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return correspondenciaRepository2.findByFechaRecepcionBetween(inicio, fin);
    }

    // Buscar por quien retiró
    public List<CorrespondenciaEntity> buscarPorRetiradoPor(Long usuarioId) {
        return correspondenciaRepository2.findByRetiradoPor_IdUsuario(usuarioId);
    }

}

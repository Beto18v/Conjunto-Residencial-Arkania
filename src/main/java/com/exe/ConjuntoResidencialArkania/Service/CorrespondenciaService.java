package com.exe.ConjuntoResidencialArkania.Service;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Estado;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity.Tipo;
import com.exe.ConjuntoResidencialArkania.Repository.CorrespondenciaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CorrespondenciaService {
    private final CorrespondenciaRepository correspondenciaRepository;

    @Autowired
    public CorrespondenciaService(CorrespondenciaRepository correspondenciaRepository) {
        this.correspondenciaRepository = correspondenciaRepository;
    }

    // Create
    public CorrespondenciaEntity crearCorrespondencia(CorrespondenciaEntity correspondencia) {
        return correspondenciaRepository.save(correspondencia);
    }

    // Read All
    public List<CorrespondenciaEntity> listarTodas() {
        return correspondenciaRepository.findAll();
    }

    // Read One
    public Optional<CorrespondenciaEntity> buscarPorId(Long id) {
        return correspondenciaRepository.findById(id);
    }

    // Update
    public CorrespondenciaEntity actualizarCorrespondencia(CorrespondenciaEntity correspondencia) {
        return correspondenciaRepository.save(correspondencia);
    }

    // Delete
    public void eliminarCorrespondencia(Long id) {
        correspondenciaRepository.deleteById(id);
    }

    // Buscar por destinatario
    public List<CorrespondenciaEntity> buscarPorDestinatario(Long destinatario) {
        return correspondenciaRepository.findByDestinatario_usuarioId(destinatario);
    }

    // Buscar por estado
    public List<CorrespondenciaEntity> buscarPorEstado(Estado estado) {
        return correspondenciaRepository.findByEstado(estado);
    }

    // Buscar por tipo
    public List<CorrespondenciaEntity> buscarPorTipo(Tipo tipo) {
        return correspondenciaRepository.findByTipo(tipo);
    }

    // Buscar por rango de fechas
    public List<CorrespondenciaEntity> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return correspondenciaRepository.findByFechaRecepcionBetween(inicio, fin);
    }

    // Buscar por quien retiró
    public List<CorrespondenciaEntity> buscarPorRetiradoPor(Long idUsuario) {
        return correspondenciaRepository.findByRetiradoPor_usuarioId(idUsuario);
    }

}

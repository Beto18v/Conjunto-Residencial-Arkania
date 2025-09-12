package com.exe.ConjuntoResidencialArkania.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.exe.ConjuntoResidencialArkania.DTO.CorrespondenciaDTO;
import com.exe.ConjuntoResidencialArkania.Entity.CorrespondenciaEntity;

/**
 * Clase de configuración para la entidad Correspondencia.
 * 
 * Esta clase define configuraciones específicas relacionadas con la entidad Correspondencia,
 * como el mapeo entre entidades y DTOs utilizando ModelMapper.
 * 
 * El objetivo es centralizar y simplificar la conversión entre objetos, asegurando
 * que las transformaciones sean consistentes y fáciles de mantener.
 * 
 * Se utiliza ModelMapper para automatizar el mapeo de campos entre CorrespondenciaEntity
 * y CorrespondenciaDTO, manejando relaciones con otras entidades como UserEntity y ApartamentoEntity.
 */
@Configuration
public class CorrespondenciaConfig {

    /**
     * Bean de ModelMapper para manejar la conversión entre CorrespondenciaEntity y CorrespondenciaDTO.
     * 
     * Este mapeo personalizado asegura que los campos específicos sean correctamente
     * transformados entre la entidad y el DTO, incluyendo relaciones con otras entidades.
     * 
     * @return Una instancia configurada de ModelMapper.
     */
    @Bean
    public ModelMapper correspondenciaModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configuración personalizada para mapear CorrespondenciaEntity a CorrespondenciaDTO
        modelMapper.addMappings(new PropertyMap<CorrespondenciaEntity, CorrespondenciaDTO>() {
            @Override
            protected void configure() {
                // Mapeo de IDs de usuarios relacionados
                map().setRegistradoPor(source.getRegistradoPor().getUsuarioId());
                map().setDestinatario(source.getDestinatario().getUsuarioId());
                map().setRetiradoPor(source.getRetiradoPor() != null ? source.getRetiradoPor().getUsuarioId() : null);
                
                // Mapeo de nombres de usuarios para información adicional en el DTO
                map().setRegistradoPorNombre(source.getRegistradoPor().getNombres() + " " + source.getRegistradoPor().getApellidos());
                map().setDestinatarioNombre(source.getDestinatario().getNombres() + " " + source.getDestinatario().getApellidos());
                map().setRetiradoPorNombre(source.getRetiradoPor() != null ? source.getRetiradoPor().getNombres() + " " + source.getRetiradoPor().getApellidos() : null);
                
                // Mapeo del ID del apartamento relacionado
                map().setApartamentoId(source.getApartamento() != null ? source.getApartamento().getApartamentoId() : null);
                
                // Mapeo de enums a strings para compatibilidad con el DTO
                map().setTipo(source.getTipo().toString());
                map().setEstado(source.getEstado().toString());
            }
        });

        // Configuración personalizada para mapear CorrespondenciaDTO a CorrespondenciaEntity
        modelMapper.addMappings(new PropertyMap<CorrespondenciaDTO, CorrespondenciaEntity>() {
            @Override
            protected void configure() {
                // Omitir campos que requieren resolución manual de entidades relacionadas
                // Estos se manejarán en el servicio mediante consultas a la base de datos
                skip(destination.getRegistradoPor());
                skip(destination.getDestinatario());
                skip(destination.getRetiradoPor());
                skip(destination.getApartamento());
                
                // Mapeo de strings a enums
                map(source.getTipo()).setTipo(null); // Se manejará manualmente en el servicio
                map(source.getEstado()).setEstado(null); // Se manejará manualmente en el servicio
            }
        });

        return modelMapper;
    }
}
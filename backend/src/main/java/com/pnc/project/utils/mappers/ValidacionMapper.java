package com.pnc.project.utils.mappers;

import com.pnc.project.dto.request.validacion.ValidacionRequest;
import com.pnc.project.dto.response.validacion.ValidacionResponse;
import com.pnc.project.entities.Formulario;
import com.pnc.project.entities.Usuario;
import com.pnc.project.entities.Validacion;

import java.util.List;

public class ValidacionMapper {

    public static Validacion toEntity(ValidacionResponse validacionDTO){
        return Validacion.builder()
                .idValidacion(validacionDTO.getIdValidacion())
                .fechaValidacion(validacionDTO.getFeachaValidacion())
                .estado(validacionDTO.getEstado())           // enum
                .usuario(Usuario.builder().codigoUsuario(validacionDTO.getCodigoUsuario()).build())
                .formulario(Formulario.builder().idFormulario(validacionDTO.getIdFormulario()).build())
                .build();
    }

    public static Validacion toEntityCreate(ValidacionRequest validacionDTO, Usuario usuario, Formulario formulario) {
        return Validacion.builder()
                .fechaValidacion(validacionDTO.getFechaValidacion())
                .estado(validacionDTO.getEstado())           // enum
                .usuario(usuario)
                .formulario(formulario)
                .build();
    }

    public static ValidacionResponse toDTO(Validacion validacion) {
        return ValidacionResponse.builder()
                .idValidacion(validacion.getIdValidacion())
                .feachaValidacion(validacion.getFechaValidacion())
                .estado(validacion.getEstado())           // enum
                .codigoUsuario(validacion.getUsuario().getCodigoUsuario())
                .idFormulario(validacion.getFormulario().getIdFormulario())
                .build();
    }

    public static List<ValidacionResponse> toDTOList(List<Validacion> validaciones) {
        if (validaciones == null) {
            return List.of();                // opcional: evita NullPointerException
        }
        return validaciones.stream()
                .map(ValidacionMapper::toDTO)
                .toList();
    }
}

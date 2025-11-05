package com.pnc.project.utils.mappers;

import com.pnc.project.dto.request.actividad.ActividadRequest;
import com.pnc.project.dto.response.actividad.ActividadResponse;
import com.pnc.project.dto.response.validacion.ValidacionResponse;
import com.pnc.project.entities.Actividad;
import com.pnc.project.entities.Validacion;
import com.pnc.project.utils.enums.TipoActividad;

import java.util.List;

public class ActividadMapper {

    public static Actividad toEntityCreate(ActividadRequest req, TipoActividad tipo) {
        return Actividad.builder()
                .nombre(req.getNombreActividad())
                .tipoActividad(tipo)
                .build();
    }

    public static Actividad toEntityUpdate(ActividadRequest req, TipoActividad tipo) {
        return Actividad.builder()
                .idActividad(req.getIdActividad())
                .nombre(req.getNombreActividad())
                .tipoActividad(tipo)
                .build();
    }

    public static ActividadResponse toDTO(Actividad a) {
        return ActividadResponse.builder()
                .idActividad(a.getIdActividad())
                .nombreActividad(a.getNombre())
                .tipoActividad(a.getTipoActividad())
                .build();
    }

    public static List<ActividadResponse> toDTOList(List<Actividad> list) {
        return list.stream().map(ActividadMapper::toDTO).toList();
    }

    public static Actividad toEntity(ActividadResponse dto) {
        if (dto == null) {
            return null;               // evita NullPointerException
        }

        return Actividad.builder()
                .idActividad(dto.getIdActividad())       // ID
                .nombre(dto.getNombreActividad())        // enum ActividadNombre
                .tipoActividad(dto.getTipoActividad())   // enum TipoActividad
                .build();
    }

}
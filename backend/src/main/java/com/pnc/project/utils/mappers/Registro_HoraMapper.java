package com.pnc.project.utils.mappers;

import com.pnc.project.dto.request.registro_hora.Registro_HoraRequest;
import com.pnc.project.dto.response.registro_hora.Registro_HoraResponse;
import com.pnc.project.entities.*;
import com.pnc.project.utils.enums.ActividadNombre;

import java.util.List;

/**
 * Conversor entre la entidad Registro_Hora y sus DTOs.
 */
public final class Registro_HoraMapper {

    private Registro_HoraMapper() { }

    /* ------------------------------------------------------------------
     *   1) Response  ->  Entity   (rara vez se usa, pero lo mantenemos)
     * ------------------------------------------------------------------ */
    public static Registro_Hora toEntity(Registro_HoraResponse dto) {
        if (dto == null) return null;

        return Registro_Hora.builder()
                .idRegistroHora(dto.getIdRegistro())
                .fechaRegistro(dto.getFechaRegistro())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .horasEfectivas(dto.getHorasEfectivas())
                .aula(dto.getAula())
                .usuario(Usuario.builder()
                        .codigoUsuario(dto.getCodigoUsuario())
                        .build())
                .actividad(Actividad.builder()
                        .nombre(dto.getNombreActividad())   // enum
                        .build())
                .formulario(Formulario.builder()
                        .idFormulario(dto.getIdFormulario())
                        .build())
                .build();
    }

    /* ------------------------------------------------------------------
     *   2) Request  ->  Entity   (crear)
     * ------------------------------------------------------------------ */
    public static Registro_Hora toEntityCreate(Registro_HoraRequest dto,
                                               Usuario    usuario,
                                               Actividad  actividad,
                                               Formulario formulario) {

        return Registro_Hora.builder()
                .fechaRegistro(dto.getFechaRegistro())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .horasEfectivas(dto.getHorasEfectivas())
                .aula(dto.getAula())
                .usuario(usuario)
                .actividad(actividad)
                .formulario(formulario)
                .build();
    }

    /* ------------------------------------------------------------------
     *   3) Request  ->  Entity   (update)
     * ------------------------------------------------------------------ */
    public static Registro_Hora toEntityUpdate(Registro_HoraRequest dto,
                                               Usuario    usuario,
                                               Actividad  actividad,
                                               Formulario formulario) {

        return Registro_Hora.builder()
                .idRegistroHora(dto.getIdRegistro())
                .fechaRegistro(dto.getFechaRegistro())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .horasEfectivas(dto.getHorasEfectivas())
                .aula(dto.getAula())
                .usuario(usuario)
                .actividad(actividad)
                .formulario(formulario)
                .build();
    }

    /* ------------------------------------------------------------------
     *   4) Entity  ->  Response
     * ------------------------------------------------------------------ */
    public static Registro_HoraResponse toDTO(Registro_Hora rh) {
        // Obtener la materia del formulario
        String nombreMateria = "";
        if (rh.getFormulario() != null && rh.getFormulario().getMateria() != null) {
            nombreMateria = rh.getFormulario().getMateria().getNombreMateria();
        }
        
        return Registro_HoraResponse.builder()
                .idRegistro(rh.getIdRegistroHora())
                .fechaRegistro(rh.getFechaRegistro())
                .horaInicio(rh.getHoraInicio())
                .horaFin(rh.getHoraFin())
                .horasEfectivas(rh.getHorasEfectivas())
                .aula(rh.getAula())
                .codigoUsuario(rh.getUsuario().getCodigoUsuario())
                .nombreActividad(rh.getActividad().getNombre())          // enum
                .idFormulario(rh.getFormulario().getIdFormulario())
                .nombreMateria(nombreMateria)
                .estado(rh.getFormulario() != null ? rh.getFormulario().getEstado() : null)
                .build();
    }

    public static List<Registro_HoraResponse> toDTOList(List<Registro_Hora> list) {
        return list.stream()
                .map(Registro_HoraMapper::toDTO)
                .toList();
    }
}

package com.pnc.project.dto.request.registro_hora;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class Registro_HoraRequest {

    private Integer idRegistro;

    @NotNull(message = "La fecha de registro no puede ser nula")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaRegistro;

    @NotNull(message = "La hora de inicio no puede ser nula")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin no puede ser nula")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaFin;

    @NotNull(message = "Las horas efectivas no pueden ser nulas")
    private BigDecimal horasEfectivas;

    @NotNull(message = "El aula no puede ser nula")
    private String aula;

    @NotNull(message = "El código de usuario no puede ser nulo")
    private String codigoUsuario;


    @NotNull(message = "El id del formulario no puede ser nulo")
    private Integer idFormulario;

    @NotNull(message = "El id de actividad no puede ser nulo")
    private Integer idActividad;

}

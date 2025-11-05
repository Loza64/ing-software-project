package com.pnc.project.dto.response.registro_hora;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pnc.project.utils.enums.ActividadNombre;
import com.pnc.project.utils.enums.EstadoFormulario;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class Registro_HoraResponse {
    private Integer idRegistro;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaRegistro;
    
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaInicio;
    
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaFin;
    
    private BigDecimal horasEfectivas;
    private String aula;
    private String codigoUsuario;
    private ActividadNombre nombreActividad;
    private Integer idFormulario;
    private String nombreMateria;
    private EstadoFormulario estado;
}

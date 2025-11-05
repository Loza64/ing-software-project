package com.pnc.project.dto.response.validacion;

import com.pnc.project.utils.enums.EstadoValidacion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ValidacionResponse {
    private Integer idValidacion;
    private LocalDate feachaValidacion;
    private EstadoValidacion estado;  // ←  campo agregado
    private String codigoUsuario;
    private Integer idFormulario;
}

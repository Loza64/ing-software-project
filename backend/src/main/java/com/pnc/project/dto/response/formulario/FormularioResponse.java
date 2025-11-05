package com.pnc.project.dto.response.formulario;

import com.pnc.project.utils.enums.EstadoFormulario;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FormularioResponse {
    private Integer idFormulario;
    private LocalDate fechaCreacion;
    private EstadoFormulario estado;      // ← enum en lugar de String
    private String codigoUsuario;
    private Integer idMateria;
}

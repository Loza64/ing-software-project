package com.pnc.project.dto.request.formulario;

import com.pnc.project.utils.enums.EstadoFormulario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FormularioRequest {

    private Integer idFormulario;

    @NotNull(message = "La fecha de creación no puede ser nula")
    private LocalDate fechaCreacion;

    @NotNull(message = "El nombre del formulario no puede ser nulo")
    private EstadoFormulario estado;      // ← enum en lugar de String

    @NotNull(message = "El código de usuario no puede ser nulo")
    private String codigoUsuario;

    private Integer idMateria;
}

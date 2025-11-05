package com.pnc.project.dto.request.materia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MateriaRequest {
    private Integer idMateria;

    @NotNull(message = "El nombre de la materia no puede ser nulo")
    @NotBlank(message = "El nombre de la materia no puede estar vacío")
    private String nombreMateria;
}

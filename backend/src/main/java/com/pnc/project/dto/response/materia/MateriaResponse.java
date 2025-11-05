package com.pnc.project.dto.response.materia;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MateriaResponse {
    private Integer idMateria;
    private String nombreMateria;
}

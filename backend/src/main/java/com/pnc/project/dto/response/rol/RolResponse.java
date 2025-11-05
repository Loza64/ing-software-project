package com.pnc.project.dto.response.rol;

import com.pnc.project.utils.enums.RolNombre;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolResponse {
    private Integer   idRol;
    private RolNombre nombre;   // se serializa como String
}

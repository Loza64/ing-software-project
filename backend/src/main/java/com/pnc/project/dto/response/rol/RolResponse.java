package com.pnc.project.dto.response.rol;

import java.util.Set;

import com.pnc.project.entities.Permission;
import com.pnc.project.utils.enums.RolNombre;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolResponse {
    private Long idRol;
    private RolNombre nombre; // se serializa como String
    private Set<Permission> permisos;
}

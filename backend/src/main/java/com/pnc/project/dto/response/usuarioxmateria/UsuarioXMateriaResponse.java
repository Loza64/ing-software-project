package com.pnc.project.dto.response.usuarioxmateria;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioXMateriaResponse {
    private Integer idUsuarioXMateria;
    private String codigoUsuario;
    private String nombreMateria;
}

package com.pnc.project.dto.request.usuarioxmateria;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioXMateriaRequest {
    @NotNull(message = "Código de usuario no puede ser nulo")
    private String codigoUsuario;
    @NotNull(message = "El nombre de la materia no puede ser nulo")
    private String nombreMateria;
}

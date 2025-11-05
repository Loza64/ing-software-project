package com.pnc.project.dto.request.rol;

import com.pnc.project.utils.enums.RolNombre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolRequest {
    @Pattern(regexp = "ENCARGADO|INSTRUCTOR_NORMAL|INSTRUCTOR_REMUNERADO", message = "Rol no válido")
    @NotNull(message = "El nombre del rol es obligatorio")
    private RolNombre nombre;
}

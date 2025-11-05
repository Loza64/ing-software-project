package com.pnc.project.dto.request.actividad;

import com.pnc.project.utils.enums.ActividadNombre;
import com.pnc.project.utils.enums.RolNombre;
import com.pnc.project.utils.enums.TipoActividad;
import com.pnc.project.validation.ActividadRolValida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ActividadRolValida
public class ActividadRequest {

    private Integer idActividad;

    @NotNull(message = "El nombre de la actividad es obligatorio")
    private ActividadNombre nombreActividad;

    @NotNull(message = "El rol del instructor es obligatorio")
    private RolNombre rolInstructor;   // llega del front

    @NotNull(message = "El tipo es obligatorio")
    private TipoActividad tipoActividad;

}

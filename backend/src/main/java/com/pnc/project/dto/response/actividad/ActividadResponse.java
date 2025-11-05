package com.pnc.project.dto.response.actividad;

import com.pnc.project.utils.enums.ActividadNombre;
import com.pnc.project.utils.enums.TipoActividad;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActividadResponse {
    private Integer idActividad;
    private ActividadNombre nombreActividad;
    private TipoActividad tipoActividad;
}

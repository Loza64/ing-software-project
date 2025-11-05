package com.pnc.project.utils.enums;

import static com.pnc.project.utils.enums.RolNombre.*;

public enum ActividadNombre {

    /* INSTRUCTOR_SOCIAL */
    APOYO_PRACTICAS_LABORATORIO(INSTRUCTOR_NORMAL),
    CONSULTAS(INSTRUCTOR_NORMAL),
    APOYO_CLASE(INSTRUCTOR_NORMAL),
    APOYO_EN_PARCIAL(INSTRUCTOR_NORMAL),

    /* INSTRUCTOR_REMUNERADO */
    PRACTICA_LABORATORIO(INSTRUCTOR_REMUNERADO),
    PERMANENCIA(INSTRUCTOR_REMUNERADO),
    APOYO_PARCIAL(INSTRUCTOR_REMUNERADO),
    APOYO_INFORMATICO(INSTRUCTOR_REMUNERADO);

    private final RolNombre rolPermitido;

    ActividadNombre(RolNombre rolPermitido) {
        this.rolPermitido = rolPermitido;
    }

    public RolNombre getRolPermitido() {
        return rolPermitido;
    }
}
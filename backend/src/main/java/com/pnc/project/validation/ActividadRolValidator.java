package com.pnc.project.validation;

import com.pnc.project.dto.request.actividad.ActividadRequest;
import com.pnc.project.utils.enums.ActividadNombre;
import com.pnc.project.utils.enums.RolNombre;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class ActividadRolValidator implements ConstraintValidator<ActividadRolValida, ActividadRequest> {

    private static final Set<ActividadNombre> SOLO_NORMAL = Set.of(
            ActividadNombre.APOYO_PRACTICAS_LABORATORIO,
            ActividadNombre.CONSULTAS,
            ActividadNombre.APOYO_CLASE,
            ActividadNombre.APOYO_EN_PARCIAL
    );

    @Override
    public boolean isValid(ActividadRequest req, ConstraintValidatorContext ctx) {
        if (req == null || req.getNombreActividad() == null || req.getRolInstructor() == null)
            return true; // @NotNull se valida aparte

        RolNombre rol = req.getRolInstructor();
        boolean esNormal = SOLO_NORMAL.contains(req.getNombreActividad());

        return (rol == RolNombre.INSTRUCTOR_NORMAL && esNormal) ||
                (rol == RolNombre.INSTRUCTOR_REMUNERADO && !esNormal);
    }
}

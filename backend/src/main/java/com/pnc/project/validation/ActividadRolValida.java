package com.pnc.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActividadRolValidator.class)
public @interface ActividadRolValida {
    String message() default "La actividad elegida no está permitida para el rol dado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

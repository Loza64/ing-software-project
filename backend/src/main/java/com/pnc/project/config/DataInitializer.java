package com.pnc.project.config;

import com.pnc.project.entities.Actividad;
import com.pnc.project.entities.Rol;
import com.pnc.project.repository.ActividadRepository;
import com.pnc.project.repository.RolRepository;
import com.pnc.project.utils.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepo;
    private final ActividadRepository actRepo;

    @Override
    public void run(String... args) {

        /* ---------- 1)  Roles ---------- */
        for (RolNombre rn : RolNombre.values()) {
            rolRepo.findByNombre(rn)
                    .orElseGet(() -> rolRepo.save(Rol.builder().nombre(rn).build()));
        }

        /* ---------- 2)  Actividades ---------- */
        for (ActividadNombre an : ActividadNombre.values()) {
            actRepo.findByNombre(an).orElseGet(() -> {
                // El tipo se deriva del rol permitido (SOCIAL ↔ REMUNERADA)
                TipoActividad tipo = an.getRolPermitido() == RolNombre.INSTRUCTOR_NORMAL
                        ? TipoActividad.SOCIAL
                        : TipoActividad.REMUNERADA;

                return actRepo.save(
                        Actividad.builder()
                                .nombre(an)
                                .tipoActividad(tipo)
                                .build());
            });
        }
    }
}

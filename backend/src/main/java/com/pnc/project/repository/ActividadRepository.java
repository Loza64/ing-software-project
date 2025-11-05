package com.pnc.project.repository;

import com.pnc.project.entities.Actividad;
import com.pnc.project.utils.enums.ActividadNombre;
import com.pnc.project.utils.enums.TipoActividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
    Optional<Actividad> findByNombre(ActividadNombre nombre);
    List<Actividad> findByTipoActividad(TipoActividad tipoActividad);
}

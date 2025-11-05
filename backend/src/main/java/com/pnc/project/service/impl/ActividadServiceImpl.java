package com.pnc.project.service.impl;

import com.pnc.project.dto.request.actividad.ActividadRequest;
import com.pnc.project.dto.response.actividad.ActividadResponse;
import com.pnc.project.entities.Actividad;
import com.pnc.project.repository.ActividadRepository;
import com.pnc.project.service.ActividadService;
import com.pnc.project.utils.enums.RolNombre;
import com.pnc.project.utils.enums.TipoActividad;
import com.pnc.project.utils.mappers.ActividadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository repo;

    /* --------- reglas de negocio --------- */
    private TipoActividad deducirTipo(ActividadRequest req) {
        return req.getRolInstructor() == RolNombre.INSTRUCTOR_NORMAL
                ? TipoActividad.SOCIAL
                : TipoActividad.REMUNERADA;
    }

    /* CRUD */
    @Override
    public List<ActividadResponse> findAll() {
        return ActividadMapper.toDTOList(repo.findAll());
    }

    @Override
    public ActividadResponse save(ActividadRequest req) {
        TipoActividad tipo = deducirTipo(req);
        Actividad act = repo.save(ActividadMapper.toEntityCreate(req, tipo));
        return ActividadMapper.toDTO(act);
    }

    @Override
    public ActividadResponse update(ActividadRequest req) {
        TipoActividad tipo = deducirTipo(req);
        Actividad act = repo.save(ActividadMapper.toEntityUpdate(req, tipo));
        return ActividadMapper.toDTO(act);
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<ActividadResponse> findByTipo(TipoActividad tipo) {
        return ActividadMapper.toDTOList(repo.findByTipoActividad(tipo));
    }

    @Override
    public ActividadResponse findById(int id) {
        return repo.findById(id)
                .map(ActividadMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    }
}

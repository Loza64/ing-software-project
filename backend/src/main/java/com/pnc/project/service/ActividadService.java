package com.pnc.project.service;

import com.pnc.project.dto.request.actividad.ActividadRequest;
import com.pnc.project.dto.response.actividad.ActividadResponse;
import com.pnc.project.utils.enums.TipoActividad;

import java.util.List;

public interface ActividadService {
    List<ActividadResponse> findAll();
    ActividadResponse       save(ActividadRequest actividad);
    ActividadResponse       update(ActividadRequest actividad);
    void                    delete(int id);
    List<ActividadResponse> findByTipo(TipoActividad tipo);
    ActividadResponse       findById(int id);
}

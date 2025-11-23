package com.pnc.project.service;

import com.pnc.project.dto.request.rol.RolRequest;
import com.pnc.project.dto.response.rol.RolResponse;
import com.pnc.project.utils.enums.RolNombre;

import java.util.List;

public interface RolService {
    List<RolResponse> findAll();

    RolResponse findByName(RolNombre nombre); // ← RolNombre

    RolResponse update(int id, RolRequest dto);
}

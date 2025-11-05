package com.pnc.project.service.impl;

import com.pnc.project.dto.response.rol.RolResponse;
import com.pnc.project.repository.RolRepository;
import com.pnc.project.service.RolService;
import com.pnc.project.utils.enums.RolNombre;
import com.pnc.project.utils.mappers.RolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepo;

    /* ---------- listar todo ---------- */
    @Override
    public List<RolResponse> findAll() {
        return RolMapper.toDTOList(rolRepo.findAll());
    }

    /* ---------- buscar por nombre ---------- */
    @Override
    public RolResponse findByName(RolNombre nombre) {
        return rolRepo.findByNombre(nombre)
                .map(RolMapper::toDTO)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado: " + nombre));
    }

}

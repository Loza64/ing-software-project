package com.pnc.project.service.impl;

import com.pnc.project.dto.request.rol.RolRequest;
import com.pnc.project.dto.response.rol.RolResponse;
import com.pnc.project.entities.Permission;
import com.pnc.project.entities.Rol;
import com.pnc.project.repository.PermissionRepository;
import com.pnc.project.repository.RolRepository;
import com.pnc.project.service.RolService;
import com.pnc.project.utils.enums.RolNombre;
import com.pnc.project.utils.mappers.RolMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepo;
    private final PermissionRepository permissionRepository;

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
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre));
    }

    @Override
    public RolResponse update(int id, RolRequest dto) {
        Rol rol = rolRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
        rol.setNombre(dto.getNombre());
        if (dto.getPermisos() != null && !dto.getPermisos().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(
                    permissionRepository.findAllById(dto.getPermisos()));
            rol.setPermissions(permissions);
        }
        Rol actualizado = rolRepo.save(rol);
        return RolMapper.toDTO(actualizado);
    }

}

package com.pnc.project.utils.mappers;

import com.pnc.project.dto.request.rol.RolRequest;
import com.pnc.project.dto.response.rol.RolResponse;
import com.pnc.project.entities.Rol;

import java.util.List;

public final class RolMapper {

    private RolMapper() { }   // evitar instancias

    /* ---------- Request → Entity  (crear) ---------- */
    public static Rol toEntityCreate(RolRequest dto) {
        return Rol.builder()
                .nombre(dto.getNombre())
                .build();
    }

    /* ---------- Response → Entity  (opcional) ---------- */
    public static Rol toEntity(RolResponse dto) {
        return Rol.builder()
                .idRol(dto.getIdRol())
                .nombre(dto.getNombre())
                .build();
    }

    /* ---------- Entity → Response ---------- */
    public static RolResponse toDTO(Rol rol) {
        return RolResponse.builder()
                .idRol(rol.getIdRol())
                .nombre(rol.getNombre())
                .build();
    }

    public static List<RolResponse> toDTOList(List<Rol> list) {
        return list.stream()
                .map(RolMapper::toDTO)
                .toList();
    }
}

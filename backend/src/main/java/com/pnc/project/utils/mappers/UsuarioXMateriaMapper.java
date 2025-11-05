package com.pnc.project.utils.mappers;

import com.pnc.project.dto.request.usuarioxmateria.UsuarioXMateriaRequest;
import com.pnc.project.dto.response.usuarioxmateria.UsuarioXMateriaResponse;
import com.pnc.project.entities.Materia;
import com.pnc.project.entities.Usuario;
import com.pnc.project.entities.UsuarioXMateria;

import java.util.List;

public class UsuarioXMateriaMapper {
    public static UsuarioXMateria toEntity(UsuarioXMateriaResponse UxM_DTO) {
        return UsuarioXMateria.builder()
                .idUsuarioXMateria(UxM_DTO.getIdUsuarioXMateria())
                .usuario(Usuario.builder().codigoUsuario(UxM_DTO.getCodigoUsuario()).build())
                .materia(Materia.builder().nombreMateria(UxM_DTO.getNombreMateria()).build())
                .build();
    }

    public static UsuarioXMateria toEntityCreate(Usuario usuario, Materia materia) {
        return UsuarioXMateria.builder()
                .usuario(usuario)
                .materia(materia)
                .build();
    }

    public static UsuarioXMateriaResponse toDTO(UsuarioXMateria usuarioXMateria) {
        return UsuarioXMateriaResponse.builder()
                .idUsuarioXMateria(usuarioXMateria.getIdUsuarioXMateria())
                .codigoUsuario(usuarioXMateria.getUsuario().getCodigoUsuario())
                .nombreMateria(usuarioXMateria.getMateria().getNombreMateria())
                .build();
    }

    public static List<UsuarioXMateriaResponse> toDTOList(List<UsuarioXMateria> usuarioXMaterias) {
        return usuarioXMaterias.stream()
                .map(UsuarioXMateriaMapper::toDTO)
                .toList();
    }


}

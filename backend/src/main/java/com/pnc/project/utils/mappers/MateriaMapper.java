package com.pnc.project.utils.mappers;

import com.pnc.project.dto.request.materia.MateriaRequest;
import com.pnc.project.dto.response.materia.MateriaResponse;
import com.pnc.project.entities.Materia;

import java.util.List;

public class MateriaMapper {
    public static Materia toEntity(MateriaResponse materiaDTO){
        return Materia.builder()
                .idMateria(materiaDTO.getIdMateria())
                .nombreMateria(materiaDTO.getNombreMateria())
                .build();
    }

    public static Materia toEntityCreate(MateriaRequest materiaDTO){
        return Materia.builder()
                .nombreMateria(materiaDTO.getNombreMateria())
                .build();
    }

    public static Materia toEntityUpdate(MateriaRequest materiaDTO){
        return Materia.builder()
                .idMateria(materiaDTO.getIdMateria())
                .nombreMateria(materiaDTO.getNombreMateria())
                .build();
    }

    public static MateriaResponse toDTO(Materia materia){
        return MateriaResponse.builder()
                .idMateria(materia.getIdMateria())
                .nombreMateria(materia.getNombreMateria())
                .build();
    }

    public static List<MateriaResponse> toDTOList(List<Materia> materias) {
        return materias.stream()
                .map(MateriaMapper::toDTO)
                .toList();
    }
}

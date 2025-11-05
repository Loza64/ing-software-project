package com.pnc.project.service;

import com.pnc.project.dto.request.materia.MateriaRequest;
import com.pnc.project.dto.response.materia.MateriaResponse;

import java.util.List;

public interface MateriaService {
    //Listar materias
    List<MateriaResponse> findAll();

    //Obtener materia por ID
    MateriaResponse findById(int id);

    //Crear materia
    MateriaResponse save(MateriaRequest materia);
    //Actualiza materia
    MateriaResponse update(MateriaRequest materia);
    //Eliminar materia
    void delete(int id);
}

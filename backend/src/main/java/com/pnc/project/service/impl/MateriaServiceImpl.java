package com.pnc.project.service.impl;

import com.pnc.project.dto.request.materia.MateriaRequest;
import com.pnc.project.dto.response.materia.MateriaResponse;
import com.pnc.project.repository.MateriaRepository;
import com.pnc.project.service.MateriaService;
import com.pnc.project.utils.mappers.MateriaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaServiceImpl implements MateriaService {
    private final MateriaRepository materiaRepository;

    @Autowired
    public MateriaServiceImpl(MateriaRepository repository) {this.materiaRepository = repository;}

    @Override
    public List<MateriaResponse> findAll() {return MateriaMapper.toDTOList(materiaRepository.findAll());}

    @Override
    public MateriaResponse findById(int id) {
        return MateriaMapper.toDTO(materiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia not found")));
    }

    @Override
    public MateriaResponse save(MateriaRequest materia) {
        materia.setNombreMateria(materia.getNombreMateria().toUpperCase());
        return MateriaMapper.toDTO(materiaRepository.save(MateriaMapper.toEntityCreate(materia)));
    }

    @Override
    public MateriaResponse update(MateriaRequest materia) {
        materia.setNombreMateria(materia.getNombreMateria().toUpperCase());
        return MateriaMapper.toDTO(materiaRepository.save(MateriaMapper.toEntityUpdate(materia)));
    }

    @Override
    public void delete(int id) { materiaRepository.deleteById(id);}

}

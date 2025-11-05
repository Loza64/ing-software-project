package com.pnc.project.service.impl;

import com.pnc.project.dto.request.formulario.FormularioRequest;
import com.pnc.project.dto.response.formulario.FormularioResponse;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.dto.response.materia.MateriaResponse;
import com.pnc.project.entities.Usuario;
import com.pnc.project.entities.Materia;
import com.pnc.project.repository.FormularioRepository;
import com.pnc.project.service.FormularioService;
import com.pnc.project.service.UsuarioService;
import com.pnc.project.service.MateriaService;
import com.pnc.project.utils.mappers.FormularioMapper;
import com.pnc.project.utils.mappers.UsuarioMapper;
import com.pnc.project.utils.mappers.MateriaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormularioServiceImpl implements FormularioService {
    private final FormularioRepository formularioRepository;
    private final UsuarioService usuarioService;
    private final MateriaService materiaService;

    @Autowired
    public FormularioServiceImpl(FormularioRepository formularioRepository, UsuarioService usuarioService, MateriaService materiaService) {
        this.formularioRepository= formularioRepository;
        this.usuarioService = usuarioService;
        this.materiaService = materiaService;
    }

    @Override
    public List<FormularioResponse> findAllUsers(Usuario usuario) {
        return FormularioMapper.toDTOList(formularioRepository.findByUsuario(usuario));
    }

    @Override
    public FormularioResponse findById(int id) {
        return FormularioMapper.toDTO(formularioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formulario not found")));
    }

    @Override
    public FormularioResponse save(FormularioRequest formulario) {
        UsuarioResponse usuario = usuarioService.findByCodigo(formulario.getCodigoUsuario());
        Materia materia = null;
        if (formulario.getIdMateria() != null) {
            MateriaResponse materiaResponse = materiaService.findById(formulario.getIdMateria());
            materia = MateriaMapper.toEntity(materiaResponse);
        }
        return FormularioMapper.toDTO(formularioRepository.save(FormularioMapper.toEntityCreate(formulario, UsuarioMapper.toEntity(usuario), materia)));
    }

    @Override
    public FormularioResponse update(FormularioRequest formulario) {
        UsuarioResponse usuario = usuarioService.findByCodigo(formulario.getCodigoUsuario());
        Materia materia = null;
        if (formulario.getIdMateria() != null) {
            MateriaResponse materiaResponse = materiaService.findById(formulario.getIdMateria());
            materia = MateriaMapper.toEntity(materiaResponse);
        }
        return FormularioMapper.toDTO(formularioRepository.save(FormularioMapper.toEntityUpdate(formulario, UsuarioMapper.toEntity(usuario), materia)));
    }

    @Override
    public void delete(int id) {formularioRepository.deleteById(id);}

}

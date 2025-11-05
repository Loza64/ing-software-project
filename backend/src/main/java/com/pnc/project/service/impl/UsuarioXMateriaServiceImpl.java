package com.pnc.project.service.impl;

import com.pnc.project.dto.request.usuarioxmateria.UsuarioXMateriaRequest;
import com.pnc.project.dto.response.usuarioxmateria.UsuarioXMateriaResponse;
import com.pnc.project.entities.Materia;
import com.pnc.project.entities.Usuario;
import com.pnc.project.entities.UsuarioXMateria;
import com.pnc.project.repository.MateriaRepository;
import com.pnc.project.repository.UsuarioRepository;
import com.pnc.project.repository.UsuarioXMateriaRepository;
import com.pnc.project.service.UsuarioXMateriaService;
import com.pnc.project.utils.mappers.UsuarioXMateriaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioXMateriaServiceImpl implements UsuarioXMateriaService {
    private final UsuarioXMateriaRepository usuarioXMateriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MateriaRepository materiaRepository;

    @Autowired
    public UsuarioXMateriaServiceImpl(UsuarioXMateriaRepository usuarioXMateriaRepository,
                                      UsuarioRepository usuarioRepository,
                                      MateriaRepository materiaRepository) {
        this.usuarioXMateriaRepository = usuarioXMateriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.materiaRepository = materiaRepository;
    }

    @Override
    public UsuarioXMateriaResponse save(UsuarioXMateriaRequest usuarioXMateria) {
        Usuario usuario = usuarioRepository.findByCodigoUsuario(usuarioXMateria.getCodigoUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Materia materia = materiaRepository.findByNombreMateria(usuarioXMateria.getNombreMateria().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        Optional<UsuarioXMateria> existente = usuarioXMateriaRepository.findByUsuarioAndMateria(usuario, materia);
        if (existente.isPresent()) {
            throw new RuntimeException("La relación entre usuario y materia ya existe.");
        }

        return UsuarioXMateriaMapper.toDTO(usuarioXMateriaRepository.save(UsuarioXMateriaMapper.toEntityCreate(usuario, materia)));
    }

    @Override
    public void delete(int id) {
        usuarioXMateriaRepository.deleteById(id);
    }

    @Override
    public List<UsuarioXMateriaResponse> listUserMaterias(Materia materia) {
        List<UsuarioXMateria> relaciones = usuarioXMateriaRepository.findByMateria(materia);
        return relaciones.stream()
                .map(UsuarioXMateriaMapper::toDTO)
                .toList();
    }

    @Override
    public List<UsuarioXMateriaResponse> listMateriaUser(Usuario usuario) {
        List<UsuarioXMateria> relaciones = usuarioXMateriaRepository.findByUsuario(usuario);
        return relaciones.stream()
                .map(UsuarioXMateriaMapper::toDTO)
                .toList();
    }

    @Override
    public List<Usuario> findUsuariosByMateria(Integer idMateria) {
        return usuarioXMateriaRepository.findByMateria_IdMateria(idMateria)
                .stream()
                .map(UsuarioXMateria::getUsuario)
                .distinct()
                .toList();
    }



}

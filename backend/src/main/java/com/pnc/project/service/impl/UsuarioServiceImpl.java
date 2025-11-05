package com.pnc.project.service.impl;

import com.pnc.project.dto.request.usuario.UsuarioRequest;
import com.pnc.project.dto.response.rol.RolResponse;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.entities.Materia;
import com.pnc.project.entities.Rol;
import com.pnc.project.entities.Usuario;
import com.pnc.project.entities.UsuarioXMateria;
import com.pnc.project.repository.RolRepository;
import com.pnc.project.repository.UsuarioRepository;
import com.pnc.project.repository.UsuarioXMateriaRepository;
import com.pnc.project.service.RolService;
import com.pnc.project.service.UsuarioService;
import com.pnc.project.utils.mappers.RolMapper;
import com.pnc.project.utils.mappers.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioXMateriaRepository usuarioXMateriaRepository;
    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository repository, UsuarioXMateriaRepository usuarioXMateriaRepository, RolService rolService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = repository;
        this.usuarioXMateriaRepository = usuarioXMateriaRepository;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsuarioResponse> findAll(){
        return UsuarioMapper.toDTOList(usuarioRepository.findAll());
    }


    @Override
    public List<UsuarioResponse> findByMateriaId(Integer materiaId) {
        List<UsuarioXMateria> usuariosXMateria = usuarioXMateriaRepository.findByMateria_IdMateria(materiaId);

        return usuariosXMateria.stream()
                .map(UsuarioXMateria::getUsuario)
                .distinct()
                .map(UsuarioMapper::toDTO)
                .toList();
    }

    public List<UsuarioResponse> findByRolId(Integer rolId) {
        List<Usuario> usuarios = usuarioRepository.findByRol_IdRol(rolId);

        return usuarios.stream()
                .distinct()
                .map(UsuarioMapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioResponse findById(int id) {
        return UsuarioMapper.toDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario not found")));
    }

    @Override
    public UsuarioResponse findByCodigo(String codigo) {
        return UsuarioMapper.toDTO(usuarioRepository.findByCodigoUsuario(codigo)
                .orElseThrow(() -> new RuntimeException("Usuario not found")));
    }

    @Override
    public UsuarioResponse findByEmail(String email) {
        return UsuarioMapper.toDTO(usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario not found")));
    }

    @Override
    public UsuarioResponse save(UsuarioRequest usuario) {
        RolResponse rol = rolService.findByName(usuario.getRol());
        return UsuarioMapper.toDTO(usuarioRepository.save(UsuarioMapper.toEntityCreate(usuario, RolMapper.toEntity(rol))));
    }

    @Override
    public UsuarioResponse update(UsuarioRequest usuario) {
        // Obtener el usuario existente
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario not found"));
        
        RolResponse rol = rolService.findByName(usuario.getRol());
        
        // Actualizar solo los campos proporcionados
        usuarioExistente.setCodigoUsuario(usuario.getCodigoUsuario());
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setEmail(usuario.getCorreo());
        usuarioExistente.setRol(RolMapper.toEntity(rol));
        
        // Solo actualizar la contraseña si se proporciona una nueva
        if (usuario.getContrasena() != null && !usuario.getContrasena().trim().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getContrasena()));
        }
        
        return UsuarioMapper.toDTO(usuarioRepository.save(usuarioExistente));
    }

    @Override
    public void delete(int id) {usuarioRepository.deleteById(id);}


    @Override
    public UsuarioResponse login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if(usuario == null){
            return  null;
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return  null;
        }

        return UsuarioMapper.toDTO(usuario);
    }


}

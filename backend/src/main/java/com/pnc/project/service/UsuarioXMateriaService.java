package com.pnc.project.service;

import com.pnc.project.dto.request.usuarioxmateria.UsuarioXMateriaRequest;
import com.pnc.project.dto.response.materia.MateriaResponse;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.dto.response.usuarioxmateria.UsuarioXMateriaResponse;
import com.pnc.project.entities.Materia;
import com.pnc.project.entities.Usuario;

import java.util.List;

public interface UsuarioXMateriaService {
    //Asociar Usuario con Materia
    UsuarioXMateriaResponse save(UsuarioXMateriaRequest usuarioXMateria);
    //Eliminar UsuarioXMateria
    void delete(int idUsuarioXMateria);
    //Listar Usuarios por Materia
    List<UsuarioXMateriaResponse> listUserMaterias(Materia materia);
    //Listar Materias por Usuario
    List<UsuarioXMateriaResponse> listMateriaUser(Usuario usuario);


    List<Usuario> findUsuariosByMateria(Integer idMateria);
}

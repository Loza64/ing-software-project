package com.pnc.project.service;

import com.pnc.project.dto.request.usuario.UsuarioRequest;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.entities.Usuario;

import java.util.List;

public interface UsuarioService {
    // Listar usuarios
    List<UsuarioResponse> findAll();

    // Listar usuario por materia
    List<UsuarioResponse> findByMateriaId(Integer materiaId);

    // Obtener usuario por id
    UsuarioResponse findById(int id);

    Usuario infoAuthById(int id);

    // Obtener usuario por código
    UsuarioResponse findByCodigo(String codigo);

    // Obtener usuario por email
    UsuarioResponse findByEmail(String email);

    // Obtener usuario por rol
    List<UsuarioResponse> findByRolId(Integer rolId);

    // Crear usuario
    UsuarioResponse save(UsuarioRequest usuario);

    // Actualizar usuario
    UsuarioResponse update(UsuarioRequest usuario);

    // Eliminar usuario
    void delete(int id);

    // login
    UsuarioResponse login(String email, String password);

    // Recuperación de contraseña
    void forgotPassword(String email);

    // Validar token de recuperación de contraseña
    boolean validateResetToken(String token);

    // Cambiar contraseña con token
    void resetPassword(String token, String newPassword);

}

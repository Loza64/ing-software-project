package com.pnc.project.utils.mappers;

import com.pnc.project.dto.request.usuario.UsuarioRequest;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.entities.Rol;
import com.pnc.project.entities.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UsuarioMapper {
    public static Usuario toEntity(UsuarioResponse dto) {
        return Usuario.builder()
                .idUsuario(dto.getIdUsuario())
                .codigoUsuario(dto.getCodigoUsuario())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getCorreo())
                .rol(Rol.builder()
                        .nombre(dto.getRol())        // enum directo
                        .build())
                .build();
    }

    public static Usuario toEntityCreate(UsuarioRequest dto, Rol rol) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        return Usuario.builder()
                .codigoUsuario(dto.getCodigoUsuario())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getCorreo())
                .password(encoder.encode(dto.getContrasena()))
                .rol(rol)                            // entidad Rol ya buscada
                .build();
    }

    public static Usuario toEntityUpdate(UsuarioRequest usuarioDTO, Rol rol) {
        return Usuario.builder()
                .idUsuario(usuarioDTO.getIdUsuario())
                .codigoUsuario(usuarioDTO.getCodigoUsuario())
                .nombre(usuarioDTO.getNombre())
                .apellido(usuarioDTO.getApellido())
                .email(usuarioDTO.getCorreo())
                .password(usuarioDTO.getContrasena())
                .rol(rol)
                .build();
    }

    public static UsuarioResponse toDTO(Usuario usuario) {
        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .codigoUsuario(usuario.getCodigoUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correo(usuario.getEmail())
                .rol(usuario.getRol().getNombre())   // enum RolNombre
                .build();
    }

    public static List<UsuarioResponse> toDTOList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::toDTO)
                .toList();
    }
}

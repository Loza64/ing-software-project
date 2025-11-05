package com.pnc.project.dto.response.usuario;


import com.pnc.project.utils.enums.RolNombre;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {
    private int idUsuario;
    private String codigoUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private RolNombre rol;   // ya no String ni Integer

}

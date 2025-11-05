package com.pnc.project.dto.request.usuario;

import com.pnc.project.utils.enums.RolNombre;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioRequest {
    private Integer idUsuario;

    @NotNull(message = "El código de usuario no puede ser nulo")
    private String codigoUsuario;

    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;

    @NotNull(message = "El correo no puede ser nulo")
    @Email(message = "El correo debe ser una dirección de correo electrónico válida")
    private String correo;

    @NotNull(message = "La contraseña no puede ser nula")
    private String contrasena;

    @NotNull(message = "El rol no puede ser nulo")
    private RolNombre rol;   // ← enum en vez de String o Integer
}
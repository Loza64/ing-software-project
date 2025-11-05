package com.pnc.project.dto.request.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Login {
    @NotBlank(message = "Email es requerido")
    @Email(message = "Debe ser un email válido")
    private String email;

    @NotBlank(message = "Password es requerido")
    @Size(min = 2, message = "Password debe tener al menos 6 caracteres")
    private String password;
}
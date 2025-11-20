package com.pnc.project.dto.request.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "Token es requerido")
    private String token;

    @NotBlank(message = "Nueva contraseña es requerida")
    private String newPassword;
}



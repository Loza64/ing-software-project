package com.pnc.project.controllers;

import com.pnc.project.config.JwtConfig;
import com.pnc.project.dto.Response;
import com.pnc.project.dto.request.usuario.ForgotPasswordRequest;
import com.pnc.project.dto.request.usuario.Login;
import com.pnc.project.dto.request.usuario.ResetPasswordRequest;
import com.pnc.project.dto.request.usuario.ValidateTokenRequest;
import com.pnc.project.dto.request.usuario.UsuarioRequest;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.entities.Usuario;
import com.pnc.project.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtConfig jwt;

    // Listar todos los usuarios
    @GetMapping("/usuarios/list")
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        List<UsuarioResponse> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    // Listar usuarios por materia
    @GetMapping("/usuarios/materia")
    public ResponseEntity<List<UsuarioResponse>> findByMateria(@RequestParam("idMateria") int idMateria) {
        List<UsuarioResponse> usuarios = usuarioService.findByMateriaId(idMateria);
        return ResponseEntity.ok(usuarios);
    }

    // Obtener datos del usuario autenticado actual
    @GetMapping("/usuarios/me")
    public ResponseEntity<UsuarioResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            UsuarioResponse usuario = usuarioService.findByEmail(email);
            if (usuario != null) {
                return ResponseEntity.ok(usuario);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Obtener usuario por ID (solo para usuarios autenticados)
    @GetMapping("/usuarios/data/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Usuario authUser = (Usuario) authentication.getPrincipal();

        UsuarioResponse currentUser = usuarioService.findByEmail(authUser.getEmail());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boolean hasPermission = currentUser.getIdUsuario() == id;
        if (!hasPermission) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UsuarioResponse usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    // Obtener usuario por código
    @GetMapping("/usuarios/codigo/{codigo}")
    public ResponseEntity<UsuarioResponse> findByCodigo(@PathVariable String codigo) {
        UsuarioResponse usuario = usuarioService.findByCodigo(codigo);
        return ResponseEntity.ok(usuario);
    }

    // Obtener usuario por rol
    @GetMapping("/usuarios/rol")
    public ResponseEntity<List<UsuarioResponse>> findByRol(@RequestParam("idRol") int idRol) {
        List<UsuarioResponse> usuario = usuarioService.findByRolId(idRol);
        return ResponseEntity.ok(usuario);
    }

    // Crear un usuario
    @PostMapping("/save")
    public ResponseEntity<UsuarioResponse> save(@RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse usuario = usuarioService.save(usuarioRequest);
        return ResponseEntity.ok(usuario);
    }

    // Actualizar un usuario
    @PutMapping("/usuarios/update/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable int id, @RequestBody UsuarioRequest usuarioRequest) {
        usuarioRequest.setIdUsuario(id);
        UsuarioResponse usuarioActualizado = usuarioService.update(usuarioRequest);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // Eliminar un usuario por su ID
    @DeleteMapping("/usuarios/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Login de usuario
    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@Valid @RequestBody Login body) {
        UsuarioResponse user = usuarioService.login(body.getEmail(), body.getPassword());
        if (user != null) {
            String token = jwt.createToken(user);
            return Response.build(HttpStatus.ACCEPTED.value(), "welcome " + user.getNombre(), token);
        } else {
            return Response.build(HttpStatus.UNAUTHORIZED.value(), "email or password incorrect", null);
        }
    }

    // Solicitar recuperación de contraseña
    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            usuarioService.forgotPassword(request.getEmail());
            return Response.build(HttpStatus.OK.value(),
                    "Se ha enviado un email con las instrucciones para recuperar tu contraseña", null);
        } catch (RuntimeException e) {
            // Por seguridad, siempre devolvemos el mismo mensaje aunque el email no exista
            // Esto previene que se descubran emails registrados en el sistema
            // Los errores se registran en los logs del servicio
            return Response.build(HttpStatus.OK.value(),
                    "Si el email existe, recibirás un mensaje con las instrucciones para recuperar tu contraseña",
                    null);
        }
    }

    // Validar token de recuperación de contraseña (OPCIONAL - solo si necesitas
    // validar antes de mostrar el formulario)
    // Nota: El endpoint /reset-password ya valida el token automáticamente, este
    // endpoint es opcional
    @PostMapping("/validate-reset-token")
    public ResponseEntity<Object> validateResetToken(@Valid @RequestBody ValidateTokenRequest request) {
        try {
            boolean isValid = usuarioService.validateResetToken(request.getToken());
            if (isValid) {
                return Response.build(HttpStatus.OK.value(),
                        "Token válido", true);
            } else {
                return Response.build(HttpStatus.BAD_REQUEST.value(),
                        "Token inválido o expirado", false);
            }
        } catch (Exception e) {
            return Response.build(HttpStatus.BAD_REQUEST.value(),
                    "Error al validar el token: " + e.getMessage(), false);
        }
    }

    // Cambiar contraseña con token (valida el token automáticamente)
    // Este endpoint valida el token y cambia la contraseña en un solo paso
    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            usuarioService.resetPassword(request.getToken(), request.getNewPassword());
            return Response.build(HttpStatus.OK.value(),
                    "Contraseña actualizada exitosamente", null);
        } catch (RuntimeException e) {
            return Response.build(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(), null);
        } catch (Exception e) {
            return Response.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al cambiar la contraseña: " + e.getMessage(), null);
        }
    }
}

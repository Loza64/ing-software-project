package com.pnc.project.controllers;

import com.pnc.project.dto.request.usuarioxmateria.UsuarioXMateriaRequest;
import com.pnc.project.dto.response.usuarioxmateria.UsuarioXMateriaResponse;
import com.pnc.project.entities.Materia;
import com.pnc.project.entities.Usuario;
import com.pnc.project.service.UsuarioXMateriaService;
import com.pnc.project.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario-materias")
@RequiredArgsConstructor
public class UsuarioXMateriaController {

    private final UsuarioXMateriaService usuarioXMateriaService;
    private final UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<UsuarioXMateriaResponse> save(@Valid @RequestBody UsuarioXMateriaRequest usuarioXMateriaRequest) {
        UsuarioXMateriaResponse asignacion = usuarioXMateriaService.save(usuarioXMateriaRequest);
        return ResponseEntity.ok(asignacion);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int idUsuarioXMateria) {
        usuarioXMateriaService.delete(idUsuarioXMateria);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/materia/{idMateria}")
    public ResponseEntity<List<UsuarioXMateriaResponse>> listUserMaterias(@PathVariable("idMateria") int idMateria) {
        Materia materia = Materia.builder().idMateria(idMateria).build(); // Construcción de Materia básica.
        List<UsuarioXMateriaResponse> usuarios = usuarioXMateriaService.listUserMaterias(materia);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<UsuarioXMateriaResponse>> listMateriaUser(@PathVariable("idUsuario") int idUsuario) {
        Usuario usuario = Usuario.builder().idUsuario(idUsuario).build(); // Construcción de Usuario básico.
        List<UsuarioXMateriaResponse> materias = usuarioXMateriaService.listMateriaUser(usuario);
        return ResponseEntity.ok(materias);
    }

    @GetMapping("/usuario/codigo/{codigoUsuario}")
    public ResponseEntity<List<UsuarioXMateriaResponse>> listMateriaUserByCodigo(@PathVariable("codigoUsuario") String codigoUsuario) {
        try {
            Usuario usuario = Usuario.builder().codigoUsuario(codigoUsuario).build();
            List<UsuarioXMateriaResponse> materias = usuarioXMateriaService.listMateriaUser(usuario);
            return ResponseEntity.ok(materias);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
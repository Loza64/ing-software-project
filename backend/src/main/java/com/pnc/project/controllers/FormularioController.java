package com.pnc.project.controllers;

import com.pnc.project.dto.request.formulario.FormularioRequest;
import com.pnc.project.dto.response.formulario.FormularioResponse;
import com.pnc.project.entities.Usuario;
import com.pnc.project.service.FormularioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formularios")
@RequiredArgsConstructor
public class FormularioController {

    private final FormularioService formularioService;

    // Listar todos los formularios asignados a un usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<FormularioResponse>> findAllByUsuario(@PathVariable int idUsuario) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        List<FormularioResponse> formularios = formularioService.findAllUsers(usuario);
        return ResponseEntity.ok(formularios);
    }

    // Obtener un formulario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<FormularioResponse> findById(@PathVariable int id) {
        FormularioResponse formulario = formularioService.findById(id);
        if (formulario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(formulario);
    }

    // Crear un formulario
    @PostMapping
    public ResponseEntity<FormularioResponse> save(@RequestBody FormularioRequest formularioRequest) {
        FormularioResponse formulario = formularioService.save(formularioRequest);
        return ResponseEntity.ok(formulario);
    }

    // Actualizar un formulario
    @PutMapping("/{id}")
    public ResponseEntity<FormularioResponse> update(@PathVariable int id, @RequestBody FormularioRequest formularioRequest) {
        formularioRequest.setIdFormulario(id);
        FormularioResponse formularioActualizado = formularioService.update(formularioRequest);
        return ResponseEntity.ok(formularioActualizado);
    }

    // Eliminar un formulario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        formularioService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
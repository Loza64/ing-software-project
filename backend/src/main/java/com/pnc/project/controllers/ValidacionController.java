package com.pnc.project.controllers;

import com.pnc.project.dto.request.validacion.ValidacionRequest;
import com.pnc.project.dto.response.formulario.FormularioResponse;
import com.pnc.project.dto.response.validacion.ValidacionResponse;
import com.pnc.project.service.ValidacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/validaciones")
@RequiredArgsConstructor
public class ValidacionController {

    private final ValidacionService validacionService;

    @PostMapping
    public ResponseEntity<ValidacionResponse> save(@Valid @RequestBody ValidacionRequest validacionRequest) {
        ValidacionResponse validacion = validacionService.save(validacionRequest);
        return ResponseEntity.ok(validacion);
    }

    @PutMapping
    public ResponseEntity<ValidacionResponse> update(@Valid @RequestBody ValidacionRequest validacionRequest) {
        ValidacionResponse validacionActualizada = validacionService.update(validacionRequest);
        return ResponseEntity.ok(validacionActualizada);
    }

    @GetMapping("/formularios-pendientes")
    public ResponseEntity<List<FormularioResponse>> listarFormulariosPendientes() {
        List<FormularioResponse> formulariosPendientes = validacionService.listarFormulariosPendientes();
        return ResponseEntity.ok(formulariosPendientes);
    }

    @GetMapping("/encargado/{idEncargado}")
    public ResponseEntity<List<ValidacionResponse>> listarValidacionesPorEncargado(@PathVariable Integer idEncargado) {
        List<ValidacionResponse> validaciones = validacionService.listarValidacionesPorEncargado(idEncargado);
        return ResponseEntity.ok(validaciones);
    }

    @PostMapping("/rechazar")
    public ResponseEntity<Void> rechazarFormulario(
            @RequestParam Integer idFormulario,
            @RequestParam String observacion) {
        validacionService.rechazarFormulario(idFormulario, observacion);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/aprobar")
    public ResponseEntity<Void> aprobarFormulario(
            @RequestParam Integer idFormulario) {
        validacionService.aprobarFormulario(idFormulario);
        return ResponseEntity.noContent().build();
    }
}
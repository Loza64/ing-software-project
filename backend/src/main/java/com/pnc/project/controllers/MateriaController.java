package com.pnc.project.controllers;

import com.pnc.project.dto.request.materia.MateriaRequest;
import com.pnc.project.dto.response.materia.MateriaResponse;
import com.pnc.project.service.MateriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    // Listar todas las materias
    @GetMapping
    public ResponseEntity<List<MateriaResponse>> getAllMaterias() {
        List<MateriaResponse> materias = materiaService.findAll();
        return ResponseEntity.ok(materias);
    }

    // Crear una materia
    @PostMapping
    public ResponseEntity<MateriaResponse> createMateria(@Valid @RequestBody MateriaRequest materiaRequest) {
        MateriaResponse nuevaMateria = materiaService.save(materiaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMateria);
    }

    // Actualizar una materia
    @PutMapping("/{id}")
    public ResponseEntity<MateriaResponse> updateMateria(@PathVariable Integer id,
            @Valid @RequestBody MateriaRequest materiaRequest) {
        materiaRequest.setIdMateria(id);
        MateriaResponse materiaActualizada = materiaService.update(materiaRequest);
        return ResponseEntity.ok(materiaActualizada);
    }

    // Eliminar una materia por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMateria(@PathVariable Integer id) {
        materiaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
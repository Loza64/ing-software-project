package com.pnc.project.controllers;

import com.pnc.project.dto.response.rol.RolResponse;
import com.pnc.project.service.RolService;
import com.pnc.project.utils.enums.RolNombre;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<RolResponse>> findAll() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<RolResponse> findByName(@PathVariable RolNombre name) {
        return ResponseEntity.ok(rolService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Void> save() {
        throw new UnsupportedOperationException(
                "Operación no soportada: los roles son fijos.");
    }
}

package com.pnc.project.controllers;

import com.pnc.project.dto.request.actividad.ActividadRequest;
import com.pnc.project.dto.response.actividad.ActividadResponse;
import com.pnc.project.service.ActividadService;
import com.pnc.project.utils.enums.TipoActividad;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadService actividadService;

    // Listar todas las actividades
    @GetMapping
    public ResponseEntity<List<ActividadResponse>> findAll() {
        List<ActividadResponse> actividades = actividadService.findAll();
        return ResponseEntity.ok(actividades);
    }

    // Listar actividades por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ActividadResponse>> findByTipo(@PathVariable String tipo) {
        List<ActividadResponse> actividades = actividadService.findByTipo(TipoActividad.valueOf(tipo));
        return ResponseEntity.ok(actividades);
    }

    // Buscar actividad por ID
    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponse> findById(@PathVariable int id) {
        ActividadResponse actividad = actividadService.findById(id);
        if (actividad == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actividad);
    }

}
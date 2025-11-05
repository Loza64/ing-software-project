package com.pnc.project.controllers;

import com.pnc.project.dto.request.registro_hora.Registro_HoraRequest;
import com.pnc.project.dto.response.registro_hora.Registro_HoraResponse;
import com.pnc.project.service.Registro_HoraService;
import com.pnc.project.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/registros")
public class Registro_HoraController {

    private final Registro_HoraService registroHoraService;
    private final UsuarioService usuarioService;

    public Registro_HoraController(Registro_HoraService registroHoraService, UsuarioService usuarioService) {
        this.registroHoraService = registroHoraService;
        this.usuarioService = usuarioService;
    }

    // Obtener todos los registros de horas
    @GetMapping("/horas")
    public ResponseEntity<List<Registro_HoraResponse>> findAll() {
        List<Registro_HoraResponse> registros = registroHoraService.findAll();
        return ResponseEntity.ok(registros);
    }

    // Obtener un registro de hora por su ID
    @GetMapping("/horas/{id}")
    public ResponseEntity<Registro_HoraResponse> findById(@PathVariable("id") int id) {
        Registro_HoraResponse registro = registroHoraService.findById(id);
        return ResponseEntity.ok(registro);
    }

    // Crear un nuevo registro de hora
    @PostMapping("/horas")
    public ResponseEntity<Registro_HoraResponse> save(@Valid @RequestBody Registro_HoraRequest registroHoraRequest) {
        Registro_HoraResponse registroGuardado = registroHoraService.save(registroHoraRequest);
        return ResponseEntity.ok(registroGuardado);
    }

    // Endpoint temporal para pruebas sin autenticación
    @PostMapping("/test/horas")
    public ResponseEntity<Registro_HoraResponse> saveTest(@Valid @RequestBody Registro_HoraRequest registroHoraRequest) {
        System.out.println("=== TEST ENDPOINT ===");
        System.out.println("Fecha recibida: " + registroHoraRequest.getFechaRegistro());
        System.out.println("Tipo de fecha: " + (registroHoraRequest.getFechaRegistro() != null ? registroHoraRequest.getFechaRegistro().getClass().getName() : "null"));
        
        Registro_HoraResponse registroGuardado = registroHoraService.save(registroHoraRequest);
        
        System.out.println("Fecha en respuesta: " + registroGuardado.getFechaRegistro());
        System.out.println("=== FIN TEST ===");
        
        return ResponseEntity.ok(registroGuardado);
    }

    // Endpoint temporal para obtener registros pendientes (sin implementación completa)
    @GetMapping("/test/pendientes")
    public ResponseEntity<List<Registro_HoraResponse>> getPendientesTest() {
        try {
            // Por ahora retornamos una lista vacía para que no falle
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            System.out.println("Error en endpoint temporal: " + e.getMessage());
            return ResponseEntity.ok(List.of());
        }
    }

    // Actualizar un registro de hora existente
    @PutMapping("/horas/{id}")
    public ResponseEntity<Registro_HoraResponse> update(
            @PathVariable("id") int id,
            @Valid @RequestBody Registro_HoraRequest registroHoraRequest
    ) {
        registroHoraRequest.setIdRegistro(id); // Establece el ID del registro al DTO.
        Registro_HoraResponse registroActualizado = registroHoraService.update(registroHoraRequest);
        return ResponseEntity.ok(registroActualizado);
    }

    // Eliminar un registro de hora por su ID
    @DeleteMapping("/horas/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        registroHoraService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Listar registros por rango de fechas y usuario
    @GetMapping("/manage/horas/usuario/fecha")
    public ResponseEntity<List<Registro_HoraResponse>> findByDateRangeAndUsuario(
            @RequestParam("idUsuario") int idUsuario,
            @RequestParam("fechaInicio") String fechaInicio,
            @RequestParam("fechaFin") String fechaFin
    ) {
        List<Registro_HoraResponse> registros = registroHoraService.dateListByUsuarioAndRange(
                idUsuario, fechaInicio, fechaFin
        );
        return ResponseEntity.ok(registros);
    }

    // Listar registros por rango de fechas y código de usuario
    @GetMapping("/manage/horas/usuario/codigo/fecha")
    public ResponseEntity<List<Registro_HoraResponse>> findByDateRangeAndUsuarioCodigo(
            @RequestParam("codigoUsuario") String codigoUsuario,
            @RequestParam("fechaInicio") String fechaInicio,
            @RequestParam("fechaFin") String fechaFin
    ) {
        try {
            // Buscar el usuario por código
            var usuario = usuarioService.findByCodigo(codigoUsuario);
            List<Registro_HoraResponse> registros = registroHoraService.dateListByUsuarioAndRange(
                    usuario.getIdUsuario(), fechaInicio, fechaFin
            );
            return ResponseEntity.ok(registros);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Nuevos endpoints para el flujo de validaciones
    
    // Obtener registros pendientes
    @GetMapping("/horas/pendientes")
    public ResponseEntity<List<Registro_HoraResponse>> findPendientes() {
        List<Registro_HoraResponse> registros = registroHoraService.findPendientes();
        return ResponseEntity.ok(registros);
    }

    // Obtener registros validados (aprobados o rechazados)
    @GetMapping("/horas/validados")
    public ResponseEntity<List<Registro_HoraResponse>> findValidados() {
        List<Registro_HoraResponse> registros = registroHoraService.findValidados();
        return ResponseEntity.ok(registros);
    }

    // Obtener registros por estado
    @GetMapping("/horas/estado/{estado}")
    public ResponseEntity<List<Registro_HoraResponse>> findByEstado(@PathVariable("estado") String estado) {
        List<Registro_HoraResponse> registros = registroHoraService.findByEstado(estado);
        return ResponseEntity.ok(registros);
    }

    // Aprobar un registro
    @PutMapping("/horas/{id}/aprobar")
    public ResponseEntity<Registro_HoraResponse> aprobarRegistro(@PathVariable("id") int id) {
        Registro_HoraResponse registro = registroHoraService.aprobarRegistro(id);
        return ResponseEntity.ok(registro);
    }

    // Denegar un registro
    @PutMapping("/horas/{id}/denegar")
    public ResponseEntity<Registro_HoraResponse> denegarRegistro(
            @PathVariable("id") int id,
            @RequestBody(required = false) String observacion
    ) {
        Registro_HoraResponse registro = registroHoraService.denegarRegistro(id, observacion);
        return ResponseEntity.ok(registro);
    }
}
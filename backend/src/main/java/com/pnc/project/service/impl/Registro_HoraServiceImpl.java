package com.pnc.project.service.impl;

import com.pnc.project.dto.request.registro_hora.Registro_HoraRequest;
import com.pnc.project.dto.request.formulario.FormularioRequest;
import com.pnc.project.dto.response.actividad.ActividadResponse;
import com.pnc.project.dto.response.formulario.FormularioResponse;
import com.pnc.project.dto.response.registro_hora.Registro_HoraResponse;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.entities.Actividad;
import com.pnc.project.entities.Formulario;
import com.pnc.project.entities.Registro_Hora;
import com.pnc.project.entities.Usuario;
import com.pnc.project.repository.Registro_HoraRepository;
import com.pnc.project.service.ActividadService;
import com.pnc.project.service.FormularioService;
import com.pnc.project.service.Registro_HoraService;
import com.pnc.project.service.UsuarioService;
import com.pnc.project.utils.enums.EstadoFormulario;
import com.pnc.project.utils.enums.RolNombre;
import com.pnc.project.utils.mappers.ActividadMapper;
import com.pnc.project.utils.mappers.FormularioMapper;
import com.pnc.project.utils.mappers.Registro_HoraMapper;
import com.pnc.project.utils.mappers.UsuarioMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class Registro_HoraServiceImpl implements Registro_HoraService {

    private final Registro_HoraRepository registro_HoraRepository;
    private final UsuarioService usuarioService;
    private final ActividadService actividadService;
    private final FormularioService formularioService;

    public Registro_HoraServiceImpl(Registro_HoraRepository registro_HoraRepository,
                                    UsuarioService usuarioService,
                                    ActividadService actividadService,
                                    FormularioService formularioService)
    {
        this.registro_HoraRepository = registro_HoraRepository;
        this.usuarioService = usuarioService;
        this.actividadService = actividadService;
        this.formularioService = formularioService;
    }

    @Override
    public List<Registro_HoraResponse> findAll() {
        return Registro_HoraMapper.toDTOList(registro_HoraRepository.findAll());
    }

    @Override
    public Registro_HoraResponse findById(int id) {
        return Registro_HoraMapper.toDTO(registro_HoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de hora no encontrado")));
    }

    @Override
    public Registro_HoraResponse save(Registro_HoraRequest registroHora) {
        System.out.println("=== DEBUG: Guardando registro ===");
        System.out.println("Fecha recibida: " + registroHora.getFechaRegistro());
        System.out.println("Tipo de fecha: " + (registroHora.getFechaRegistro() != null ? registroHora.getFechaRegistro().getClass().getName() : "null"));
        
        UsuarioResponse usuarioDto = usuarioService.findByCodigo(registroHora.getCodigoUsuario());
        ActividadResponse actividadDto = actividadService.findById(registroHora.getIdActividad());
        FormularioResponse formularioDto = formularioService.findById(registroHora.getIdFormulario());

        Usuario usuario = UsuarioMapper.toEntity(usuarioDto);
        Actividad actividad = ActividadMapper.toEntity(actividadDto);
        Formulario formulario = FormularioMapper.toEntity(formularioDto);

        Registro_Hora entity = Registro_HoraMapper.toEntityCreate(registroHora, usuario, actividad, formulario);

        // Validación de fecha futura: solo usuarios con rol ENCARGADO pueden crear fechas futuras
        java.time.LocalDate today = java.time.LocalDate.now();
        if (entity.getFechaRegistro() != null && entity.getFechaRegistro().isAfter(today)) {
            if (usuarioDto == null || usuarioDto.getRol() != RolNombre.ENCARGADO) {
                throw new IllegalArgumentException("No se permiten fechas futuras");
            }
        }

        // Validaciones de formato y rango de horas
        if (entity.getHoraInicio() == null || entity.getHoraFin() == null) {
            throw new IllegalArgumentException("Hora inicio y hora fin son requeridas");
        }

        // Asegurar rango 00:00 - 23:59:59
        java.time.LocalTime min = java.time.LocalTime.MIN; // 00:00
        java.time.LocalTime max = java.time.LocalTime.of(23, 59, 59);
        if (entity.getHoraInicio().isBefore(min) || entity.getHoraInicio().isAfter(max) ||
                entity.getHoraFin().isBefore(min) || entity.getHoraFin().isAfter(max)) {
            throw new IllegalArgumentException("Horas fuera de rango permitido (00:00 - 23:59)");
        }

        long minutes = java.time.Duration.between(entity.getHoraInicio(), entity.getHoraFin()).toMinutes();
        if (minutes < 0) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }

        // Calcular horas efectivas y asignar (BigDecimal con 2 decimales)
        java.math.BigDecimal horas = java.math.BigDecimal.valueOf(minutes)
                .divide(java.math.BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);
        entity.setHorasEfectivas(horas);

        // Verificación previa para evitar registros duplicados (usuario, actividad, fecha, hora_inicio)
        if (entity.getUsuario() != null && entity.getActividad() != null && entity.getFechaRegistro() != null && entity.getHoraInicio() != null) {
            boolean exists = registro_HoraRepository.existsByUsuarioAndActividadAndFechaRegistroAndHoraInicio(
                    entity.getUsuario(), entity.getActividad(), entity.getFechaRegistro(), entity.getHoraInicio());
            if (exists) {
                throw new com.pnc.project.exception.DuplicateFieldException("registro_hora", "Ya existe un registro con la misma fecha, actividad y hora de inicio para este usuario");
            }
        }

        Registro_Hora savedEntity = registro_HoraRepository.save(entity);
        System.out.println("Fecha guardada: " + savedEntity.getFechaRegistro());
        
        Registro_HoraResponse response = Registro_HoraMapper.toDTO(savedEntity);
        System.out.println("Fecha en respuesta: " + response.getFechaRegistro());
        System.out.println("=== FIN DEBUG ===");
        
        return response;
    }

    @Override
    public Registro_HoraResponse update(Registro_HoraRequest registroHora) {

        Registro_Hora existente = registro_HoraRepository.findById(registroHora.getIdRegistro())
                .orElseThrow(() -> new RuntimeException("Registro de hora no encontrado"));

        UsuarioResponse usuarioDto = usuarioService.findByCodigo(registroHora.getCodigoUsuario());
        ActividadResponse actividadDto = actividadService.findById(registroHora.getIdActividad());
        FormularioResponse formularioDto = formularioService.findById(registroHora.getIdFormulario());

        Usuario usuario = UsuarioMapper.toEntity(usuarioDto);
        Actividad actividad = ActividadMapper.toEntity(actividadDto);
        Formulario formulario = FormularioMapper.toEntity(formularioDto);

        existente.setFechaRegistro(registroHora.getFechaRegistro());
        // Validación de fecha futura en update: solo ENCARGADO puede establecer fechas futuras
        java.time.LocalDate todayUpd = java.time.LocalDate.now();
        if (registroHora.getFechaRegistro() != null && registroHora.getFechaRegistro().isAfter(todayUpd)) {
            // obtener rol del usuario a partir del DTO
            UsuarioResponse usuarioDtoUpd = usuarioService.findByCodigo(registroHora.getCodigoUsuario());
            if (usuarioDtoUpd == null || usuarioDtoUpd.getRol() != RolNombre.ENCARGADO) {
                throw new IllegalArgumentException("No se permiten fechas futuras");
            }
        }
        existente.setHoraInicio(registroHora.getHoraInicio());
        existente.setHoraFin(registroHora.getHoraFin());
        // Validar y recalcular horas efectivas en backend
        if (registroHora.getHoraInicio() == null || registroHora.getHoraFin() == null) {
            throw new IllegalArgumentException("Hora inicio y hora fin son requeridas");
        }
        java.time.LocalTime min = java.time.LocalTime.MIN;
        java.time.LocalTime max = java.time.LocalTime.of(23, 59, 59);
        if (registroHora.getHoraInicio().isBefore(min) || registroHora.getHoraInicio().isAfter(max) ||
                registroHora.getHoraFin().isBefore(min) || registroHora.getHoraFin().isAfter(max)) {
            throw new IllegalArgumentException("Horas fuera de rango permitido (00:00 - 23:59)");
        }

        long minutesUpd = java.time.Duration.between(registroHora.getHoraInicio(), registroHora.getHoraFin()).toMinutes();
        if (minutesUpd < 0) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        java.math.BigDecimal horasUpd = java.math.BigDecimal.valueOf(minutesUpd)
                .divide(java.math.BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);
        existente.setHorasEfectivas(horasUpd);
        existente.setAula(registroHora.getAula());
        existente.setUsuario(usuario);
        existente.setActividad(actividad);
        existente.setFormulario(formulario);

        return Registro_HoraMapper.toDTO(registro_HoraRepository.save(existente));
    }

    @Override
    public void delete(int id) {
        registro_HoraRepository.deleteById(id);
    }

    @Override
    public List<Registro_HoraResponse> getUsuarioRequests(Usuario usuario) {
        List<Registro_Hora> registros = registro_HoraRepository.findByUsuario(usuario);
        return Registro_HoraMapper.toDTOList(registros);
    }

    @Override
    public List<Registro_HoraResponse> getFormularioRequests(Formulario formulario) {
        List<Registro_Hora> registros = registro_HoraRepository.findByFormulario(formulario);
        return Registro_HoraMapper.toDTOList(registros);
    }

    @Override
    public Registro_HoraResponse calcularHora(LocalDate inicio, LocalDate fin) {
        List<Registro_Hora> registros = registro_HoraRepository.findByFechaRegistroBetween(inicio, fin);

        BigDecimal totalHoras = registros.stream()
                .map(Registro_Hora::getHorasEfectivas)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Registro_HoraResponse.builder()
                .horasEfectivas(totalHoras)
                .build();
    }

    @Override
    public List<Registro_HoraResponse> dateList(Usuario usuario, LocalDate inicio, LocalDate fin) {
        List<Registro_Hora> registros = registro_HoraRepository
                .findByUsuarioAndFechaRegistroBetween(usuario, inicio, fin);
        return Registro_HoraMapper.toDTOList(registros);
    }

    @Override
    public List<Registro_HoraResponse> dateListByUsuarioAndRange(
            int idUsuario,
            String fechaInicio,
            String fechaFin) {
        
        DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE;  // yyyy-MM-dd
        LocalDate ini = LocalDate.parse(fechaInicio, fmt);
        LocalDate fin = LocalDate.parse(fechaFin, fmt);

        Usuario usuario = Usuario.builder()
                .idUsuario(idUsuario)
                .build();

        List<Registro_Hora> registros = registro_HoraRepository
                .findByUsuarioAndFechaRegistroBetween(usuario, ini, fin);

        return Registro_HoraMapper.toDTOList(registros);
    }

    @Override
    public List<Registro_HoraResponse> findPendientes() {
        List<Registro_Hora> registros = registro_HoraRepository.findByFormulario_Estado(EstadoFormulario.PENDIENTE);
        return Registro_HoraMapper.toDTOList(registros);
    }

    @Override
    public List<Registro_HoraResponse> findValidados() {
        List<Registro_Hora> registros = registro_HoraRepository.findByFormulario_EstadoIn(List.of(EstadoFormulario.APROBADO, EstadoFormulario.DENEGADO));
        return Registro_HoraMapper.toDTOList(registros);
    }

    @Override
    public List<Registro_HoraResponse> findByEstado(String estado) {
        EstadoFormulario estadoEnum;
        try {
            estadoEnum = EstadoFormulario.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado no válido: " + estado);
        }
        List<Registro_Hora> registros = registro_HoraRepository.findByFormulario_Estado(estadoEnum);
        return Registro_HoraMapper.toDTOList(registros);
    }

    @Override
    public Registro_HoraResponse aprobarRegistro(int id) {
        Registro_Hora registro = registro_HoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de hora no encontrado"));
        
        // Actualizar el estado del formulario a APROBADO
        Formulario formulario = registro.getFormulario();
        formulario.setEstado(EstadoFormulario.APROBADO);
        
        // Crear un FormularioRequest para actualizar
        FormularioRequest formularioRequest = FormularioRequest.builder()
                .idFormulario(formulario.getIdFormulario())
                .fechaCreacion(formulario.getFechaCreacion())
                .estado(EstadoFormulario.APROBADO)
                .codigoUsuario(formulario.getUsuario().getCodigoUsuario())
                .idMateria(formulario.getMateria() != null ? formulario.getMateria().getIdMateria() : null)
                .build();
        
        formularioService.update(formularioRequest);
        
        return Registro_HoraMapper.toDTO(registro);
    }

    @Override
    public Registro_HoraResponse denegarRegistro(int id, String observacion) {
        Registro_Hora registro = registro_HoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de hora no encontrado"));
        
        // Actualizar el estado del formulario a DENEGADO
        Formulario formulario = registro.getFormulario();
        formulario.setEstado(EstadoFormulario.DENEGADO);
        
        // Crear un FormularioRequest para actualizar
        FormularioRequest formularioRequest = FormularioRequest.builder()
                .idFormulario(formulario.getIdFormulario())
                .fechaCreacion(formulario.getFechaCreacion())
                .estado(EstadoFormulario.DENEGADO)
                .codigoUsuario(formulario.getUsuario().getCodigoUsuario())
                .idMateria(formulario.getMateria() != null ? formulario.getMateria().getIdMateria() : null)
                .build();
        
        formularioService.update(formularioRequest);
        
        return Registro_HoraMapper.toDTO(registro);
    }

}

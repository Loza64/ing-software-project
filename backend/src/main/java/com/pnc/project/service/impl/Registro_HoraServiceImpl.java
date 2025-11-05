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
        
        System.out.println("Fecha en entidad: " + entity.getFechaRegistro());
        System.out.println("Tipo de fecha en entidad: " + (entity.getFechaRegistro() != null ? entity.getFechaRegistro().getClass().getName() : "null"));

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
        existente.setHoraInicio(registroHora.getHoraInicio());
        existente.setHoraFin(registroHora.getHoraFin());
        existente.setHorasEfectivas(registroHora.getHorasEfectivas());
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

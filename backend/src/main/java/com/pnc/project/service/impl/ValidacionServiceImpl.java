package com.pnc.project.service.impl;

import com.pnc.project.dto.request.validacion.ValidacionRequest;
import com.pnc.project.dto.response.formulario.FormularioResponse;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.dto.response.validacion.ValidacionResponse;
import com.pnc.project.entities.Formulario;
import com.pnc.project.entities.Usuario;
import com.pnc.project.entities.Validacion;
import com.pnc.project.repository.ValidacionRepository;
import com.pnc.project.repository.FormularioRepository;
import com.pnc.project.repository.UsuarioRepository;
import com.pnc.project.service.FormularioService;
import com.pnc.project.service.UsuarioService;
import com.pnc.project.service.ValidacionService;
import com.pnc.project.utils.enums.EstadoFormulario;
import com.pnc.project.utils.enums.EstadoValidacion;
import com.pnc.project.utils.mappers.FormularioMapper;
import com.pnc.project.utils.mappers.UsuarioMapper;
import com.pnc.project.utils.mappers.ValidacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ValidacionServiceImpl implements ValidacionService {

    private final ValidacionRepository validacionRepository;
    private final UsuarioService usuarioService;
    private final FormularioService formularioService;
    private final UsuarioRepository usuarioRepository;
    private final FormularioRepository formularioRepository;

    @Autowired
    public ValidacionServiceImpl(
            ValidacionRepository validacionRepository,
            UsuarioService usuarioService,
            FormularioService formularioService,
            UsuarioRepository usuarioRepository,
            FormularioRepository formularioRepository) {
        this.validacionRepository = validacionRepository;
        this.usuarioService = usuarioService;
        this.formularioService = formularioService;
        this.usuarioRepository = usuarioRepository;
        this.formularioRepository = formularioRepository;
    }

    @Override
    public ValidacionResponse save(ValidacionRequest validacionRequest) {
        Usuario usuario = obtenerUsuarioDesdeCodigo(validacionRequest.getCodigoUsuario());
        Formulario formulario = obtenerFormularioDesdeId(validacionRequest.getIdFormulario());

        Validacion nuevaValidacion = ValidacionMapper.toEntityCreate(validacionRequest, usuario, formulario);
        Validacion savedValidacion = validacionRepository.save(nuevaValidacion);

        return ValidacionMapper.toDTO(savedValidacion);
    }

    @Override
    public ValidacionResponse findByFormulario(Formulario formulario) {
        Validacion validacion = validacionRepository.findByFormulario(formulario)
                .orElseThrow(() -> new RuntimeException("No se encontró validación para el formulario"));

        return ValidacionMapper.toDTO(validacion);
    }

    @Override
    public ValidacionResponse update(ValidacionRequest validacionRequest) {
        Validacion validacionExistente = validacionRepository.findById(validacionRequest.getIdValidacion())
                .orElseThrow(() -> new RuntimeException("Validación no encontrada con ID: " + validacionRequest.getIdValidacion()));

        Usuario usuario = obtenerUsuarioDesdeCodigo(validacionRequest.getCodigoUsuario());
        Formulario formulario = obtenerFormularioDesdeId(validacionRequest.getIdFormulario());

        validacionExistente.setUsuario(usuario);
        validacionExistente.setFormulario(formulario);
        validacionExistente.setEstado(validacionRequest.getEstado());
        validacionExistente.setFechaValidacion(validacionRequest.getFechaValidacion());

        Validacion updatedValidacion = validacionRepository.save(validacionExistente);
        return ValidacionMapper.toDTO(updatedValidacion);
    }

    @Override
    public List<FormularioResponse> listarFormulariosPendientes() {
        List<Formulario> formulariosPendientes = formularioRepository.findByEstado(EstadoFormulario.PENDIENTE);
        return FormularioMapper.toDTOList(formulariosPendientes);
    }

    @Override
    public List<ValidacionResponse> listarValidacionesPorEncargado(Integer idEncargado) {
        Usuario encargado = usuarioRepository.findById(idEncargado)
                .orElseThrow(() -> new RuntimeException("Encargado no encontrado"));

        List<Validacion> validaciones = validacionRepository.findByUsuario(encargado);
        return ValidacionMapper.toDTOList(validaciones);
    }

    @Override
    @Transactional
    public void rechazarFormulario(Integer idFormulario, String observacion) {

        Formulario formulario = formularioRepository.findById(idFormulario)
                .orElseThrow(() -> new RuntimeException("Formulario no encontrado"));

        /* 1) Cambiar estado del formulario */
        formulario.setEstado(EstadoFormulario.DENEGADO);   // enum, no String
        formularioRepository.save(formulario);

        /* 2) Crear la validación asociada */
        Validacion validacion = Validacion.builder()
                .fechaValidacion(LocalDate.now())
                .estado(EstadoValidacion.DENEGADA)         // enum
                .formulario(formulario)
                .usuario(null)    // TODO: asigna el usuario que rechaza, si aplica
                .build();

        validacionRepository.save(validacion);
    }

    @Override
    @Transactional
    public void aprobarFormulario(Integer idFormulario) {

        Formulario formulario = formularioRepository.findById(idFormulario)
                .orElseThrow(() -> new RuntimeException("Formulario no encontrado"));

        /* 1) Cambiar estado del formulario */
        formulario.setEstado(EstadoFormulario.APROBADO);
        formularioRepository.save(formulario);

        /* 2) Crear la validación asociada */
        Validacion validacion = Validacion.builder()
                .fechaValidacion(LocalDate.now())
                .estado(EstadoValidacion.APROBADA)
                .formulario(formulario)
                .usuario(null)    // TODO: asigna el usuario que aprueba, si aplica
                .build();

        validacionRepository.save(validacion);
    }

    // Métodos auxiliares

    private Usuario obtenerUsuarioDesdeCodigo(String codigoUsuario) {
        UsuarioResponse usuarioResponse = usuarioService.findByCodigo(codigoUsuario);
        return UsuarioMapper.toEntity(usuarioResponse);
    }

    private Formulario obtenerFormularioDesdeId(Integer idFormulario) {
        FormularioResponse formularioResponse = formularioService.findById(idFormulario);
        return FormularioMapper.toEntity(formularioResponse);
    }
}
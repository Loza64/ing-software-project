package com.pnc.project.service;

import com.pnc.project.dto.request.validacion.ValidacionRequest;
import com.pnc.project.dto.response.formulario.FormularioResponse;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.dto.response.validacion.ValidacionResponse;
import com.pnc.project.entities.Formulario;

import java.time.LocalDate;
import java.util.List;

public interface ValidacionService {

    //Crear Validación
    ValidacionResponse save(ValidacionRequest validacion);
    //Obtener Validación por Formulario
    ValidacionResponse findByFormulario(Formulario formulario);
    //Actualizar Validación
    ValidacionResponse update(ValidacionRequest validacion);


    //------HACEN FALTA------
    //listarFormulariosPendientes() (para encargado)
    //listarValidacionesPorEncargado(id_encargado)
    //rechazarFormulario(id_formulario, observacion)
    // Listar formularios pendientes (para encargado)
    List<FormularioResponse> listarFormulariosPendientes();

    // Listar validaciones de un encargado por su ID
    List<ValidacionResponse> listarValidacionesPorEncargado(Integer idEncargado);

    // Rechazar un formulario por ID con observación
    void rechazarFormulario(Integer idFormulario, String observacion);

    void aprobarFormulario(Integer idFormulario);

}

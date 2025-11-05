package com.pnc.project.service;

import com.pnc.project.dto.request.registro_hora.Registro_HoraRequest;
import com.pnc.project.dto.response.actividad.ActividadResponse;
import com.pnc.project.dto.response.formulario.FormularioResponse;
import com.pnc.project.dto.response.registro_hora.Registro_HoraResponse;
import com.pnc.project.dto.response.usuario.UsuarioResponse;
import com.pnc.project.entities.Formulario;
import com.pnc.project.entities.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface Registro_HoraService {
    List<Registro_HoraResponse> findAll();
    //Obtener por ID
    Registro_HoraResponse findById(int id);
    //Actualizar Registro
    Registro_HoraResponse update(Registro_HoraRequest registro_Hora);
    //Crear Registro
    Registro_HoraResponse save(Registro_HoraRequest registroHora);
    void delete(int id);
    //Listar registro por usuario
    List<Registro_HoraResponse> getUsuarioRequests(Usuario usuario);
    //Listar registros por formulario
    List<Registro_HoraResponse> getFormularioRequests(Formulario formulario);

    //Calcular horas efectivas
    Registro_HoraResponse calcularHora(LocalDate inicio, LocalDate fin);
    //Listar por fecha de rangos
    List<Registro_HoraResponse> dateList(Usuario usuario, LocalDate inicio, LocalDate fin);

    List<Registro_HoraResponse> dateListByUsuarioAndRange(int idUsuario, String fechaInicio, String fechaFin);

    // Nuevos métodos para el flujo de validaciones
    List<Registro_HoraResponse> findPendientes();
    List<Registro_HoraResponse> findValidados();
    List<Registro_HoraResponse> findByEstado(String estado);
    Registro_HoraResponse aprobarRegistro(int id);
    Registro_HoraResponse denegarRegistro(int id, String observacion);
}

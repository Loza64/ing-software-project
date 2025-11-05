package com.pnc.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "registro_hora")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Registro_Hora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro_hora")
    private Integer idRegistroHora;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "horas_efectivas")
    private BigDecimal horasEfectivas;

    @Column(name = "aula")
    private String aula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_actividad", referencedColumnName = "id_actividad")
    private Actividad actividad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_formulario", referencedColumnName = "id_formulario")
    private Formulario formulario;
}

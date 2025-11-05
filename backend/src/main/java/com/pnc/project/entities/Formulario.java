package com.pnc.project.entities;

import com.pnc.project.utils.enums.EstadoFormulario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "formulario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Formulario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formulario")
    private Integer idFormulario;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoFormulario estado = EstadoFormulario.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_materia", referencedColumnName = "id_materia")
    private Materia materia;

}

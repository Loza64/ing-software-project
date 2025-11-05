package com.pnc.project.entities;

import com.pnc.project.utils.enums.EstadoValidacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "validacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Validacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_validacion")
    private Integer idValidacion;

    @Column(name = "fecha_validacion")
    private LocalDate fechaValidacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoValidacion estado = EstadoValidacion.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_formulario", referencedColumnName = "id_formulario")
    private Formulario formulario;

}

package com.pnc.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "usuario_x_materia",  uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_materia"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class UsuarioXMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_x_materia")
    private Integer idUsuarioXMateria;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_materia", referencedColumnName = "id_materia")
    private Materia materia;


}

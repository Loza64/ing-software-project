package com.pnc.project.entities;

import com.pnc.project.utils.enums.ActividadNombre;
import com.pnc.project.utils.enums.TipoActividad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "actividad")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad")
    private Integer idActividad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ActividadNombre nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoActividad tipoActividad;
}

package com.pnc.project.entities;

import com.pnc.project.utils.enums.RolNombre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table (name = "rols")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder

public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Enumerated(EnumType.STRING)                // <-- nuevo
    @Column(nullable = false, unique = true)
    private RolNombre nombre;                   // <-- enum en vez de String
}

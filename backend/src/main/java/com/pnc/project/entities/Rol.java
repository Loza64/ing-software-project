package com.pnc.project.entities;

import java.util.Set;

import com.pnc.project.utils.enums.RolNombre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "rols")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder

public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RolNombre nombre;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Permission> permissions;
}

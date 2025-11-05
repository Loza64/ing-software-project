package com.pnc.project.repository;

import com.pnc.project.entities.Materia;
import com.pnc.project.entities.Rol;
import com.pnc.project.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCodigoUsuario(String codigoUsuario);
    Optional<Usuario> findByRol(Rol rol);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol_IdRol(Integer rolId);

}

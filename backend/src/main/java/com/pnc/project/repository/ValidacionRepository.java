package com.pnc.project.repository;

import com.pnc.project.entities.Formulario;
import com.pnc.project.entities.Usuario;
import com.pnc.project.entities.Validacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValidacionRepository extends JpaRepository<Validacion, Integer> {
    Optional<Validacion> findByFormulario(Formulario formulario);

    // Buscar validaciones por usuario
    List<Validacion> findByUsuario(Usuario usuario);
}

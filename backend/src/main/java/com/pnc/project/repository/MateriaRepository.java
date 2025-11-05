package com.pnc.project.repository;

import com.pnc.project.entities.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Integer> {
    Optional<Materia> findByNombreMateria(String nombreMateria);
}

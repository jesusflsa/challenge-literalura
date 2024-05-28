package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutoresRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreIgnoreCaseAndFechaNacimiento(String nombre, Integer fechaNacimiento);
    List<Autor> findAllByFechaFallecimientoIsGreaterThan(int fechaFallecimiento);
}

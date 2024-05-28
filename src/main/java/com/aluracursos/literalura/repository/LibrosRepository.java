package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibrosRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);
    List<Libro> findAllByIdioma(String idioma);
}

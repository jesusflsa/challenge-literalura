package com.aluracursos.literalura.service;

import com.aluracursos.literalura.models.Autor;
import com.aluracursos.literalura.models.DatosAutor;
import com.aluracursos.literalura.models.DatosLibro;
import com.aluracursos.literalura.models.Libro;
import com.aluracursos.literalura.repository.AutoresRepository;
import com.aluracursos.literalura.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LiteraluraService {
    @Autowired
    LibrosRepository librosRepository;

    @Autowired
    AutoresRepository autoresRepository;

    public Optional<Libro> buscarLibroPorNombre(String nombre) {
        return librosRepository.findByTituloContainsIgnoreCase(nombre);
    }

    public void guardarLibro(DatosLibro datosLibro) {
        List<Autor> autoresDelLibro = obtenerAutoresExistentes(datosLibro.autores());
        Libro libro = new Libro(datosLibro);
        libro.setAutores(autoresDelLibro);
        librosRepository.save(libro);
        System.out.println("----- NUEVO LIBRO -----\n");
        System.out.println(libro);
        System.out.println("-----------------------\n");
    }

    public List<Libro> obtenerTodosLosLibros() {
        return librosRepository.findAll();
    }

    public List<Autor> obtenerAutoresExistentes(List<DatosAutor> autores) {
        List<Autor> autoresObtenidos = new ArrayList<>();
        autores.forEach(autor -> {
            Optional<Autor> autorObtenido = autoresRepository.findByNombreIgnoreCaseAndFechaNacimiento(autor.nombre(), autor.fechaNacimiento());
            var nuevoAutor = autorObtenido.orElseGet(() -> autoresRepository.save(new Autor(autor)));
            autoresObtenidos.add(nuevoAutor);
        });
        return autoresObtenidos;
    }

    public List<Autor> obtenerTodosLosAutores() {
        return autoresRepository.findAll();
    }

    public List<Autor> obtenerAutoresPorAnho(int anho) {
        return autoresRepository.findAllByFechaFallecimientoIsGreaterThan(anho);
    }

    public List<Libro> obtenerLibrosPorIdioma(String idioma) {
        return librosRepository.findAllByIdioma(idioma);
    }
}

package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.models.Autor;
import com.aluracursos.literalura.models.DatosApi;
import com.aluracursos.literalura.models.DatosLibro;
import com.aluracursos.literalura.models.Libro;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.aluracursos.literalura.service.LiteraluraService;
import org.hibernate.Hibernate;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "http://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();

    private LiteraluraService libreria;

    public Principal(LiteraluraService libreria) {
        this.libreria = libreria;
    }

    public void mostrarMenu() {
        var codigo = -1;
        while (codigo != 0) {
            System.out.println("""
                    --------- MENU ---------
                                        
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """);
            System.out.println("Elija la opción a través de su número:");
            int opcion = -1;
            try {
                opcion = teclado.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivosEnDeterminadoAnho();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    codigo = 0;
                    System.out.println("Saliendo de la aplicación");
                    teclado.close();
                    break;
                default:
                    codigoInvalido();
                    break;
            }
        }
    }

    private void buscarLibro() {
        System.out.println("Ingrese el título del libro que desea buscar:");
        String nombre = teclado.next();
        teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombre.replace(" ", "+"));
        DatosApi resultados = conversor.obtenerDatos(json, DatosApi.class);

        if (resultados.resultados().isEmpty()) {
            System.out.println("No se encontró un libro con ese titulo");
            return;
        }
        ;
        DatosLibro datosLibro = resultados.resultados().get(0);

        Optional<Libro> libroBuscado = libreria.buscarLibroPorNombre(datosLibro.titulo());

        if (libroBuscado.isPresent()) {
            System.out.println("Este libro ya se encuentra en tu lista");
        } else {
            libreria.guardarLibro(datosLibro);
        }
    }

    private void listarLibros() {
        System.out.println("Listando libros...");
        List<Libro> libros = libreria.obtenerTodosLosLibros();
        imprimirLibros(libros);
    }

    private void listarAutores() {
        System.out.println("Listando autores...");
        List<Autor> autores = libreria.obtenerTodosLosAutores();
        imprimirAutores(autores);
    }

    private void listarAutoresVivosEnDeterminadoAnho() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar");
        int anho;
        try {
            anho = teclado.nextInt();
            List<Autor> autores = libreria.obtenerAutoresPorAnho(anho);
            imprimirAutores(autores);

        } catch (InputMismatchException e) {
            System.out.println("Indique un número válido");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingresa el código de idioma");
        String idioma = teclado.next();

        List<Libro> libros = libreria.obtenerLibrosPorIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No existen libros con ese código");
        } else {
            imprimirLibros(libros);
        }
    }


    private void codigoInvalido() {
        System.out.println("Código inválido");
    }

    private void imprimirAutores(List<Autor> autores) {
        autores.forEach(autor -> {
            Hibernate.initialize(autor.getLibros());

            System.out.println("-------- AUTOR --------\n");
            System.out.println(autor + "Libros: " + autor.getLibros().stream().map(Libro::getTitulo).toList() + "\n");
            System.out.println("-----------------------\n");
        });
    }

    private void imprimirLibros(List<Libro> libros) {
        libros.forEach(libro -> {
            System.out.println("-------- LIBRO --------\n");
            System.out.println(libro);
            System.out.println("-----------------------\n");
        });
    }

}


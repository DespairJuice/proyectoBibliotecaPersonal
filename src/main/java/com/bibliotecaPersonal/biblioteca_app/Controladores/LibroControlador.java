package com.bibliotecaPersonal.biblioteca_app.Controladores;

import com.bibliotecaPersonal.biblioteca_app.Persistencia.Entidades.Libro;
import com.bibliotecaPersonal.biblioteca_app.Servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<?> registrarLibro(@RequestBody Libro libro) {
        System.out.println("Intentando registrar libro: " + libro);
        try {
            Libro nuevo = libroServicio.registrarLibro(libro);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            e.printStackTrace(); // Esto mostrará el error real en consola
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{isbnOriginal}")
    public ResponseEntity<?> actualizarLibro(@PathVariable String isbnOriginal, @RequestBody Libro libro) {
        try {
            Libro actualizado = libroServicio.actualizarLibro(isbnOriginal, libro);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<?> eliminarLibro(@PathVariable String isbn) {
        try {
            libroServicio.eliminarLibro(isbn);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Libro> listarLibros() {
        return libroServicio.buscarPorTitulo(""); // Devuelve todos si vacío
    }

    @GetMapping("/buscar")
    public List<Libro> buscarLibros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String estadoLectura,
            @RequestParam(required = false) Integer calificacion
    ) {
        if (isbn != null && !isbn.isEmpty()) {
            Optional<Libro> libro = libroServicio.buscarPorIsbn(isbn);
            return libro.map(List::of).orElse(List.of());
        }
        if (titulo != null && !titulo.isEmpty()) {
            return libroServicio.buscarPorTitulo(titulo);
        }
        if (autor != null && !autor.isEmpty()) {
            return libroServicio.buscarPorAutor(autor);
        }
        if (estadoLectura != null && !estadoLectura.isEmpty()) {
            return libroServicio.filtrarPorEstadoLectura(estadoLectura);
        }
        if (calificacion != null) {
            return libroServicio.filtrarPorCalificacion(calificacion);
        }
        return libroServicio.buscarPorTitulo("");
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<?> obtenerLibro(@PathVariable String isbn) {
        Optional<Libro> libro = libroServicio.buscarPorIsbn(isbn);
        return libro.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

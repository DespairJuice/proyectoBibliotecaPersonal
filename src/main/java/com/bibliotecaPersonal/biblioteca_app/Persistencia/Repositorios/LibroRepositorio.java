package com.bibliotecaPersonal.biblioteca_app.Persistencia.Repositorios;

import com.bibliotecaPersonal.biblioteca_app.Persistencia.Entidades.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepositorio extends JpaRepository<Libro, String> {
	List<Libro> findByNombreContainingIgnoreCase(String nombre);
	List<Libro> findByAutorContainingIgnoreCase(String autor);
	List<Libro> findByEstadoLectura(String estadoLectura);
	List<Libro> findByCalificacion(int calificacion);
}

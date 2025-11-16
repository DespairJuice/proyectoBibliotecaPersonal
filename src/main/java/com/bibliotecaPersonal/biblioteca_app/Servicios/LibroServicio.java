
package com.bibliotecaPersonal.biblioteca_app.Servicios;

import com.bibliotecaPersonal.biblioteca_app.Persistencia.Entidades.Libro;
import com.bibliotecaPersonal.biblioteca_app.Persistencia.Repositorios.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LibroServicio {
	@Autowired
	private LibroRepositorio libroRepositorio;

	public Libro registrarLibro(Libro libro) throws Exception {
		validarLibro(libro, true);
		libro.setUltimaActualizacion(LocalDateTime.now());
		return libroRepositorio.save(libro);
	}

	public Libro actualizarLibro(String isbnOriginal, Libro libroActualizado) throws Exception {
		Optional<Libro> libroExistenteOpt = libroRepositorio.findById(isbnOriginal);
		if (libroExistenteOpt.isEmpty()) {
			throw new Exception("Libro no encontrado");
		}
		Libro libroExistente = libroExistenteOpt.get();
		validarLibro(libroActualizado, false, isbnOriginal);
		// Copiar campos actualizables al objeto existente
		libroExistente.setNombre(libroActualizado.getNombre());
		libroExistente.setAutor(libroActualizado.getAutor());
		libroExistente.setEstadoLectura(libroActualizado.getEstadoLectura());
		libroExistente.setCalificacion(libroActualizado.getCalificacion());
		libroExistente.setNotasPersonales(libroActualizado.getNotasPersonales());
		libroExistente.setIsbn(libroActualizado.getIsbn()); // En caso de cambio de ISBN
		libroExistente.setUltimaActualizacion(LocalDateTime.now());
		return libroRepositorio.save(libroExistente);
	}

	public void eliminarLibro(String isbn) throws Exception {
		if (!libroRepositorio.existsById(isbn)) {
			throw new Exception("Libro no encontrado");
		}
		libroRepositorio.deleteById(isbn);
	}

	public List<Libro> buscarPorTitulo(String titulo) {
		return libroRepositorio.findByNombreContainingIgnoreCase(titulo);
	}

	public List<Libro> buscarPorAutor(String autor) {
		return libroRepositorio.findByAutorContainingIgnoreCase(autor);
	}

	public Optional<Libro> buscarPorIsbn(String isbn) {
		return libroRepositorio.findById(isbn);
	}

	public List<Libro> filtrarPorEstadoLectura(String estado) {
		return libroRepositorio.findByEstadoLectura(estado);
	}

	public List<Libro> filtrarPorCalificacion(int calificacion) {
		return libroRepositorio.findByCalificacion(calificacion);
	}

	private void validarLibro(Libro libro, boolean esNuevo) throws Exception {
		validarLibro(libro, esNuevo, null);
	}

	private void validarLibro(Libro libro, boolean esNuevo, String isbnOriginal) throws Exception {
		if (libro.getNombre() == null || libro.getNombre().trim().isEmpty() || libro.getNombre().length() > 200) {
			throw new Exception("El título es obligatorio y debe tener entre 1 y 200 caracteres");
		}
		if (libro.getAutor() == null || libro.getAutor().trim().isEmpty() || libro.getAutor().length() > 100) {
			throw new Exception("El autor es obligatorio y debe tener entre 1 y 100 caracteres");
		}
		if (libro.getIsbn() == null || !esIsbnValido(libro.getIsbn())) {
			throw new Exception("Formato ISBN inválido");
		}
		if (esNuevo || (isbnOriginal != null && !libro.getIsbn().equals(isbnOriginal))) {
			if (libroRepositorio.existsById(libro.getIsbn())) {
				throw new Exception("ISBN ya existe en su biblioteca");
			}
		}
		if (libro.getNotasPersonales() != null && libro.getNotasPersonales().length() > 1000) {
			throw new Exception("Notas personales no puede exceder 1000 caracteres");
		}
	}

	private boolean esIsbnValido(String isbn) {
		String cleanIsbn = isbn.replaceAll("[- ]", "");
		if (cleanIsbn.length() == 10) {
			return cleanIsbn.matches("\\d{9}[\\dXx]") && validarIsbn10(cleanIsbn);
		} else if (cleanIsbn.length() == 13) {
			return cleanIsbn.matches("\\d{13}") && validarIsbn13(cleanIsbn);
		}
		return false;
	}

	private boolean validarIsbn10(String isbn) {
		int suma = 0;
		for (int i = 0; i < 9; i++) {
			suma += (isbn.charAt(i) - '0') * (10 - i);
		}
		char ultimo = isbn.charAt(9);
		suma += (ultimo == 'X' || ultimo == 'x') ? 10 : (ultimo - '0');
		return suma % 11 == 0;
	}

	private boolean validarIsbn13(String isbn) {
		int suma = 0;
		for (int i = 0; i < 12; i++) {
			int digito = isbn.charAt(i) - '0';
			suma += (i % 2 == 0) ? digito : digito * 3;
		}
		int digitoControl = (10 - (suma % 10)) % 10;
		return digitoControl == (isbn.charAt(12) - '0');
	}
}

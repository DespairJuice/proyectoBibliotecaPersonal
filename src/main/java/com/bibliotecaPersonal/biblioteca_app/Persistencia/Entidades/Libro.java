package com.bibliotecaPersonal.biblioteca_app.Persistencia.Entidades;

import jakarta.persistence.*;

import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table (name = "libros")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class Libro {
    @Id
    private String isbn;

    private String nombre;
    private String autor;
    private String estadoLectura; // Leído, Leyendo, Por leer
    private int calificacion; // 1-5, 0 por defecto
    private String notasPersonales;
    private LocalDateTime ultimaActualizacion;
}

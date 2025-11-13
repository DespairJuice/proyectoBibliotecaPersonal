package com.bibliotecaPersonal.biblioteca_app.Controladores;

import com.bibliotecaPersonal.biblioteca_app.Persistencia.Entidades.Libro;
import com.bibliotecaPersonal.biblioteca_app.Servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeControlador {

    @Autowired
    private LibroServicio libroServicio;

    @GetMapping("/")
    public List<Libro> home() {
        return libroServicio.buscarPorTitulo(""); // Devuelve todos si vacío
    }
}

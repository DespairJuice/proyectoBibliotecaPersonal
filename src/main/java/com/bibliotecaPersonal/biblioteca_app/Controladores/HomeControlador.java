package com.bibliotecaPersonal.biblioteca_app.Controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeControlador {

    @GetMapping("/api")
    public String home() {
        return "Welcome to Biblioteca Personal API";
    }
}

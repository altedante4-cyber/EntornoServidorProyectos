package com.example.d4un2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class I18nController {

    @GetMapping("/")
    public String home( Model model) {

        model.addAttribute("titulo", "home.titulo");
        model.addAttribute("bienvenido", "home.bienvenido");
        model.addAttribute("mensaje", "home.mensaje");
        model.addAttribute("idioma", "home.idioma");
        return "home";
    }
}

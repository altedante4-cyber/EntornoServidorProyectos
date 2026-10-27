package com.example.d4un2;

import jakarta.websocket.server.PathParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Controller
public class CodificacionController {

    @GetMapping("/formulario")
    public String formulario(Model model) {
        model.addAttribute("titulo", "Prueba de codificación");
        return "formulario";
    }


    @GetMapping("/resultado-get")
    @ResponseBody
    public String resultadoGet(@RequestParam(required = false, defaultValue = "(sin valor)") String texto) {
        return "<h2>Resultado GET</h2>" +
                "<p><strong>Vía:</strong> GET (query-string)</p>" +
                "<p><strong>Texto recibido:</strong> " + texto + "</p>" +
                "<p><a href='/formulario'>Volver al formulario</a></p>";
    }

    @PostMapping("/resultado-post")
    @ResponseBody
    public String resultadoPost(@RequestParam(required = false, defaultValue = "(sin valor)") String texto) {
        return "<h2>Resultado POST</h2>" +
                "<p><strong>Vía:</strong> POST (cuerpo de la petición)</p>" +
                "<p><strong>Texto recibido:</strong> " + texto + "</p>" +
                "<p><a href='/formulario'>Volver al formulario</a></p>";
    }


    @GetMapping("/enlace")
    @ResponseBody
    public String enlace(@RequestParam(required = false, defaultValue = "(sin valor)") String texto) {
        String textoCodificado = URLEncoder.encode(texto, StandardCharsets.UTF_8);
        String urlResultante = "/resultado-get?texto=" + textoCodificado;

        return "<h2>Enlace codificado</h2>" +
                "<p><strong>Texto original:</strong> " + texto + "</p>" +
                "<p><strong>Texto codificado:</strong> " + textoCodificado + "</p>" +
                "<p><strong>URL resultante:</strong> <a href='" + urlResultante + "'>" + urlResultante + "</a></p>" +
                "<p><a href='/formulario'>Volver al formulario</a></p>";
    }




}

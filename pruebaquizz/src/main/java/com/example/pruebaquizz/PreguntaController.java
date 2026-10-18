package com.example.pruebaquizz;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PreguntaController {
     @GetMapping("/")
    public String primerpregunta(Model model){
          model.addAttribute("primerapregunta",new Preguntas());
          return "primerapregunta";
     }
    @PostMapping("/pregunta1")
    public String formuno(@Valid @ModelAttribute("primerapregunta") Preguntas pregunta , BindingResult result){
         if(result.hasErrors()){
             return "primerapregunta";
         }
         return "segundapregunta";
    }
    @PostMapping("/pregunta2")
    public String fordos(@Valid@ModelAttribute("primerapregunta") Preguntas pregunta ,BindingResult result ){
            if(result.hasErrors()){
                return "segundapregunta";
            }
            return "tercerapregunta";

    }
    @PostMapping("/pregunta3")
    public String fortres(@Valid @ModelAttribute("primerapregunta") Preguntas pregunta ,BindingResult result ,Model model ){

            if(result.hasErrors()){
                return "tercerapregunta";
            }
        List<Preguntas>respuestas = new ArrayList<>();
            respuestas.add(pregunta);
            model.addAttribute("pre",respuestas);
         return "resultado";

    }
    @GetMapping("/volver/pregunta1")
    public String volverPreguntaUno(
            @ModelAttribute("primerapregunta") Preguntas pregunta) {

        return "primerapregunta";
    }
    @GetMapping("/volver/pregunta2")
    public String volverPregunta2(
            @ModelAttribute("primerapregunta") Preguntas pregunta) {

        return "segundapregunta";
    }
    @GetMapping("/volver/pregunta3")
    public String volverPregunta3(
            @ModelAttribute("primerapregunta") Preguntas pregunta) {

        return "tercerapregunta";
    }


}


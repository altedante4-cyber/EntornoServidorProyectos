package goya.daw2.pruebas_plantillas;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CookiesController {

    private static final int DURACION = 60 * 60;
    private static final String NOMBRE = "quiz_nombre";
    private static final String APELLIDO = "quiz_apellido";
    private static final String EDAD = "quiz_edad";
    private static final String GENERO = "quiz_genero";
    private static final String AFICIONES = "quiz_aficiones";
    private static final String[] TODAS = {NOMBRE, APELLIDO, EDAD, GENERO, AFICIONES};
    private static final List<String> LISTA_AFICIONES = List.of(
            "Gimnasio", "Fútbol", "Ciclismo", "Videojuegos", "Música");

    @GetMapping("/")
    public String inicio() {
        return "inicio";
    }

    @GetMapping("/v3")
    public String empezar(HttpServletResponse respuesta) {
        borrarCookies(respuesta);
        return "redirect:/v3/paso1";
    }

    @GetMapping("/v3/paso1")
    public String paso1(HttpServletRequest peticion, Model modelo) {
        return vista(modelo, leer(peticion), null, "paso1");
    }

    @PostMapping("/v3/paso1")
    public String procesarPaso1(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "apellido", required = false) String apellido,
            @RequestParam(value = "edad", required = false) String edad,
            HttpServletResponse respuesta,
            Model modelo) {

        Map<String, Object> datos = datosVacios();
        datos.put("nombre", nombre == null ? "" : nombre);
        datos.put("apellido", apellido == null ? "" : apellido);
        datos.put("edad", edad == null ? "" : edad);

        String error = validarPaso1(datos);
        if (error != null) {
            return vista(modelo, datos, error, "paso1");
        }

        guardar(respuesta, NOMBRE, nombre);
        guardar(respuesta, APELLIDO, apellido);
        guardar(respuesta, EDAD, edad);
        return "redirect:/v3/paso2";
    }

    @GetMapping("/v3/paso2")
    public String paso2(HttpServletRequest peticion, Model modelo) {
        Map<String, Object> datos = leer(peticion);
        if (!paso1Completo(datos)) {
            return "redirect:/v3/paso1";
        }
        return vista(modelo, datos, null, "paso2");
    }

    @PostMapping("/v3/paso2")
    public String procesarPaso2(
            @RequestParam(value = "genero", required = false) String genero,
            HttpServletRequest peticion,
            HttpServletResponse respuesta,
            Model modelo) {

        Map<String, Object> datos = leer(peticion);
        if (!paso1Completo(datos)) {
            return "redirect:/v3/paso1";
        }

        datos.put("genero", genero == null ? "" : genero);
        String error = validarPaso2(datos);
        if (error != null) {
            return vista(modelo, datos, error, "paso2");
        }

        guardar(respuesta, GENERO, genero);
        return "redirect:/v3/paso3";
    }

    @GetMapping("/v3/paso3")
    public String paso3(HttpServletRequest peticion, Model modelo) {
        Map<String, Object> datos = leer(peticion);
        if (!paso2Completo(datos)) {
            return "redirect:/v3/paso1";
        }
        return vista(modelo, datos, null, "paso3");
    }

    @PostMapping("/v3/paso3")
    public String procesarPaso3(
            @RequestParam(value = "aficiones", required = false) List<String> aficiones,
            HttpServletRequest peticion,
            HttpServletResponse respuesta,
            Model modelo) {

        Map<String, Object> datos = leer(peticion);
        if (!paso2Completo(datos)) {
            return "redirect:/v3/paso1";
        }

        datos.put("aficiones", aficiones == null ? new ArrayList<String>() : aficiones);
        String error = validarPaso3(datos);
        if (error != null) {
            return vista(modelo, datos, error, "paso3");
        }

        guardar(respuesta, AFICIONES, String.join("|", aficiones));
        return "redirect:/v3/resultado";
    }

    @GetMapping("/v3/resultado")
    public String resultado(HttpServletRequest peticion, Model modelo) {
        Map<String, Object> datos = leer(peticion);
        if (!todoCompleto(datos)) {
            return "redirect:/v3/paso1";
        }
        return vista(modelo, datos, null, "resultado");
    }

    @GetMapping("/v3/reiniciar")
    public String reiniciar(HttpServletResponse respuesta) {
        borrarCookies(respuesta);
        return "redirect:/v3";
    }

    private Map<String, Object> leer(HttpServletRequest peticion) {
        Map<String, Object> datos = datosVacios();
        Cookie[] cookies = peticion.getCookies();
        if (cookies == null) {
            return datos;
        }
        for (Cookie cookie : cookies) {
            String valor = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
            switch (cookie.getName()) {
                case NOMBRE -> datos.put("nombre", valor);
                case APELLIDO -> datos.put("apellido", valor);
                case EDAD -> datos.put("edad", valor);
                case GENERO -> datos.put("genero", valor);
                case AFICIONES -> datos.put("aficiones", valor.isBlank()
                        ? new ArrayList<String>()
                        : new ArrayList<>(List.of(valor.split("\\|"))));
                default -> {
                }
            }
        }
        return datos;
    }

    private void guardar(HttpServletResponse respuesta, String nombre, String valor) {
        Cookie cookie = new Cookie(nombre,
                URLEncoder.encode(valor == null ? "" : valor, StandardCharsets.UTF_8));
        cookie.setPath("/");
        cookie.setMaxAge(DURACION);
        respuesta.addCookie(cookie);
    }

    private void borrarCookies(HttpServletResponse respuesta) {
        for (String nombre : TODAS) {
            Cookie cookie = new Cookie(nombre, "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            respuesta.addCookie(cookie);
        }
    }

    private String vista(Model modelo, Map<String, Object> datos, String error, String plantilla) {
        modelo.addAttribute("datos", datos);
        modelo.addAttribute("error", error);
        modelo.addAttribute("aficiones", LISTA_AFICIONES);
        modelo.addAttribute("generoTexto", generoTexto(texto(datos, "genero")));
        modelo.addAttribute("version", "Versión con cookies");
        modelo.addAttribute("base", "/v3");
        modelo.addAttribute("accion1", "/v3/paso1");
        modelo.addAttribute("accion2", "/v3/paso2");
        modelo.addAttribute("accion3", "/v3/paso3");
        return plantilla;
    }

    private Map<String, Object> datosVacios() {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("nombre", "");
        datos.put("apellido", "");
        datos.put("edad", "");
        datos.put("genero", "");
        datos.put("aficiones", new ArrayList<String>());
        return datos;
    }

    private String validarPaso1(Map<String, Object> datos) {
        if (texto(datos, "nombre").isBlank()) {
            return "El nombre no puede estar vacío.";
        }
        if (texto(datos, "apellido").isBlank()) {
            return "El apellido no puede estar vacío.";
        }
        if (texto(datos, "edad").isBlank()) {
            return "La edad no puede estar vacía.";
        }
        try {
            int edad = Integer.parseInt(texto(datos, "edad").trim());
            if (edad < 1 || edad > 120) {
                return "La edad debe estar entre 1 y 120 años.";
            }
        } catch (NumberFormatException e) {
            return "La edad debe ser un número.";
        }
        return null;
    }

    private String validarPaso2(Map<String, Object> datos) {
        if (texto(datos, "genero").isBlank()) {
            return "Tienes que elegir un género.";
        }
        return null;
    }

    private String validarPaso3(Map<String, Object> datos) {
        if (aficiones(datos).isEmpty()) {
            return "Elige al menos una afición.";
        }
        return null;
    }

    private boolean paso1Completo(Map<String, Object> datos) {
        return validarPaso1(datos) == null;
    }

    private boolean paso2Completo(Map<String, Object> datos) {
        return paso1Completo(datos) && validarPaso2(datos) == null;
    }

    private boolean todoCompleto(Map<String, Object> datos) {
        return paso2Completo(datos) && validarPaso3(datos) == null;
    }

    private String generoTexto(String genero) {
        return switch (genero) {
            case "hombre" -> "Hombre";
            case "mujer" -> "Mujer";
            case "otro" -> "Prefiero no decirlo";
            default -> genero;
        };
    }

    private String texto(Map<String, Object> datos, String clave) {
        Object valor = datos.get(clave);
        return valor == null ? "" : valor.toString();
    }

    @SuppressWarnings("unchecked")
    private List<String> aficiones(Map<String, Object> datos) {
        Object valor = datos.get("aficiones");
        return valor instanceof List<?> lista ? (List<String>) lista : List.of();
    }
}

package org.goya.daw2.dwes.d2ud3;



import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginasController {
    @GetMapping("/")
    public String paginainicio(HttpSession sesion , Authentication auth , Model model  ){
            if(auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
                   return "redirect:/login";
            }
            String usuario = auth.getName();
            sesion.setAttribute("usuario", usuario);
            model.addAttribute("titulo","Bienvenido " + usuario);
            model.addAttribute("paginaactual","Esta es la pagina inicio");
            model.addAttribute("texto","inicio");

            return "inicio";
    }

    @GetMapping("/pagina1")
    public String pagina1(Model model , Authentication auth ){
        model.addAttribute("titulopagina1","Bievenido"+auth.getName());
        model.addAttribute("paginaactual","Estas en la Pagina1");
        model.addAttribute("texto","pagina1");
        return "inicio";
    }
    @GetMapping("/pagina2")
    public String pagina2(Model model , Authentication auth ){
        model.addAttribute("titulopagina2","Bievenido"+auth.getName());
        model.addAttribute("paginaactua2","Estas en la Pagina2");
        model.addAttribute("texto","pagina2");
        return "inicio";
    }

    @GetMapping("/login")
    public String paginalogin(Authentication auth){
        if(auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
            return "redirect:/";
        }
        return "login";
    }
}

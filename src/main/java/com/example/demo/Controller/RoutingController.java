package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RoutingController {

    // 1. Renderizado directo (sin redirección)
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("mensaje", "¡Bienvenido a la página de inicio!");
        return "home"; // Renderiza home.jsp
    }

    // 2. Redirección (redirect) con parámetros
    @GetMapping("/redirigir")
    public String redirigir(@RequestParam String nombre, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("nombre", nombre);
        System.out.println(nombre);
        return "redirect:/destino"; // Cambia la URL a /destino
    }

    // 3. Forward (mantiene la URL)
    @GetMapping("/reusar")
    public String reusar(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Error simulado");
        return "forward:/mostrar-error"; // Mantiene la URL pero muestra error.jsp
    }   

    // Página de destino (para redirect)
    @GetMapping("/destino")
    public String destino(@RequestParam(required = false) String nombre, Model model) {
        if (nombre != null) {
            model.addAttribute("saludo", "Hola, " + nombre + " (redirigido)");
        }
        return "destino";
    }

    // Página de error (para forward) 
    @GetMapping("/mostrar-error")
    public String error(Model model) {
        model.addAttribute("mensajeError", "Error simulado");
        return "error";
    }
}
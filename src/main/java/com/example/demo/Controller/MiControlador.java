package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiControlador {

    @GetMapping("/hola")
    public String holaMundo(Model model) {
        System.out.println("test");
        model.addAttribute("mensaje", "¡Hola desde Spring Boot!");
        return "vista";
    }

    
}

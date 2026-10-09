package com.example.demo.Artesanos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/artesano")
public class ArtesanoController {

    @GetMapping({"/list"})
    public String listarArtesanos() {
        return "redirect:/admin/artesanos/artesanos.html";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear() {
        return "redirect:/admin/artesanos/registrar.html";
    }

    @GetMapping("/detalle")
    public String mostrarDetalle(@RequestParam("id") int id) {
        return "redirect:/admin/artesanos/detalle-artesano.html?id=" + id;
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam("id") int id) {
        return "redirect:/admin/artesanos/editar.html?id=" + id;
    }
}

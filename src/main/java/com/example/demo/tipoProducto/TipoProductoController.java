package com.example.demo.tipoProducto;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;


@Controller
@RequestMapping("/tipoproducto")
public class TipoProductoController {

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("tipoproductos", tipoProductoService.listaTipoProducto());
        return "tipoproducto/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("tipoProducto", new TipoProducto());
        return "tipoproducto/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("tipoProducto") TipoProducto tipo, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("error", "Revisa los datos de la categoría.");
        } else {
            try {
                tipoProductoService.crearTipoProducto(tipo);
                return "redirect:/tipoproducto/listar";
            } catch (IllegalArgumentException error) {
                model.addAttribute("error", error.getMessage());
            }
        }
        return "tipoproducto/nuevo";
    }

    @GetMapping("/detalle")
    public String detalle(@RequestParam int id, Model model) {
        TipoProducto tipo = tipoProductoService.obtenerTipoProductoPorId(id);
        if (tipo == null) {
            throw new IllegalArgumentException("La categoría no existe.");
        }
        model.addAttribute("tipoProducto", tipo);
        return "tipoproducto/detalle";
    }

    @PostMapping("/desactivar")
    public String desactivar(@RequestParam int id) {
        tipoProductoService.desactivar(id);
        return "redirect:/tipoproducto/listar";
    }

    private final TipoProductoService tipoProductoService;

    public TipoProductoController(TipoProductoService tipoProductoService) {
        this.tipoProductoService = tipoProductoService;
    }

    @RequestMapping("/list")
    public String listaTipoProducto(Model model) {
        model.addAttribute("tipoproductos", tipoProductoService.listaTipoProducto());      
        return "tipoproducto/lista"; // Retorna la vista correspondiente
    }


    @GetMapping("/api/list")
    public List<TipoProducto> obtenerProductos() {
        return tipoProductoService.listaTipoProducto();
    }

    @PostMapping("/get")
    public String obtenerTipoProducto(@RequestParam("id") int id,  Model model) {
        TipoProducto tipoProducto = tipoProductoService.obtenerTipoProductoPorId(id);
        model.addAttribute("tipoProducto", tipoProducto );
        System.out.println("tipoProducto" + tipoProducto.getNombre() );      
        return "tipoproducto/obtener"; // Retorna la vista correspondiente

    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        // Se agrega un objeto vacío al modelo para que el formulario pueda vincularse
        model.addAttribute("tipoProducto", new TipoProducto());
        return "tipoproducto/crear";
    }

    @PostMapping("/crear")
    public String crearTipoProducto(@ModelAttribute("tipoProducto") TipoProducto tipoProducto) {
        tipoProductoService.crearTipoProducto(tipoProducto);
        return "redirect:/tipoproducto/list";
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("tipoProducto", new TipoProducto());
        return "/tipoproducto/registro";
    }

    @PostMapping("/registrar")
    public String registrarUsuario(@ModelAttribute("tipoProducto") TipoProducto tipoProducto, Model model) {
        tipoProductoService.crearTipoProducto(tipoProducto);
        return "redirect:/tipoproducto/list";
    }

}

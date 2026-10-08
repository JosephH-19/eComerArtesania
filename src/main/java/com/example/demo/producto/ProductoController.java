package com.example.demo.producto;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.tipoProducto.TipoProducto;
import com.example.demo.tipoProducto.TipoProductoService;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final TipoProductoService tipoProductoService;
    private final ProductoService productoService;

    public ProductoController(
            TipoProductoService tipoProductoService,
            ProductoService productoService) {

        this.tipoProductoService = tipoProductoService;
        this.productoService = productoService;
    }

    @GetMapping("/list")
    public String listaTipoProducto(Model model) {
        model.addAttribute(
                "tipoproductos",
                tipoProductoService.listaTipoProducto()
        );

        return "producto/reporte/producto_list";
    }

    @PostMapping("/reporte/productoTipoProducto")
    public String ReporteProducto(
            @RequestParam("id") int id,
            Model model) {

        model.addAttribute(
                "productos",
                productoService.ProductoReporte(id)
        );

        return "producto/reporte/producto";
    }

    @GetMapping("/listar")
    public String listar(
            @RequestParam(name = "nombre", defaultValue = "") String nombre,
            Model model) {

        model.addAttribute("productos",
                productoService.buscarPorNombre(nombre));

        model.addAttribute("nombre", nombre);

        return "producto/listar";
    }

    @GetMapping("/crear")
    public String crear(Model model) {

        Producto producto = new Producto();
        producto.setTipoProducto(new TipoProducto());

        model.addAttribute("producto", producto);
        model.addAttribute(
                "tipoproductos",
                tipoProductoService.listaTipoProducto()
        );

        return "producto/crear";
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute("producto") Producto producto,
            BindingResult resultado,
            Model model) {

        if (resultado.hasErrors()) {
            model.addAttribute(
                    "error",
                    "Revisa el precio, stock y tipo de producto."
            );
        } else {
            try {
                productoService.guardar(producto);
                return "redirect:/producto/listar";
            } catch (IllegalArgumentException error) {
                model.addAttribute("error", error.getMessage());
            }
        }

        model.addAttribute(
                "tipoproductos",
                tipoProductoService.listaTipoProducto()
        );

        return "producto/crear";
    }

    @GetMapping("/detalle")
    public String detalle(@RequestParam("id") int id, Model model) {
        Producto producto = productoService.buscarPorId(id);

        if (producto == null) {
            model.addAttribute("error", "El producto no existe.");
        } else {
            model.addAttribute("producto", producto);
        }

        return "producto/detalle";
    }
}
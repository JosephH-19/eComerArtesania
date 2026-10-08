package com.example.demo.producto;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.tipoProducto.TipoProductoService;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final TipoProductoService tipoProductoService;
    private final ProductoService productoService;


    public ProductoController(TipoProductoService tipoProductoService, ProductoService productoService) {
        this.tipoProductoService = tipoProductoService;
        this.productoService = productoService;
    }

    @RequestMapping("/list")
    public String listaTipoProducto(Model model) {
        model.addAttribute("tipoproductos", tipoProductoService.listaTipoProducto());      
        return "producto/reporte/producto_list"; // Retorna la vista correspondiente
    }

    @PostMapping("/reporte/productoTipoProducto")
    public String ReporteProducto( @RequestParam("id") int id , Model model) {
        model.addAttribute("productos", productoService.ProductoReporte(id));   
        return "producto/reporte/producto"; // Retorna la vista correspondiente
    }


}

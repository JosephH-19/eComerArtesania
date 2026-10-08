package com.example.demo.tipoProducto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/tipoproducto")
public class TipoProductoRestController {

    private final TipoProductoService tipoProductoService;

    public TipoProductoRestController(TipoProductoService tipoProductoService) {
        this.tipoProductoService = tipoProductoService;
    }

    @GetMapping("/list")
    public List<TipoProducto> obtenerProductos() {
        return tipoProductoService.listaTipoProducto();
    }

}
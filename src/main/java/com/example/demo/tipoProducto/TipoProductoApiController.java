package com.example.demo.tipoProducto;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipoproductos")
public class TipoProductoApiController {

    private final TipoProductoService tipoProductoService;

    public TipoProductoApiController(TipoProductoService tipoProductoService) {
        this.tipoProductoService = tipoProductoService;
    }

    @GetMapping("/list")
    public List<TipoProducto> obtenerProductos() {
        return tipoProductoService.listaTipoProducto();
    }

}
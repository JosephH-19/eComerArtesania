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
import com.example.demo.artesano.ArtesanoService;
import com.example.demo.artesano.Artesano;
import java.util.List;
import com.example.demo.tipoProducto.TipoProductoService;

import java.math.BigDecimal;
import java.util.ArrayList;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final TipoProductoService tipoProductoService;
    private final ProductoService productoService;
    private final ArtesanoService artesanoService;

    public ProductoController(
            TipoProductoService tipoProductoService,
            ProductoService productoService, ArtesanoService artesanoService) {

        this.tipoProductoService = tipoProductoService;
        this.productoService = productoService;
        this.artesanoService = artesanoService;
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
            @RequestParam(name = "disponibilidad", defaultValue = "TODOS")
            String disponibilidad,
            Model model) {

        if (!"CON_STOCK".equals(disponibilidad) &&
                !"SIN_STOCK".equals(disponibilidad)) {
            disponibilidad = "TODOS";
        }

        model.addAttribute("productos",
                productoService.filtrar(nombre, disponibilidad));

        model.addAttribute("nombre", nombre);
        model.addAttribute("disponibilidad", disponibilidad);

        return "producto/listar";
    }

    @GetMapping("/crear")
    public String crear(Model model) {

        Producto producto = new Producto();
        producto.setTipoProducto(new TipoProducto());
        producto.setArtesano(new Artesano());

        model.addAttribute("producto", producto);
        model.addAttribute(
                "tipoproductos",
                tipoProductoService.listarActivos()
        );

        cargarRelaciones(producto, model);
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
                tipoProductoService.listarActivos()
        );

        cargarRelaciones(producto, model);
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

    @GetMapping("/editar")
    public String editar(@RequestParam("id") int id, Model model) {
        Producto producto = productoService.buscarPorId(id);

        if (producto == null) {
            model.addAttribute("error", "El producto no existe.");
            return "producto/detalle";
        }

        model.addAttribute("producto", producto);
        model.addAttribute("tipoproductos",
                tipoProductoService.listarActivos());

        cargarRelaciones(producto, model);
        return "producto/editar";
    }

    @PostMapping("/actualizar")
    public String actualizar(
            @ModelAttribute("producto") Producto producto,
            BindingResult resultado,
            Model model) {

        if (resultado.hasErrors()) {
            model.addAttribute("error",
                    "Revisa el precio, stock y tipo de producto.");
        } else {
            try {
                productoService.actualizar(producto);
                return "redirect:/producto/listar";
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        model.addAttribute("tipoproductos",
                tipoProductoService.listarActivos());

        cargarRelaciones(producto, model);
        return "producto/editar";
    }

    @GetMapping("/catalogo")
    public String catalogo(
            @RequestParam(name = "nombre", defaultValue = "") String nombre,
            @RequestParam(name = "disponibilidad", defaultValue = "TODOS")
            String disponibilidad,
            @RequestParam(name = "precioMinimo", required = false)
            BigDecimal precioMinimo,
            @RequestParam(name = "precioMaximo", required = false)
            BigDecimal precioMaximo,
            @RequestParam(name = "categoria", required = false) Integer categoria,
            Model model) {

        if (!"CON_STOCK".equals(disponibilidad) &&
                !"SIN_STOCK".equals(disponibilidad)) {
            disponibilidad = "TODOS";
        }

        model.addAttribute("nombre", nombre);
        model.addAttribute("disponibilidad", disponibilidad);
        model.addAttribute("precioMinimo", precioMinimo);
        model.addAttribute("precioMaximo", precioMaximo);
        model.addAttribute("categoria", categoria);
        model.addAttribute("categorias", tipoProductoService.listaTipoProducto());

        try {
            model.addAttribute("productos",
                    productoService.catalogo(
                            nombre, disponibilidad, precioMinimo, precioMaximo, categoria));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("productos", new ArrayList<Producto>());
        }

        return "producto/catalogo";
    }

    @GetMapping("/detallePublico")
    public String detallePublico(@RequestParam("id") int id, Model model) {
        Producto producto = productoService.buscarPorId(id);

        if (producto == null ||
                !"ACTIVO".equals(producto.getEstado())) {
            model.addAttribute("error", "El producto no está disponible.");
        } else {
            model.addAttribute("producto", producto);
        }

        return "producto/detalle_publico";
    }

    private void cargarRelaciones(Producto producto, Model model) {
        List<TipoProducto> categorias = new ArrayList<>(tipoProductoService.listarActivos());
        List<Artesano> artesanos = new ArrayList<>(artesanoService.listarActivos());
        Producto existente = productoService.buscarPorId(producto.getId());
        if (existente != null) {
            TipoProducto actual = tipoProductoService.obtenerTipoProductoPorId(existente.getTipoProducto().getId());
            if (actual != null && "INACTIVO".equals(actual.getEstado())) {
                categorias.add(actual);
            }
            Artesano artesano = artesanoService.buscarPorId(existente.getArtesano().getId());
            if (artesano != null && "INACTIVO".equals(artesano.getEstado())) {
                artesanos.add(artesano);
            }
        }
        model.addAttribute("tipoproductos", categorias);
        model.addAttribute("artesanos", artesanos);
    }

    @PostMapping("/cambiarEstado")
    public String cambiarEstado(
            @RequestParam("id") int id,
            @RequestParam("estado") String estado,
            Model model) {

        try {
            productoService.cambiarEstado(id, estado);
            return "redirect:/producto/listar";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "producto/detalle";
        }
    }
}

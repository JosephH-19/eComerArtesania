package com.tallerbuendia.api.catalogo;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tallerbuendia.api.dto.ProductoRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService service;
    public ProductoController(ProductoService service) { this.service = service; }

    @GetMapping public List<Producto> listar(@RequestParam(required = false) Long categoriaId,
                                             @RequestParam(required = false) String q) {
        return service.listar(categoriaId, q);
    }
    @GetMapping("/{id}") public Producto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@Valid @RequestBody ProductoRequest request) { return guardar(null, request); }
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) { return guardar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }

    private Producto guardar(Long id, ProductoRequest request) {
        return service.guardar(id, request.nombre(), request.descripcion(), request.material(), request.precio(),
                request.stock(), request.estado(), request.categoriaId(), request.artesanoId(), request.imagen());
    }
}

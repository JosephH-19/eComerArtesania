package com.tallerbuendia.api.descuentos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tallerbuendia.api.dto.DescuentoRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/descuentos")
public class DescuentoController {
    private final DescuentoService service;
    public DescuentoController(DescuentoService service) { this.service = service; }

    @GetMapping public List<Descuento> listar() { return service.listar(); }
    @GetMapping("/{id}") public Descuento obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Descuento crear(@Valid @RequestBody DescuentoRequest request) { return guardar(null, request); }
    @PutMapping("/{id}")
    public Descuento actualizar(@PathVariable Long id, @Valid @RequestBody DescuentoRequest request) { return guardar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }

    private Descuento guardar(Long id, DescuentoRequest request) {
        return service.guardar(id, request.nombre(), request.descripcion(), request.tipo(), request.valor(),
                request.fechaInicio(), request.fechaFin(), request.aplicableA(), request.objetivoId(),
                request.activo() == null || request.activo());
    }
}

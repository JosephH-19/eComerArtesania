package com.tallerbuendia.api.artesanos;

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

import com.tallerbuendia.api.dto.ArtesanoRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/artesanos")
public class ArtesanoController {
    private final ArtesanoService service;
    public ArtesanoController(ArtesanoService service) { this.service = service; }

    @GetMapping public List<Artesano> listar() { return service.listar(); }
    @GetMapping("/{id}") public Artesano obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Artesano crear(@Valid @RequestBody ArtesanoRequest request) {
        return service.crear(request.nombre(), request.especialidad(), request.aniosExperiencia(), request.correo(), request.telefono(), request.activo() == null || request.activo());
    }
    @PutMapping("/{id}")
    public Artesano actualizar(@PathVariable Long id, @Valid @RequestBody ArtesanoRequest request) {
        return service.actualizar(id, request.nombre(), request.especialidad(), request.aniosExperiencia(), request.correo(), request.telefono(), request.activo() == null || request.activo());
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}

package com.example.demo.Artesanos;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/artesanos")
public class ArtesanoRestController {

    private final ArtesanoService artesanoService;

    public ArtesanoRestController(ArtesanoService artesanoService) {
        this.artesanoService = artesanoService;
    }

    @GetMapping("/list")
    public List<Artesano> listarArtesanos() {
        return artesanoService.listarArtesanos();
    }

    @GetMapping("/{id}")
    public Artesano obtenerArtesano(@PathVariable int id) {
        Artesano artesano = artesanoService.obtenerArtesanoPorId(id);

        if (artesano == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontró el artesano con ID " + id
            );
        }

        return artesano;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crearArtesano(
            @RequestBody Artesano artesano) {

        artesanoService.crearArtesano(artesano);

        return Map.of("mensaje", "Artesano registrado correctamente.");
    }

    @PostMapping("/{id}/actualizar")
    public Map<String, String> actualizarArtesano(
            @PathVariable int id,
            @RequestBody Artesano artesano) {

        obtenerArtesano(id);
        artesano.setId(id);
        artesanoService.actualizarArtesano(artesano);

        return Map.of("mensaje", "Artesano actualizado correctamente.");
    }

    @PostMapping("/{id}/eliminar")
    public Map<String, String> eliminarArtesano(@PathVariable int id) {
        obtenerArtesano(id);
        artesanoService.eliminarArtesano(id);

        return Map.of("mensaje", "Artesano eliminado correctamente.");
    }
}

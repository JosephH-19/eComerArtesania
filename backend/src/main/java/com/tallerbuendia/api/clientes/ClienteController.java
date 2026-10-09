package com.tallerbuendia.api.clientes;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tallerbuendia.api.dto.ClienteRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service = service; }

    @GetMapping public List<Cliente> listar(@RequestParam(required = false) String q,
                                            @RequestParam(required = false) String estado) {
        return service.listar(q, estado);
    }
    @GetMapping("/{id}") public Cliente obtener(@PathVariable Long id) { return service.obtener(id); }
    @GetMapping("/dni/{dni}") public Cliente obtenerPorDni(@PathVariable String dni) { return service.obtenerPorDni(dni); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(@Valid @RequestBody ClienteRequest request) {
        return service.registrar(request.nombre(), request.dni(), request.correo(), request.telefono(), request.direccion(),
                request.distrito(), request.fechaNacimiento(), request.genero(), request.estado());
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}

package com.tallerbuendia.api.pedidos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tallerbuendia.api.dto.CambioEstadoRequest;
import com.tallerbuendia.api.dto.PedidoRequest;
import com.tallerbuendia.api.dto.PedidoResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;
    public PedidoController(PedidoService service) { this.service = service; }

    @GetMapping public List<PedidoResponse> listar(@RequestParam(required = false) String estado) {
        List<PedidoResponse> all = service.listar();
        return estado == null || estado.isBlank() ? all : all.stream().filter(order -> order.estado().equalsIgnoreCase(estado.replace(' ', '_'))).toList();
    }
    @GetMapping("/{id}") public PedidoResponse obtener(@PathVariable Long id) { return service.obtener(id); }
    @GetMapping("/dni/{dni}") public List<PedidoResponse> consultarPorDni(@PathVariable String dni) { return service.listarPorDni(dni); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse crear(@Valid @RequestBody PedidoRequest request) { return service.crear(request); }
    @PatchMapping("/{id}/estado")
    public PedidoResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambioEstadoRequest request) {
        return service.cambiarEstado(id, request.estado(), request.observacion());
    }
}

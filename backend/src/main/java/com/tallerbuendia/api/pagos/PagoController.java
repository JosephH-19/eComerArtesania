package com.tallerbuendia.api.pagos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tallerbuendia.api.dto.PagoRequest;
import com.tallerbuendia.api.dto.PagoResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    private final PagoService service;
    public PagoController(PagoService service) { this.service = service; }

    @GetMapping public List<PagoResponse> listar() { return service.listar(); }
    @GetMapping("/{id}") public PagoResponse obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public PagoResponse registrar(@Valid @RequestBody PagoRequest request) { return service.registrar(request); }
    @PatchMapping("/{id}/confirmar") public PagoResponse confirmar(@PathVariable Long id) { return service.confirmar(id); }
}

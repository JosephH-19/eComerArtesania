package com.tallerbuendia.api.metricas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tallerbuendia.api.dto.MetricasResponse;

@RestController
@RequestMapping("/api/metricas")
public class MetricasController {
    private final MetricasService service;
    public MetricasController(MetricasService service) { this.service = service; }
    @GetMapping public MetricasResponse resumen() { return service.resumen(); }
}

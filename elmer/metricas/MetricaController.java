package com.example.demo.elmer.metricas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Integrar bajo la autenticación y configuración MVC del proyecto común.
// Las rutas administrativas deben quedar protegidas por el acceso del equipo.
@Controller
public class MetricaController {
  private final MetricaService servicio;

  public MetricaController(MetricaService servicio) {
    this.servicio = servicio;
  }

  @GetMapping("/elmer/admin/metricas")
  public String mostrar(@RequestParam(required = false) String mes, Model model) {
    model.addAllAttributes(servicio.calcular(mes));
    return "elmer/metricas";
  }
}

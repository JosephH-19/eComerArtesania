package com.example.demo.elmer.pedidos;

import jakarta.servlet.http.HttpSession;
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Integrar bajo la autenticación y configuración MVC del proyecto común.
// Las rutas administrativas deben quedar protegidas por el acceso del equipo.
@Controller
@RequestMapping("/elmer")
public class PedidoController {
  private final PedidoService servicio;

  public PedidoController(PedidoService servicio) {
    this.servicio = servicio;
  }

  @ModelAttribute
  public void estados(Model m) {
    m.addAttribute(
        "estados",
        List.of(
            "PENDIENTE",
            "CONFIRMADO",
            "PREPARACION",
            "LISTO",
            "EN_CAMINO",
            "ENTREGADO",
            "CANCELADO"));
  }

  @GetMapping({"", "/", "/consulta"})
  public String consulta() {
    return "elmer/consulta";
  }

  @PostMapping("/consulta")
  public String buscar(@RequestParam String dni, @RequestParam String codigo, HttpSession session) {
    Map<String, Object> pedido = servicio.consultar(dni, codigo);
    session.setAttribute("elmerPedido", ((Number) pedido.get("id")).longValue());
    return "redirect:/elmer/pedido/" + pedido.get("id");
  }

  @GetMapping("/pedido/{id}")
  public String publico(@PathVariable long id, HttpSession session, Model m) {
    verificarConsulta(id, session);
    m.addAllAttributes(servicio.detalle(id));
    return "elmer/pedido";
  }

  public static void verificarConsulta(long id, HttpSession session) {
    if (!Objects.equals(id, session.getAttribute("elmerPedido")))
      throw new IllegalArgumentException("Consulta primero el pedido con tu DNI y código.");
  }

  @GetMapping("/admin/pedidos")
  public String listar(
      @RequestParam(defaultValue = "") String q,
      @RequestParam(defaultValue = "") String estado,
      @RequestParam(defaultValue = "") String pago,
      Model m) {
    m.addAttribute("pedidos", servicio.listar(q, estado, pago));
    return "elmer/pedidos";
  }

  @GetMapping("/admin/pedido/{id}")
  public String detalle(@PathVariable long id, Model m) {
    m.addAllAttributes(servicio.detalle(id));
    m.addAttribute("administracion", true);
    return "elmer/pedido";
  }

  @PostMapping("/admin/pedido/{id}/estado")
  public String actualizar(
      @PathVariable long id, @RequestParam String estado, @RequestParam String nota) {
    servicio.cambiarEstado(id, estado, nota);
    return "redirect:/elmer/admin/pedido/" + id;
  }
}

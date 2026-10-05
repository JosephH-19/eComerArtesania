package com.example.demo.elmer.pagos;

import com.example.demo.elmer.pedidos.PedidoController;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/elmer")
public class PagoController {
  private final PagoService servicio;

  public PagoController(PagoService servicio) {
    this.servicio = servicio;
  }

  @GetMapping("/admin/pagos")
  public String lista(Model m) {
    m.addAttribute("pagos", servicio.listar());
    return "elmer/pagos";
  }

  @PostMapping("/admin/pedido/{id}/pago")
  public String registrar(@PathVariable long id, @RequestParam Map<String, String> datos) {
    servicio.registrarPago(id, datos, true);
    return "redirect:/elmer/admin/pedido/" + id;
  }

  @PostMapping("/admin/pago/{id}/resolver")
  public String resolver(@PathVariable long id, @RequestParam boolean aprobar) {
    servicio.resolverPago(id, aprobar);
    return "redirect:/elmer/admin/pagos";
  }

  @PostMapping("/pedido/{id}/pago")
  public String informar(
      @PathVariable long id,
      @RequestParam Map<String, String> datos,
      HttpSession session,
      RedirectAttributes flash) {
    PedidoController.verificarConsulta(id, session);
    servicio.registrarPago(id, datos, false);
    flash.addFlashAttribute("aviso", "Pago informado, pendiente de revisión administrativa.");
    return "redirect:/elmer/pedido/" + id;
  }
}

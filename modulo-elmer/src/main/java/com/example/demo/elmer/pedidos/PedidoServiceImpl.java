package com.example.demo.elmer.pedidos;

import com.example.demo.elmer.comun.SqlDAO;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {
  private final SqlDAO db;

  public PedidoServiceImpl(SqlDAO db) {
    this.db = db;
  }

  public List<Map<String, Object>> listar(String q, String estado, String pago) {
    List<Map<String, Object>> pedidos =
        db.listar(
            "SELECT p.*, COALESCE((SELECT SUM(monto) FROM pago_t WHERE pedido_id=p.id AND"
                + " estado='CONFIRMADO'),0) AS cobrado FROM pedido_t p WHERE (LOWER(nombre) LIKE ?"
                + " OR dni LIKE ? OR codigo LIKE ?) AND (?='' OR estado=?) ORDER BY id DESC",
            "%" + q.toLowerCase() + "%",
            "%" + q + "%",
            "%" + q.toUpperCase() + "%",
            estado,
            estado);
    List<Map<String, Object>> resultado = new ArrayList<>();
    for (Map<String, Object> pedido : pedidos) {
      BigDecimal cobrado = (BigDecimal) pedido.get("cobrado");
      BigDecimal total = (BigDecimal) pedido.get("total");
      String estadoPago = "PENDIENTE";
      if (cobrado.compareTo(total) >= 0) estadoPago = "PAGADO";
      else if (cobrado.signum() > 0) estadoPago = "PARCIAL";
      pedido.put("pago", estadoPago);
      if (pago.isEmpty() || pago.equals(estadoPago)) resultado.add(pedido);
    }
    return resultado;
  }

  public Map<String, Object> obtener(long id) {
    return db.obtener("SELECT * FROM pedido_t WHERE id=?", id);
  }

  public Map<String, Object> consultar(String dni, String codigo) {
    if (!dni.matches("[0-9]{8}"))
      throw new IllegalArgumentException("El DNI debe tener 8 dígitos.");
    List<Map<String, Object>> pedidos =
        db.listar(
            "SELECT * FROM pedido_t WHERE dni=? AND codigo=?", dni, codigo.trim().toUpperCase());
    if (pedidos.isEmpty())
      throw new IllegalArgumentException("No se encontró un pedido con ese DNI y código.");
    return pedidos.get(0);
  }

  public Map<String, Object> detalle(long id) {
    Map<String, Object> vista = new HashMap<>();
    vista.put("pedido", obtener(id));
    vista.put("detalles", db.listar("SELECT * FROM detalle_t WHERE pedido_id=?", id));
    vista.put("pagos", db.listar("SELECT * FROM pago_t WHERE pedido_id=? ORDER BY id", id));
    vista.put(
        "seguimiento", db.listar("SELECT * FROM seguimiento_t WHERE pedido_id=? ORDER BY id", id));
    vista.put(
        "cobrado",
        db.obtener(
                "SELECT COALESCE(SUM(monto),0) AS total FROM pago_t WHERE pedido_id=? AND"
                    + " estado='CONFIRMADO'",
                id)
            .get("total"));
    return vista;
  }

  @Transactional
  public void cambiarEstado(long id, String siguiente, String nota) {
    // Bloqueamos este pedido mientras se valida y guarda el cambio.
    Map<String, Object> p = db.obtener("SELECT * FROM pedido_t WHERE id=? FOR UPDATE", id);
    String actual = p.get("estado").toString();
    boolean permitido = false;
    if (actual.equals("PENDIENTE")) {
      permitido = siguiente.equals("CONFIRMADO") || siguiente.equals("CANCELADO");
    } else if (actual.equals("CONFIRMADO")) {
      permitido = siguiente.equals("PREPARACION") || siguiente.equals("CANCELADO");
    } else if (actual.equals("PREPARACION")) {
      permitido = siguiente.equals("LISTO") || siguiente.equals("CANCELADO");
    } else if (actual.equals("LISTO")) {
      permitido =
          siguiente.equals("EN_CAMINO")
              || siguiente.equals("ENTREGADO")
              || siguiente.equals("CANCELADO");
    } else if (actual.equals("EN_CAMINO")) {
      permitido = siguiente.equals("ENTREGADO");
    }
    if (!permitido)
      throw new IllegalArgumentException(
          "No se permite pasar de " + actual + " a " + siguiente + ".");
    BigDecimal cobrado =
        (BigDecimal)
            db.obtener(
                    "SELECT COALESCE(SUM(monto),0) AS total FROM pago_t WHERE pedido_id=? AND"
                        + " estado='CONFIRMADO'",
                    id)
                .get("total");
    if (siguiente.equals("ENTREGADO") && cobrado.compareTo((BigDecimal) p.get("total")) < 0)
      throw new IllegalArgumentException("Completa el pago antes de entregar el pedido.");
    if (siguiente.equals("CANCELADO")) {
      if (cobrado.signum() > 0
          || !db.listar("SELECT id FROM pago_t WHERE pedido_id=? AND estado='PENDIENTE'", id)
              .isEmpty())
        throw new IllegalArgumentException(
            "No se cancela un pedido con pagos confirmados o por revisar. Las devoluciones"
                + " requieren un proceso aparte.");
      // El catálogo y la reserva de stock pertenecen al módulo de compra.
    }
    if (nota == null || nota.isBlank() || nota.length() > 500)
      throw new IllegalArgumentException(
          "Escribe una nota de seguimiento de hasta 500 caracteres.");
    db.ejecutar("UPDATE pedido_t SET estado=? WHERE id=?", siguiente, id);
    db.insertar(
        "INSERT INTO seguimiento_t(pedido_id,estado,nota) VALUES(?,?,?)", id, siguiente, nota);
  }
}

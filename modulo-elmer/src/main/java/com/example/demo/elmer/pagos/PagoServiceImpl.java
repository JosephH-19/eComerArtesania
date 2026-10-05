package com.example.demo.elmer.pagos;

import static com.example.demo.elmer.comun.Validaciones.*;

import com.example.demo.elmer.comun.SqlDAO;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagoServiceImpl implements PagoService {
  private final SqlDAO db;

  public PagoServiceImpl(SqlDAO db) {
    this.db = db;
  }

  public List<Map<String, Object>> listar() {
    return db.listar(
        "SELECT g.*,p.codigo FROM pago_t g JOIN pedido_t p ON p.id=g.pedido_id ORDER BY g.id DESC");
  }

  @Transactional
  public void registrarPago(long id, Map<String, String> d, boolean admin) {
    // El pago y el pedido se validan dentro de la misma transacción.
    Map<String, Object> p = db.obtener("SELECT * FROM pedido_t WHERE id=? FOR UPDATE", id);
    if (p.get("estado").equals("CANCELADO"))
      throw new IllegalArgumentException("Pedido cancelado.");
    BigDecimal monto = dinero(texto(d, "monto", 20));
    String metodo = opcion(d.get("metodo"), "YAPE", "PLIN", "TRANSFERENCIA", "EFECTIVO");
    if (!admin && metodo.equals("EFECTIVO"))
      throw new IllegalArgumentException("El efectivo lo registra el administrador al recibirlo.");
    BigDecimal reservado =
        (BigDecimal)
            db.obtener(
                    "SELECT COALESCE(SUM(monto),0) AS total FROM pago_t WHERE pedido_id=? AND"
                        + " estado IN ('PENDIENTE','CONFIRMADO')",
                    id)
                .get("total");
    if (monto.signum() <= 0 || monto.add(reservado).compareTo((BigDecimal) p.get("total")) > 0)
      throw new IllegalArgumentException("El pago supera el saldo disponible o no es positivo.");
    String ref = texto(d, "referencia", 100);
    if (!db.listar("SELECT id FROM pago_t WHERE referencia=?", ref).isEmpty())
      throw new IllegalArgumentException("La referencia de pago ya está registrada.");
    String estadoPago = "PENDIENTE";
    if (admin) {
      estadoPago = "CONFIRMADO";
    }
    db.insertar(
        "INSERT INTO pago_t(pedido_id,monto,metodo,referencia,estado) VALUES(?,?,?,?,?)",
        id,
        monto,
        metodo,
        ref,
        estadoPago);
  }

  @Transactional
  public void resolverPago(long id, boolean aprobar) {
    Map<String, Object> pago = db.obtener("SELECT * FROM pago_t WHERE id=?", id);
    db.obtener("SELECT * FROM pedido_t WHERE id=? FOR UPDATE", pago.get("pedido_id"));
    String nuevoEstado = "RECHAZADO";
    if (aprobar) {
      nuevoEstado = "CONFIRMADO";
    }
    if (db.ejecutar("UPDATE pago_t SET estado=? WHERE id=? AND estado='PENDIENTE'", nuevoEstado, id)
        != 1) throw new IllegalArgumentException("Este pago ya fue revisado.");
  }
}

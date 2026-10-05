package com.example.demo.elmer.pagos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagoServiceImpl implements PagoService {
  private final JdbcTemplate jdbcTemplate;

  public PagoServiceImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Map<String, Object>> listar() {
    return listar(
        "SELECT g.*,p.codigo FROM pago_t g JOIN pedido_t p ON p.id=g.pedido_id ORDER BY g.id DESC");
  }

  @Transactional
  public void registrarPago(long id, Map<String, String> d, boolean admin) {
    // El pago y el pedido se validan dentro de la misma transacción.
    Map<String, Object> p = obtener("SELECT * FROM pedido_t WHERE id=? FOR UPDATE", id);
    if (p.get("estado").equals("CANCELADO"))
      throw new IllegalArgumentException("Pedido cancelado.");
    BigDecimal monto = dinero(texto(d, "monto", 20));
    String metodo = opcion(d.get("metodo"), "YAPE", "PLIN", "TRANSFERENCIA", "EFECTIVO");
    if (!admin && metodo.equals("EFECTIVO"))
      throw new IllegalArgumentException("El efectivo lo registra el administrador al recibirlo.");
    BigDecimal reservado =
        (BigDecimal)
            obtener(
                    "SELECT COALESCE(SUM(monto),0) AS total FROM pago_t WHERE pedido_id=? AND"
                        + " estado IN ('PENDIENTE','CONFIRMADO')",
                    id)
                .get("total");
    if (monto.signum() <= 0 || monto.add(reservado).compareTo((BigDecimal) p.get("total")) > 0)
      throw new IllegalArgumentException("El pago supera el saldo disponible o no es positivo.");
    String ref = texto(d, "referencia", 100);
    if (!listar("SELECT id FROM pago_t WHERE referencia=?", ref).isEmpty())
      throw new IllegalArgumentException("La referencia de pago ya está registrada.");
    String estadoPago = "PENDIENTE";
    if (admin) {
      estadoPago = "CONFIRMADO";
    }
    jdbcTemplate.update(
        "INSERT INTO pago_t(pedido_id,monto,metodo,referencia,estado) VALUES(?,?,?,?,?)",
        id,
        monto,
        metodo,
        ref,
        estadoPago);
  }

  @Transactional
  public void resolverPago(long id, boolean aprobar) {
    Map<String, Object> pago = obtener("SELECT * FROM pago_t WHERE id=?", id);
    obtener("SELECT * FROM pedido_t WHERE id=? FOR UPDATE", pago.get("pedido_id"));
    String nuevoEstado = "RECHAZADO";
    if (aprobar) {
      nuevoEstado = "CONFIRMADO";
    }
    if (jdbcTemplate.update(
            "UPDATE pago_t SET estado=? WHERE id=? AND estado='PENDIENTE'", nuevoEstado, id)
        != 1) throw new IllegalArgumentException("Este pago ya fue revisado.");
  }

  // JdbcTemplate ejecuta el SQL y devuelve las filas de la consulta.
  private List<Map<String, Object>> listar(String sql, Object... parametros) {
    return jdbcTemplate.queryForList(sql, parametros);
  }

  // Obtiene una sola fila, por ejemplo el pedido que se va a actualizar.
  private Map<String, Object> obtener(String sql, Object... parametros) {
    List<Map<String, Object>> filas = listar(sql, parametros);
    if (filas.isEmpty()) {
      throw new IllegalArgumentException("No se encontró el registro solicitado.");
    }
    return filas.get(0);
  }

  private String texto(Map<String, String> d, String campo, int max) {
    String v = d.getOrDefault(campo, "").trim();
    if (v.isEmpty() || v.length() > max)
      throw new IllegalArgumentException("Revisa el campo " + campo + ".");
    return v;
  }

  private BigDecimal dinero(String s) {
    try {
      return new BigDecimal(s).setScale(2, RoundingMode.UNNECESSARY);
    } catch (Exception e) {
      throw new IllegalArgumentException("El importe debe tener como máximo dos decimales.");
    }
  }

  private String opcion(String s, String... opciones) {
    if (!Arrays.asList(opciones).contains(s))
      throw new IllegalArgumentException("Opción no válida.");
    return s;
  }
}

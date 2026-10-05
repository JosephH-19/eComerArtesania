package com.example.demo.elmer.metricas;

import java.time.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MetricaService {
  private final JdbcTemplate jdbcTemplate;

  public MetricaService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Map<String, Object> calcular(String mes) {
    YearMonth periodo = mes == null || mes.isBlank() ? YearMonth.now() : YearMonth.parse(mes);
    LocalDate inicio = periodo.atDay(1), fin = periodo.plusMonths(1).atDay(1);
    Map<String, Object> datos = new HashMap<>();
    datos.put("mes", periodo.toString());
    datos.put(
        "ventas",
        obtener(
            "SELECT COUNT(*) AS pedidos,COALESCE(SUM(total),0) AS importe FROM pedido_t WHERE"
                + " estado<>'CANCELADO' AND creado>=? AND creado<?",
            inicio,
            fin));
    datos.put(
        "cobros",
        obtener(
            "SELECT COALESCE(SUM(monto),0) AS importe FROM pago_t WHERE estado='CONFIRMADO' AND"
                + " creado>=? AND creado<?",
            inicio,
            fin));
    datos.put(
        "unidades",
        obtener(
                "SELECT COALESCE(SUM(d.cantidad),0) AS n FROM detalle_t d JOIN pedido_t p ON"
                    + " p.id=d.pedido_id WHERE p.estado<>'CANCELADO' AND p.creado>=? AND"
                    + " p.creado<?",
                inicio,
                fin)
            .get("n"));
    datos.put(
        "resumenEstados",
        listar(
            "SELECT estado,COUNT(*) AS cantidad FROM pedido_t WHERE creado>=? AND creado<? GROUP BY"
                + " estado",
            inicio,
            fin));
    return datos;
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
}

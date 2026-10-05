package com.example.demo.elmer.metricas;

import com.example.demo.elmer.comun.SqlDAO;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class MetricaService {
  private final SqlDAO db;

  public MetricaService(SqlDAO db) {
    this.db = db;
  }

  public Map<String, Object> calcular(String mes) {
    YearMonth periodo = mes == null || mes.isBlank() ? YearMonth.now() : YearMonth.parse(mes);
    LocalDate inicio = periodo.atDay(1), fin = periodo.plusMonths(1).atDay(1);
    Map<String, Object> datos = new HashMap<>();
    datos.put("mes", periodo.toString());
    datos.put(
        "ventas",
        db.obtener(
            "SELECT COUNT(*) AS pedidos,COALESCE(SUM(total),0) AS importe FROM pedido_t WHERE"
                + " estado<>'CANCELADO' AND creado>=? AND creado<?",
            inicio,
            fin));
    datos.put(
        "cobros",
        db.obtener(
            "SELECT COALESCE(SUM(monto),0) AS importe FROM pago_t WHERE estado='CONFIRMADO' AND"
                + " creado>=? AND creado<?",
            inicio,
            fin));
    datos.put(
        "unidades",
        db.obtener(
                "SELECT COALESCE(SUM(d.cantidad),0) AS n FROM detalle_t d JOIN pedido_t p ON"
                    + " p.id=d.pedido_id WHERE p.estado<>'CANCELADO' AND p.creado>=? AND"
                    + " p.creado<?",
                inicio,
                fin)
            .get("n"));
    datos.put(
        "resumenEstados",
        db.listar(
            "SELECT estado,COUNT(*) AS cantidad FROM pedido_t WHERE creado>=? AND creado<? GROUP BY"
                + " estado",
            inicio,
            fin));
    return datos;
  }
}

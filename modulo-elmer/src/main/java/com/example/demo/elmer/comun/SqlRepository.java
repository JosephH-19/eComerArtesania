package com.example.demo.elmer.comun;

import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SqlRepository implements SqlDAO {
  private final JdbcTemplate jdbc;

  public SqlRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<Map<String, Object>> listar(String sql, Object... args) {
    return jdbc.query(
        sql,
        (rs, n) -> {
          Map<String, Object> row = new LinkedHashMap<>();
          for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
            row.put(rs.getMetaData().getColumnLabel(i).toLowerCase(), rs.getObject(i));
          return row;
        },
        args);
  }

  public Map<String, Object> obtener(String sql, Object... args) {
    var rows = listar(sql, args);
    if (rows.isEmpty())
      throw new IllegalArgumentException("No se encontró el registro solicitado.");
    return rows.get(0);
  }

  public int ejecutar(String sql, Object... args) {
    return jdbc.update(sql, args);
  }

  public long insertar(String sql, Object... args) {
    var key = new GeneratedKeyHolder();
    jdbc.update(
        conn -> {
          var ps = conn.prepareStatement(sql, new String[] {"ID"});
          for (int i = 0; i < args.length; i++) ps.setObject(i + 1, args[i]);
          return ps;
        },
        key);
    return key.getKey().longValue();
  }
}

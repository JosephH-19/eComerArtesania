package com.example.demo.elmer.comun;

import java.util.*;

public interface SqlDAO {
  List<Map<String, Object>> listar(String sql, Object... parametros);

  Map<String, Object> obtener(String sql, Object... parametros);

  int ejecutar(String sql, Object... parametros);

  long insertar(String sql, Object... parametros);
}

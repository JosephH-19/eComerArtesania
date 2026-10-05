package com.example.demo.elmer.pedidos;

import java.util.*;

public interface PedidoService {
  List<Map<String, Object>> listar(String busqueda, String estado, String pago);

  Map<String, Object> obtener(long id);

  Map<String, Object> consultar(String dni, String codigo);

  Map<String, Object> detalle(long id);

  void cambiarEstado(long id, String estado, String nota);
}

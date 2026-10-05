package com.example.demo.elmer.pagos;

import java.util.*;

public interface PagoService {
  List<Map<String, Object>> listar();

  void registrarPago(long pedido, Map<String, String> datos, boolean administrador);

  void resolverPago(long pago, boolean aprobar);
}

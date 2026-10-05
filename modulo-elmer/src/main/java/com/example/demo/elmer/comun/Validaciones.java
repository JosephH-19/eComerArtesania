package com.example.demo.elmer.comun;

import java.math.*;
import java.util.*;

public class Validaciones {
  public static String texto(Map<String, String> d, String campo, int max) {
    String v = d.getOrDefault(campo, "").trim();
    if (v.isEmpty() || v.length() > max)
      throw new IllegalArgumentException("Revisa el campo " + campo + ".");
    return v;
  }

  public static BigDecimal dinero(String s) {
    try {
      return new BigDecimal(s).setScale(2, RoundingMode.UNNECESSARY);
    } catch (Exception e) {
      throw new IllegalArgumentException("El importe debe tener como máximo dos decimales.");
    }
  }

  public static String opcion(String s, String... opciones) {
    if (!Arrays.asList(opciones).contains(s))
      throw new IllegalArgumentException("Opción no válida.");
    return s;
  }
}

package com.example.demo.elmer.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice(basePackages = "com.example.demo.elmer")
public class ErroresModulo {
  @ExceptionHandler({IllegalArgumentException.class, java.time.DateTimeException.class})
  public String datos(Exception e, Model m, HttpServletResponse response) {
    response.setStatus(400);
    m.addAttribute("mensaje", e.getMessage());
    return "elmer/error";
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public String duplicado(Model m, HttpServletResponse response) {
    response.setStatus(409);
    m.addAttribute(
        "mensaje",
        "Revisa los datos y la referencia: puede estar duplicada o el registro relacionado ya no"
            + " existe.");
    return "elmer/error";
  }
}

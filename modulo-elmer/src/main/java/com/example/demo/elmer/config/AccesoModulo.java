package com.example.demo.elmer.config;

import jakarta.servlet.http.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

/** Acceso local mínimo. El equipo puede sustituirlo por su autenticación común. */
@Configuration
public class AccesoModulo implements HandlerInterceptor, WebMvcConfigurer {
  @Value("${elmer.usuario:elmer}")
  private String usuario;

  @Value("${elmer.clave:}")
  private String clave;

  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(this).addPathPatterns("/elmer/**");
  }

  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
      throws Exception {
    var session = req.getSession();
    if (session.getAttribute("csrf") == null)
      session.setAttribute("csrf", UUID.randomUUID().toString());
    if (req.getRequestURI().startsWith("/elmer/admin/")) {
      if (clave.isBlank()) {
        res.setStatus(503);
        res.setContentType("text/plain;charset=UTF-8");
        res.getWriter()
            .write(
                "Configura la variable ELMER_CLAVE en IntelliJ y reinicia para abrir"
                    + " administración. No se ha creado ningún usuario ni pedido.");
        return false;
      }
      String recibido = req.getHeader("Authorization");
      String esperado =
          "Basic "
              + Base64.getEncoder()
                  .encodeToString((usuario + ":" + clave).getBytes(StandardCharsets.UTF_8));
      if (recibido == null
          || !MessageDigest.isEqual(
              esperado.getBytes(StandardCharsets.UTF_8),
              recibido.getBytes(StandardCharsets.UTF_8))) {
        res.setHeader("WWW-Authenticate", "Basic realm=\"Modulo Elmer\", charset=\"UTF-8\"");
        res.setStatus(401);
        return false;
      }
    }
    if (req.getMethod().equals("POST")
        && !Objects.equals(session.getAttribute("csrf"), req.getParameter("csrf"))) {
      res.sendError(403, "Recarga el formulario antes de enviarlo.");
      return false;
    }
    return true;
  }
}

package com.example.demo.elmer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/** Inicio independiente del módulo. No copiar esta clase si ya hay una aplicación común. */
@SpringBootApplication
public class ElmerApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(ElmerApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(ElmerApplication.class);
  }
}

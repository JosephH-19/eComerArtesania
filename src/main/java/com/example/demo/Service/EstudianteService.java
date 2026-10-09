package com.example.demo.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;

@Service
public class EstudianteService {

    private final JdbcTemplate jdbcTemplate;

    // Spring inyectará automáticamente el DataSource configurado
    public EstudianteService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String obtenerEstudiante(int id) {
        return jdbcTemplate.queryForObject(
            "SELECT concat(codigo,' ',nombre,' ' ,apellido) "
            +" FROM estudiante WHERE id = ? ",
            String.class,
            id
        );
    }
}
package com.example.demo.tipoProducto;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TipoProductoRepository implements TipoProductoDAO{
    
    private final JdbcTemplate jdbcTemplate;

    public TipoProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TipoProducto> tipoProductoRowMapper = (rs, rowNum) -> {
        return new TipoProducto(
            rs.getInt("id"),  
            rs.getString("nombre"),  
            rs.getDate("fechaCreacion").toLocalDate()  
        );
    };
    
    public List<TipoProducto> listaTipoProducto() {
        String query = "SELECT id, nombre, fechaCreacion FROM tipo_producto";
        return jdbcTemplate.query(query, tipoProductoRowMapper);
    }

    public TipoProducto obtenerTipoProductoPorId(int id) {
        String query = "SELECT id, nombre, fechaCreacion FROM tipo_producto WHERE id = ?";
        List<TipoProducto> result = jdbcTemplate.query(query, tipoProductoRowMapper, id);
        if (result.isEmpty()) {
            return null; 
        } else if (result.size() == 1) {
            return result.get(0);
        } else {
            throw new IllegalStateException("Expected 0 or 1 row but found " + result.size() + " for id " + id);
        }
    }

    public void crearTipoProducto(TipoProducto tipoProducto) {
        String query = "INSERT INTO tipo_producto (nombre, fechaCreacion) VALUES (?, ?)";
        jdbcTemplate.update(query, tipoProducto.getNombre(), tipoProducto.getFechaCreacion());
    }

}

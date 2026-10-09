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
        TipoProducto tipo = new TipoProducto(
            rs.getInt("id"),  
            rs.getString("nombre"),  
            rs.getDate("fechaCreacion").toLocalDate()  
        );
        tipo.setDescripcion(rs.getString("descripcion"));
        tipo.setEstado(rs.getString("estado"));
        return tipo;
    };
    
    public List<TipoProducto> listaTipoProducto() {
        String query = "SELECT * FROM tipo_producto ORDER BY id";
        return jdbcTemplate.query(query, tipoProductoRowMapper);
    }

    public TipoProducto obtenerTipoProductoPorId(int id) {
        String query = "SELECT * FROM tipo_producto WHERE id = ?";
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
        String query = "INSERT INTO tipo_producto (nombre, fechaCreacion, descripcion, estado) VALUES (?, ?, ?, 'ACTIVO')";
        jdbcTemplate.update(query, tipoProducto.getNombre(), tipoProducto.getFechaCreacion(), tipoProducto.getDescripcion());
    }

    public List<TipoProducto> listarActivos() {
        return jdbcTemplate.query("SELECT * FROM tipo_producto WHERE estado = 'ACTIVO' ORDER BY id", tipoProductoRowMapper);
    }

    public void desactivar(int id) {
        if (jdbcTemplate.update("UPDATE tipo_producto SET estado = 'INACTIVO' WHERE id = ?", id) == 0) {
            throw new IllegalArgumentException("La categoría no existe.");
        }
    }

}

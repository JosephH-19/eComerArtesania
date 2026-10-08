package com.example.demo.producto;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.tipoProducto.TipoProducto;

@Repository
public class ProductoRepository implements ProductoDAO{
    
    private final JdbcTemplate jdbcTemplate;

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Producto> ProductoRowMapper = (rs, rowNum) -> {
        return new Producto(
            rs.getInt("id"),  
            rs.getString("nombre"),  
            rs.getDate("fechaCreacion").toLocalDate(),
            new TipoProducto(
                rs.getInt("id_tipo_producto"),  
                rs.getString("nombre_tipo_producto"),  
                rs.getDate("fecha_Creacion_tipo_producto").toLocalDate() )
        );
    };
    
    @Override
    public List<Producto> ProductoReporte(Integer idTipoProducto) {
        System.out.println("idTipoProducto = " + idTipoProducto);
        String query = "SELECT producto.id as id, producto.nombre as nombre "+
              ", producto.fecha_creacion as fechaCreacion, "+
              " tipo_producto.id as id_tipo_producto , "+
              " tipo_producto.nombre as nombre_tipo_producto, " +
              " tipo_producto.fechaCreacion as fecha_Creacion_tipo_producto " +
              " FROM producto " +
              " join tipo_producto "+
              " on tipo_producto.id = producto.id_tipo_producto " +
              " where id_tipo_producto = ? " ;
        return jdbcTemplate.query(query, ProductoRowMapper,idTipoProducto  );
    }

    public List<Producto> listarTodos() {
        String query = "SELECT producto.id as id, producto.nombre as nombre, " +
                "producto.fecha_creacion as fechaCreacion, " +
                "tipo_producto.id as id_tipo_producto, " +
                "tipo_producto.nombre as nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion as fecha_Creacion_tipo_producto " +
                "FROM producto " +
                "JOIN tipo_producto ON tipo_producto.id = producto.id_tipo_producto " +
                "ORDER BY producto.id";
        return jdbcTemplate.query(query, ProductoRowMapper);
    }

}



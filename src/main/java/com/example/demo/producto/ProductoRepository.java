package com.example.demo.producto;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.tipoProducto.TipoProducto;

@Repository
public class ProductoRepository implements ProductoDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Producto> productoRowMapper = (rs, rowNum) -> {

        TipoProducto tipoProducto = new TipoProducto(
                rs.getInt("id_tipo_producto"),
                rs.getString("nombre_tipo_producto"),
                rs.getDate("fecha_creacion_tipo_producto").toLocalDate()
        );

        Producto producto = new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getDate("fechaCreacion").toLocalDate(),
                tipoProducto
        );

        producto.setDescripcion(rs.getString("descripcion"));
        producto.setMaterial(rs.getString("material"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setEstado(rs.getString("estado"));
        producto.setImagen(rs.getString("imagen"));

        return producto;
    };

    @Override
    public List<Producto> ProductoReporte(Integer idTipoProducto) {

        String query = "SELECT producto.id, producto.nombre, " +
                "producto.fecha_creacion AS fechaCreacion, " +
                "producto.descripcion, producto.material, producto.precio, " +
                "producto.stock, producto.estado, producto.imagen, " +
                "tipo_producto.id AS id_tipo_producto, " +
                "tipo_producto.nombre AS nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion AS fecha_creacion_tipo_producto " +
                "FROM producto " +
                "JOIN tipo_producto " +
                "ON tipo_producto.id = producto.id_tipo_producto " +
                "WHERE producto.id_tipo_producto = ? " +
                "ORDER BY producto.id";

        return jdbcTemplate.query(
                query, productoRowMapper, idTipoProducto
        );
    }

    public List<Producto> listarTodos() {

        String query = "SELECT producto.id, producto.nombre, " +
                "producto.fecha_creacion AS fechaCreacion, " +
                "producto.descripcion, producto.material, producto.precio, " +
                "producto.stock, producto.estado, producto.imagen, " +
                "tipo_producto.id AS id_tipo_producto, " +
                "tipo_producto.nombre AS nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion AS fecha_creacion_tipo_producto " +
                "FROM producto " +
                "JOIN tipo_producto " +
                "ON tipo_producto.id = producto.id_tipo_producto " +
                "ORDER BY producto.id";

        return jdbcTemplate.query(query, productoRowMapper);
    }

    @Override
    public void guardar(Producto producto) {

        String consulta = "SELECT COUNT(*) FROM tipo_producto WHERE id = ?";

        Integer cantidad = jdbcTemplate.queryForObject(
                consulta,
                Integer.class,
                producto.getTipoProducto().getId()
        );

        if (cantidad == null || cantidad == 0) {
            throw new IllegalArgumentException(
                    "Selecciona un tipo de producto existente."
            );
        }

        String query = "INSERT INTO producto " +
                "(nombre, fecha_creacion, id_tipo_producto, descripcion, " +
                "material, precio, stock, estado, imagen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                query,
                producto.getNombre(),
                producto.getFechaCreacion(),
                producto.getTipoProducto().getId(),
                producto.getDescripcion(),
                producto.getMaterial(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getEstado(),
                producto.getImagen()
        );
    }

    @Override
    public Producto buscarPorId(int id) {
        String query = "SELECT producto.id AS id, " +
                "producto.nombre AS nombre, " +
                "producto.fecha_creacion AS fechaCreacion, " +
                "producto.descripcion, producto.material, " +
                "producto.precio, producto.stock, " +
                "producto.estado, producto.imagen, " +
                "tipo_producto.id AS id_tipo_producto, " +
                "tipo_producto.nombre AS nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion AS fecha_creacion_tipo_producto " +
                "FROM producto " +
                "JOIN tipo_producto ON tipo_producto.id = producto.id_tipo_producto " +
                "WHERE producto.id = ?";

        List<Producto> productos =
                jdbcTemplate.query(query, productoRowMapper, id);

        if (productos.isEmpty()) {
            return null;
        }

        return productos.get(0);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        String query = "SELECT producto.id AS id, " +
                "producto.nombre AS nombre, " +
                "producto.fecha_creacion AS fechaCreacion, " +
                "producto.descripcion, producto.material, " +
                "producto.precio, producto.stock, " +
                "producto.estado, producto.imagen, " +
                "tipo_producto.id AS id_tipo_producto, " +
                "tipo_producto.nombre AS nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion AS fecha_creacion_tipo_producto " +
                "FROM producto " +
                "JOIN tipo_producto ON tipo_producto.id = producto.id_tipo_producto " +
                "WHERE LOWER(producto.nombre) LIKE LOWER(?) " +
                "ORDER BY producto.id";

        return jdbcTemplate.query(
                query, productoRowMapper, "%" + nombre + "%");
    }
}
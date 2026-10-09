package com.example.demo.producto;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.example.demo.tipoProducto.TipoProducto;
import com.example.demo.artesano.Artesano;

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

        Artesano artesano = new Artesano();
        artesano.setId(rs.getInt("id_artesano"));
        artesano.setNombres(rs.getString("artesano_nombres"));
        artesano.setApellidos(rs.getString("artesano_apellidos"));
        producto.setArtesano(artesano);
        return producto;
    };

    @Override
    public List<Producto> ProductoReporte(Integer idTipoProducto) {

        String query = "SELECT producto.id, producto.nombre, " +
                "producto.fecha_creacion AS fechaCreacion, " +
                "producto.descripcion, producto.material, producto.precio, " +
                "producto.stock, producto.estado, producto.imagen, " +
                "producto.id_artesano, artesano.nombres AS artesano_nombres, " +
                "artesano.apellidos AS artesano_apellidos, " +
                "tipo_producto.id AS id_tipo_producto, " +
                "tipo_producto.nombre AS nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion AS fecha_creacion_tipo_producto " +
                "FROM producto " +
                "JOIN artesano ON artesano.id = producto.id_artesano " +
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
                "producto.id_artesano, artesano.nombres AS artesano_nombres, " +
                "artesano.apellidos AS artesano_apellidos, " +
                "tipo_producto.id AS id_tipo_producto, " +
                "tipo_producto.nombre AS nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion AS fecha_creacion_tipo_producto " +
                "FROM producto " +
                "JOIN artesano ON artesano.id = producto.id_artesano " +
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
                "(nombre, fecha_creacion, id_tipo_producto, id_artesano, descripcion, " +
                "material, precio, stock, estado, imagen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                query,
                producto.getNombre(),
                producto.getFechaCreacion(),
                producto.getTipoProducto().getId(),
                producto.getArtesano().getId(),
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
                "producto.id_artesano, artesano.nombres AS artesano_nombres, " +
                "artesano.apellidos AS artesano_apellidos, " +
                "tipo_producto.id AS id_tipo_producto, " +
                "tipo_producto.nombre AS nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion AS fecha_creacion_tipo_producto " +
                "FROM producto " +
                "JOIN artesano ON artesano.id = producto.id_artesano " +
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
    public void actualizar(Producto producto) {
        String consultaTipo =
                "SELECT COUNT(*) FROM tipo_producto WHERE id = ?";

        Integer cantidad = jdbcTemplate.queryForObject(
                consultaTipo,
                Integer.class,
                producto.getTipoProducto().getId());

        if (cantidad == null || cantidad == 0) {
            throw new IllegalArgumentException(
                    "Selecciona un tipo de producto existente.");
        }

        String query = "UPDATE producto SET " +
                "nombre = ?, descripcion = ?, material = ?, " +
                "precio = ?, stock = ?, imagen = ?, " +
                "id_tipo_producto = ?, id_artesano = ? " +
                "WHERE id = ?";

        int filas = jdbcTemplate.update(
                query,
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getMaterial(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagen(),
                producto.getTipoProducto().getId(),
                producto.getArtesano().getId(),
                producto.getId());

        if (filas == 0) {
            throw new IllegalArgumentException("El producto no existe.");
        }
    }

    @Override
    public List<Producto> filtrar(String nombre, String disponibilidad) {
        String query = "SELECT producto.id AS id, " +
                "producto.nombre AS nombre, " +
                "producto.fecha_creacion AS fechaCreacion, " +
                "producto.descripcion, producto.material, " +
                "producto.precio, producto.stock, " +
                "producto.estado, producto.imagen, " +
                "producto.id_artesano, artesano.nombres AS artesano_nombres, " +
                "artesano.apellidos AS artesano_apellidos, " +
                "tipo_producto.id AS id_tipo_producto, " +
                "tipo_producto.nombre AS nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion AS fecha_creacion_tipo_producto " +
                "FROM producto " +
                "JOIN artesano ON artesano.id = producto.id_artesano " +
                "JOIN tipo_producto ON tipo_producto.id = producto.id_tipo_producto " +
                "WHERE LOWER(producto.nombre) LIKE LOWER(?) ";

        if ("CON_STOCK".equals(disponibilidad)) {
            query += "AND producto.stock > 0 ";
        } else if ("SIN_STOCK".equals(disponibilidad)) {
            query += "AND producto.stock = 0 ";
        }

        query += "ORDER BY producto.id";

        return jdbcTemplate.query(
                query, productoRowMapper, "%" + nombre + "%");
    }

    @Override
    public List<Producto> catalogo(String nombre, String disponibilidad, BigDecimal precioMinimo, BigDecimal precioMaximo) {

        String query = "SELECT producto.id AS id, " +
                "producto.nombre AS nombre, " +
                "producto.fecha_creacion AS fechaCreacion, " +
                "producto.descripcion, producto.material, " +
                "producto.precio, producto.stock, " +
                "producto.estado, producto.imagen, " +
                "producto.id_artesano, artesano.nombres AS artesano_nombres, " +
                "artesano.apellidos AS artesano_apellidos, " +
                "tipo_producto.id AS id_tipo_producto, " +
                "tipo_producto.nombre AS nombre_tipo_producto, " +
                "tipo_producto.fechaCreacion AS fecha_creacion_tipo_producto " +
                "FROM producto " +
                "JOIN artesano ON artesano.id = producto.id_artesano " +
                "JOIN tipo_producto ON tipo_producto.id = producto.id_tipo_producto " +
                "WHERE producto.estado = 'ACTIVO' " +
                "AND LOWER(producto.nombre) LIKE LOWER(?) ";

        List<Object> parametros = new ArrayList<>();
        parametros.add("%" + nombre + "%");

        if ("CON_STOCK".equals(disponibilidad)) {
            query += "AND producto.stock > 0 ";
        } else if ("SIN_STOCK".equals(disponibilidad)) {
            query += "AND producto.stock = 0 ";
        }

        if (precioMinimo != null) {
            query += "AND producto.precio >= ? ";
            parametros.add(precioMinimo);
        }

        if (precioMaximo != null) {
            query += "AND producto.precio <= ? ";
            parametros.add(precioMaximo);
        }

        query += "ORDER BY producto.id";

        return jdbcTemplate.query(
                query, productoRowMapper, parametros.toArray());
    }

    @Override
    public void cambiarEstado(int id, String estado) {
        String query = "UPDATE producto SET estado = ? WHERE id = ?";

        int filas = jdbcTemplate.update(query, estado, id);

        if (filas == 0) {
            throw new IllegalArgumentException("El producto no existe.");
        }
    }

    public boolean descontarStock(int id, int cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        return jdbcTemplate.update("UPDATE producto SET stock = stock - ? WHERE id = ? AND stock >= ? AND estado = 'ACTIVO'",
                cantidad, id, cantidad) == 1;
    }
}

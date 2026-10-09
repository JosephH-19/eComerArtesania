package com.example.demo.Artesanos;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ArtesanoRepository implements ArtesanoDAO {

    private final JdbcTemplate jdbcTemplate;

    public ArtesanoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Convierte cada fila de la tabla en un objeto Artesano.
    private final RowMapper<Artesano> artesanoRowMapper = (rs, rowNum) -> {
        Artesano artesano = new Artesano();

        artesano.setId(rs.getInt("id"));
        artesano.setNombre(rs.getString("nombre"));
        artesano.setEspecialidad(rs.getString("especialidad"));

        Date fecha = rs.getDate("fecha_nacimiento");
        if (fecha != null) {
            artesano.setFechaNacimiento(fecha.toLocalDate());
        }

        artesano.setTelefono(rs.getString("telefono"));
        artesano.setCorreoElectronico(rs.getString("correo_electronico"));
        artesano.setEdad(rs.getInt("edad"));
        artesano.setAnosExp(rs.getInt("anos_exp"));
        artesano.setProductosElaborados(rs.getInt("productos_elaborados"));
        artesano.setEstado(rs.getBoolean("estado"));
        artesano.setImagen(rs.getString("imagen"));

        return artesano;
    };

    @Override
    public List<Artesano> listarArtesanos() {
        String sql = """
            SELECT id, nombre, especialidad, fecha_nacimiento,
                   telefono, correo_electronico, edad, anos_exp,
                   productos_elaborados, estado, imagen
            FROM artesano
            ORDER BY id
            """;

        return jdbcTemplate.query(sql, artesanoRowMapper);
    }

    @Override
    public Artesano obtenerArtesanoPorId(int id) {
        String sql = """
            SELECT id, nombre, especialidad, fecha_nacimiento,
                   telefono, correo_electronico, edad, anos_exp,
                   productos_elaborados, estado, imagen
            FROM artesano
            WHERE id = ?
            """;

        List<Artesano> resultados =
                jdbcTemplate.query(sql, artesanoRowMapper, id);

        if (resultados.isEmpty()) {
            return null;
        }

        return resultados.get(0);
    }

    @Override
    public void crearArtesano(Artesano artesano) {
        String sql = """
            INSERT INTO artesano (
                nombre, especialidad, fecha_nacimiento, telefono,
                correo_electronico, edad, anos_exp,
                productos_elaborados, estado, imagen
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        GeneratedKeyHolder generadorClave = new GeneratedKeyHolder();

        jdbcTemplate.update(conexion -> {
            PreparedStatement sentencia = conexion.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            sentencia.setString(1, artesano.getNombre());
            sentencia.setString(2, artesano.getEspecialidad());
            sentencia.setObject(3, artesano.getFechaNacimiento());
            sentencia.setString(4, artesano.getTelefono());
            sentencia.setString(5, artesano.getCorreoElectronico());
            sentencia.setInt(6, artesano.getEdad());
            sentencia.setInt(7, artesano.getAnosExp());
            sentencia.setInt(8, artesano.getProductosElaborados());
            sentencia.setBoolean(9, artesano.isEstado());
            sentencia.setString(10, artesano.getImagen());

            return sentencia;
        }, generadorClave);

        if (generadorClave.getKey() != null) {
            artesano.setId(generadorClave.getKey().intValue());
        }
    }

    @Override
    public void actualizarArtesano(Artesano artesano) {
        String sql = """
            UPDATE artesano
            SET nombre = ?,
                especialidad = ?,
                fecha_nacimiento = ?,
                telefono = ?,
                correo_electronico = ?,
                edad = ?,
                anos_exp = ?,
                productos_elaborados = ?,
                estado = ?,
                imagen = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(
                sql,
                artesano.getNombre(),
                artesano.getEspecialidad(),
                artesano.getFechaNacimiento(),
                artesano.getTelefono(),
                artesano.getCorreoElectronico(),
                artesano.getEdad(),
                artesano.getAnosExp(),
                artesano.getProductosElaborados(),
                artesano.isEstado(),
                artesano.getImagen(),
                artesano.getId()
        );
    }

    @Override
    public void eliminarArtesano(int id) {
        String sql = "DELETE FROM artesano WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
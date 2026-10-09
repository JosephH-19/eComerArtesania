package com.example.demo.Artesanos;
import java.util.List;

public interface ArtesanoDAO {
    // Consultar todos los artesanos.
    List<Artesano> listarArtesanos();

    // Consultar un artesano mediante su identificador.
    Artesano obtenerArtesanoPorId(int id);

    // Registrar un nuevo artesano.
    void crearArtesano(Artesano artesano);

    // Actualizar los datos de un artesano.
    void actualizarArtesano(Artesano artesano);

    // Eliminar un artesano mediante su identificador.
    void eliminarArtesano(int id);
}

package com.example.demo.Artesanos;

import java.util.List;

public interface ArtesanoService {

    List<Artesano> listarArtesanos();

    Artesano obtenerArtesanoPorId(int id);

    void crearArtesano(Artesano artesano);

    void actualizarArtesano(Artesano artesano);

    void eliminarArtesano(int id);
}
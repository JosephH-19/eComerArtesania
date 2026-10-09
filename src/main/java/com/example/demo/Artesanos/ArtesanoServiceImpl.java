package com.example.demo.Artesanos;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ArtesanoServiceImpl implements ArtesanoService {

    private final ArtesanoDAO artesanoDAO;

    public ArtesanoServiceImpl(ArtesanoDAO artesanoDAO) {
        this.artesanoDAO = artesanoDAO;
    }

    @Override
    public List<Artesano> listarArtesanos() {
        return artesanoDAO.listarArtesanos();
    }

    @Override
    public Artesano obtenerArtesanoPorId(int id) {
        return artesanoDAO.obtenerArtesanoPorId(id);
    }

    @Override
    public void crearArtesano(Artesano artesano) {
        artesanoDAO.crearArtesano(artesano);
    }

    @Override
    public void actualizarArtesano(Artesano artesano) {
        artesanoDAO.actualizarArtesano(artesano);
    }

    @Override
    public void eliminarArtesano(int id) {
        artesanoDAO.eliminarArtesano(id);
    }
}
package com.tallerbuendia.api.artesanos;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArtesanoService {
    private final ArtesanoRepository repository;
    public ArtesanoService(ArtesanoRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<Artesano> listar() { return repository.findAll(); }
    @Transactional(readOnly = true)
    public Artesano obtener(Long id) { return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Artesano no encontrado: " + id)); }
    public Artesano crear(String nombre, String especialidad, Integer experiencia, String correo, String telefono, boolean activo) {
        return repository.save(new Artesano(nombre.trim(), especialidad, experiencia, correo, telefono, activo));
    }
    public Artesano actualizar(Long id, String nombre, String especialidad, Integer experiencia, String correo, String telefono, boolean activo) {
        Artesano artisan = obtener(id);
        artisan.setNombre(nombre.trim());
        artisan.setEspecialidad(especialidad);
        artisan.setAniosExperiencia(experiencia);
        artisan.setCorreo(correo);
        artisan.setTelefono(telefono);
        artisan.setActivo(activo);
        return repository.save(artisan);
    }
    public void eliminar(Long id) { Artesano artisan = obtener(id); artisan.setActivo(false); }
}

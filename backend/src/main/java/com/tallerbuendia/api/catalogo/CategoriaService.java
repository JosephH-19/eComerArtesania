package com.tallerbuendia.api.catalogo;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoriaService {
    private final CategoriaRepository repository;
    public CategoriaService(CategoriaRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<Categoria> listar() { return repository.findAll(); }
    @Transactional(readOnly = true)
    public Categoria obtener(Long id) { return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Categoría no encontrada: " + id)); }
    public Categoria crear(String nombre, String descripcion, boolean activa) {
        if (repository.existsByNombreIgnoreCase(nombre)) throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        return repository.save(new Categoria(nombre.trim(), descripcion, activa));
    }
    public Categoria actualizar(Long id, String nombre, String descripcion, boolean activa) {
        Categoria category = obtener(id);
        category.setNombre(nombre.trim());
        category.setDescripcion(descripcion);
        category.setActiva(activa);
        return repository.save(category);
    }
    public void eliminar(Long id) { repository.delete(obtener(id)); }
}

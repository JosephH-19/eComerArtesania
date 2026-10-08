package com.tallerbuendia.api.catalogo;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallerbuendia.api.artesanos.Artesano;
import com.tallerbuendia.api.artesanos.ArtesanoRepository;

@Service
@Transactional
public class ProductoService {
    private final ProductoRepository productos;
    private final CategoriaRepository categorias;
    private final ArtesanoRepository artesanos;
    public ProductoService(ProductoRepository productos, CategoriaRepository categorias, ArtesanoRepository artesanos) {
        this.productos = productos; this.categorias = categorias; this.artesanos = artesanos;
    }
    @Transactional(readOnly = true)
    public List<Producto> listar(Long categoriaId, String query) {
        List<Producto> result = categoriaId == null ? productos.findAll() : productos.findByCategoriaId(categoriaId);
        if (query == null || query.isBlank()) return result;
        String term = query.trim().toLowerCase();
        return result.stream().filter(product -> product.getNombre().toLowerCase().contains(term)
                || (product.getDescripcion() != null && product.getDescripcion().toLowerCase().contains(term))).toList();
    }
    @Transactional(readOnly = true)
    public Producto obtener(Long id) { return productos.findById(id).orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + id)); }
    public Producto guardar(Long id, String nombre, String descripcion, String material, BigDecimal precio,
                            Integer stock, String estado, Long categoriaId, Long artesanoId, String imagen) {
        Categoria category = categoriaId == null ? null : categorias.findById(categoriaId)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada: " + categoriaId));
        Artesano artisan = artesanoId == null ? null : artesanos.findById(artesanoId)
                .orElseThrow(() -> new NoSuchElementException("Artesano no encontrado: " + artesanoId));
        Producto product = id == null ? new Producto(nombre.trim(), descripcion, material, precio, stock, estado, category, artisan, imagen) : obtener(id);
        product.setNombre(nombre.trim()); product.setDescripcion(descripcion); product.setMaterial(material);
        product.setPrecio(precio); product.setStock(stock); product.setEstado(estado == null ? "disponible" : estado);
        product.setCategoria(category); product.setArtesano(artisan); product.setImagen(imagen);
        return productos.save(product);
    }
    public void eliminar(Long id) { productos.delete(obtener(id)); }
}

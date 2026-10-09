package com.example.demo.producto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.math.RoundingMode;
import com.example.demo.descuento.DescuentoService;
import com.example.demo.artesano.ArtesanoService;
import com.example.demo.artesano.Artesano;
import com.example.demo.tipoProducto.TipoProductoService;
import com.example.demo.tipoProducto.TipoProducto;

import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDAO productoDAO;

    private final TipoProductoService categorias;
    private final ArtesanoService artesanos;
    private final DescuentoService descuentos;

    public ProductoServiceImpl(ProductoDAO productoDAO, TipoProductoService categorias, ArtesanoService artesanos, DescuentoService descuentos) {
        this.productoDAO = productoDAO;
        this.categorias = categorias;
        this.artesanos = artesanos;
        this.descuentos = descuentos;
    }

    private void validarRelaciones(Producto producto, Producto anterior) {
        TipoProducto categoria = categorias.obtenerTipoProductoPorId(producto.getTipoProducto().getId());
        boolean mismaCategoria = anterior != null && anterior.getTipoProducto().getId().equals(producto.getTipoProducto().getId());
        if (categoria == null || (!"ACTIVO".equals(categoria.getEstado()) && !mismaCategoria)) {
            throw new IllegalArgumentException("Selecciona una categoría activa.");
        }
        if (producto.getArtesano() == null) {
            throw new IllegalArgumentException("Selecciona un artesano activo.");
        }
        Artesano artesano = artesanos.buscarPorId(producto.getArtesano().getId());
        boolean mismoArtesano = anterior != null && anterior.getArtesano().getId() == producto.getArtesano().getId();
        if (artesano == null || (!"ACTIVO".equals(artesano.getEstado()) && !mismoArtesano)) {
            throw new IllegalArgumentException("Selecciona un artesano activo.");
        }
    }

    @Override
    public List<Producto> ProductoReporte(Integer idTipoProducto) {
        return productoDAO.ProductoReporte(idTipoProducto);
    }

    @Override
    public List<Producto> listarTodos() {
        return productoDAO.listarTodos();
    }

    private void validarProducto(Producto producto) {

        if (producto.getNombre() == null ||
                producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        if (producto.getDescripcion() == null ||
                producto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La descripción es obligatoria."
            );
        }

        if (producto.getMaterial() == null ||
                producto.getMaterial().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El material es obligatorio."
            );
        }

        if (producto.getNombre().length() > 200 ||
                producto.getDescripcion().length() > 1000 ||
                producto.getMaterial().length() > 200) {
            throw new IllegalArgumentException(
                    "El nombre, descripción o material supera el tamaño permitido."
            );
        }

        if (producto.getPrecio() == null ||
                producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0 ||
                producto.getPrecio().compareTo(
                        new BigDecimal("99999999.99")) > 0 ||
                producto.getPrecio().scale() > 2) {
            throw new IllegalArgumentException(
                    "Ingresa un precio mayor que cero, con hasta dos decimales."
            );
        }

        if (producto.getStock() < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }

        if (producto.getTipoProducto() == null ||
                producto.getTipoProducto().getId() == null ||
                producto.getTipoProducto().getId() <= 0) {
            throw new IllegalArgumentException(
                    "Selecciona un tipo de producto."
            );
        }

        if (producto.getImagen() == null) {
            producto.setImagen("");
        }

        if (producto.getImagen().length() > 500) {
            throw new IllegalArgumentException(
                    "La ruta de imagen no puede superar 500 caracteres."
            );
        }

        producto.setNombre(producto.getNombre().trim());
        producto.setDescripcion(producto.getDescripcion().trim());
        producto.setMaterial(producto.getMaterial().trim());
    }

    @Override
    public Producto buscarPorId(int id) {
        Producto producto = productoDAO.buscarPorId(id);
        if (producto != null) {
            aplicarPrecio(producto);
        }
        return producto;
    }

    private void aplicarPrecio(Producto producto) {
        BigDecimal porcentaje = descuentos.porcentajeVigente(producto.getId(), LocalDate.now());
        producto.setPorcentajeDescuento(porcentaje);
        BigDecimal factor = BigDecimal.ONE.subtract(porcentaje.divide(new BigDecimal("100")));
        producto.setPrecioFinal(producto.getPrecio().multiply(factor).setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public void guardar(Producto producto) {
        validarProducto(producto);

        validarRelaciones(producto, null);
        producto.setFechaCreacion(LocalDate.now());
        producto.setEstado("ACTIVO");

        productoDAO.guardar(producto);
    }

    @Override
    public void actualizar(Producto producto) {
        Producto existente = productoDAO.buscarPorId(producto.getId());

        if (existente == null) {
            throw new IllegalArgumentException("El producto no existe.");
        }

        validarProducto(producto);

        validarRelaciones(producto, existente);
        productoDAO.actualizar(producto);
    }

    @Override
    public List<Producto> filtrar(String nombre, String disponibilidad) {
        if (nombre == null) {
            nombre = "";
        }

        return productoDAO.filtrar(nombre.trim(), disponibilidad);
    }

    @Override
    public List<Producto> catalogo(
            String nombre,
            String disponibilidad,
            BigDecimal precioMinimo,
            BigDecimal precioMaximo) {
        return catalogo(nombre, disponibilidad, precioMinimo, precioMaximo, null);
    }

    public List<Producto> catalogo(String nombre, String disponibilidad,
            BigDecimal precioMinimo, BigDecimal precioMaximo, Integer categoria) {

        if (nombre == null) {
            nombre = "";
        }

        if (precioMinimo != null &&
                (precioMinimo.compareTo(BigDecimal.ZERO) < 0 ||
                        precioMinimo.scale() > 2)) {
            throw new IllegalArgumentException(
                    "El precio mínimo debe ser positivo o cero, con hasta dos decimales.");
        }

        if (precioMaximo != null &&
                (precioMaximo.compareTo(BigDecimal.ZERO) < 0 ||
                        precioMaximo.scale() > 2)) {
            throw new IllegalArgumentException(
                    "El precio máximo debe ser positivo o cero, con hasta dos decimales.");
        }

        if (precioMinimo != null && precioMaximo != null &&
                precioMinimo.compareTo(precioMaximo) > 0) {
            throw new IllegalArgumentException(
                    "El precio mínimo no puede ser mayor que el máximo.");
        }

        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : productoDAO.catalogo(nombre.trim(), disponibilidad, null, null)) {
            aplicarPrecio(producto);
            boolean cumpleCategoria = categoria == null || categoria == 0 || producto.getTipoProducto().getId().equals(categoria);
            boolean cumpleMinimo = precioMinimo == null || producto.getPrecioFinal().compareTo(precioMinimo) >= 0;
            boolean cumpleMaximo = precioMaximo == null || producto.getPrecioFinal().compareTo(precioMaximo) <= 0;
            if (cumpleCategoria && cumpleMinimo && cumpleMaximo) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    @Override
    public void cambiarEstado(int id, String estado) {
        if (!"ACTIVO".equals(estado) &&
                !"INACTIVO".equals(estado)) {
            throw new IllegalArgumentException(
                    "El estado debe ser ACTIVO o INACTIVO.");
        }

        productoDAO.cambiarEstado(id, estado);
    }
}

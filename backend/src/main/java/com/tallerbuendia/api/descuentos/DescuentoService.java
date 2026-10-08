package com.tallerbuendia.api.descuentos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallerbuendia.api.catalogo.Producto;

@Service
@Transactional
public class DescuentoService {
    private final DescuentoRepository repository;
    public DescuentoService(DescuentoRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<Descuento> listar() { return repository.findAll(); }
    @Transactional(readOnly = true)
    public Descuento obtener(Long id) { return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Descuento no encontrado: " + id)); }
    public Descuento guardar(Long id, String nombre, String descripcion, String tipo, BigDecimal valor,
                             LocalDate inicio, LocalDate fin, String aplicableA, Long objetivoId, boolean activo) {
        Descuento discount = id == null ? new Descuento(nombre.trim(), descripcion, tipo, valor, inicio, fin, aplicableA, objetivoId, activo) : obtener(id);
        discount.setNombre(nombre.trim()); discount.setDescripcion(descripcion); discount.setTipo(tipo.toUpperCase());
        discount.setValor(valor); discount.setFechaInicio(inicio); discount.setFechaFin(fin);
        discount.setAplicableA(aplicableA); discount.setObjetivoId(objetivoId); discount.setActivo(activo);
        return repository.save(discount);
    }
    public void eliminar(Long id) { repository.delete(obtener(id)); }
    @Transactional(readOnly = true)
    public BigDecimal precioFinal(Producto product) {
        LocalDate today = LocalDate.now();
        List<Descuento> available = repository.findAll().stream().filter(Descuento::isActivo)
                .filter(discount -> discount.getFechaInicio() == null || !discount.getFechaInicio().isAfter(today))
                .filter(discount -> discount.getFechaFin() == null || !discount.getFechaFin().isBefore(today))
                .filter(discount -> "todos".equalsIgnoreCase(discount.getAplicableA())
                        || ("producto".equalsIgnoreCase(discount.getAplicableA()) && product.getId().equals(discount.getObjetivoId()))
                        || ("categoria".equalsIgnoreCase(discount.getAplicableA()) && product.getCategoria() != null && product.getCategoria().getId().equals(discount.getObjetivoId())))
                .toList();
        BigDecimal result = product.getPrecio();
        for (Descuento discount : available) {
            BigDecimal reduced = "PORCENTAJE".equalsIgnoreCase(discount.getTipo())
                    ? result.multiply(BigDecimal.ONE.subtract(discount.getValor().movePointLeft(2)))
                    : result.subtract(discount.getValor());
            result = reduced.max(BigDecimal.ZERO);
        }
        return result.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}

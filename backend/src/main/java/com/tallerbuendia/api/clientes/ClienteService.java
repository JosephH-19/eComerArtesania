package com.tallerbuendia.api.clientes;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService {
    private final ClienteRepository repository;
    public ClienteService(ClienteRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<Cliente> listar(String query, String estado) {
        String term = query == null ? "" : query.trim().toLowerCase();
        String state = estado == null ? "" : estado.trim().toLowerCase();
        return repository.findAll().stream()
                .filter(client -> !"eliminado".equalsIgnoreCase(client.getEstado()))
                .filter(client -> term.isBlank() || client.getNombre().toLowerCase().contains(term)
                        || client.getDni().contains(term) || client.getCorreo().toLowerCase().contains(term))
                .filter(client -> state.isBlank() || "todos".equals(state) || client.getEstado().equalsIgnoreCase(state))
                .toList();
    }

    @Transactional(readOnly = true)
    public Cliente obtener(Long id) { return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id)); }

    @Transactional(readOnly = true)
    public Cliente obtenerPorDni(String dni) { return repository.findByDni(dni).orElseThrow(() -> new NoSuchElementException("Cliente no encontrado")); }

    public Cliente registrar(String nombre, String dni, String correo, String telefono, String direccion,
                             String distrito, java.time.LocalDate fechaNacimiento, String genero, String estado) {
        if (repository.existsByDni(dni)) throw new IllegalArgumentException("Ya existe un cliente con ese DNI");
        return repository.save(new Cliente(nombre.trim(), dni, correo.trim(), telefono.trim(), direccion,
                distrito, fechaNacimiento, genero, estado));
    }

    public void eliminar(Long id) { obtener(id).setEstado("eliminado"); }
}

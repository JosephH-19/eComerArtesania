package com.tallerbuendia.api.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tallerbuendia.api.artesanos.Artesano;
import com.tallerbuendia.api.artesanos.ArtesanoRepository;
import com.tallerbuendia.api.catalogo.Categoria;
import com.tallerbuendia.api.catalogo.CategoriaRepository;
import com.tallerbuendia.api.catalogo.Producto;
import com.tallerbuendia.api.catalogo.ProductoRepository;
import com.tallerbuendia.api.clientes.Cliente;
import com.tallerbuendia.api.clientes.ClienteRepository;
import com.tallerbuendia.api.descuentos.Descuento;
import com.tallerbuendia.api.descuentos.DescuentoRepository;

@Configuration
public class DemoDataInitializer {
    @Bean
    CommandLineRunner initializeDemoData(CategoriaRepository categories, ArtesanoRepository artisans,
                                         ProductoRepository products, ClienteRepository clients,
                                         DescuentoRepository discounts) {
        return args -> {
            if (categories.count() == 0) {
                Categoria ponchos = categories.save(new Categoria("Ponchos", "Prendas tejidas tradicionales", true));
                Categoria mantas = categories.save(new Categoria("Mantas", "Mantas de lana y alpaca", true));
                Artesano jose = artisans.save(new Artesano("José Buendía", "Telar tradicional", 40,
                        "jose.buendia@tallerbuendia.com", "987654321", true));
                Artesano maria = artisans.save(new Artesano("María López", "Diseños Sicaya", 30,
                        "maria.lopez@tallerbuendia.com", "912345678", true));
                products.saveAll(List.of(
                        new Producto("Poncho Callhua", "Diseño tradicional de Hualhuas", "Lana de oveja",
                                new BigDecimal("75.00"), 12, "disponible", ponchos, jose, "/assets/productos/8.jpg"),
                        new Producto("Poncho Andino", "Diseño tradicional Sicaya", "Lana de alpaca",
                                new BigDecimal("48.00"), 5, "disponible", ponchos, maria, "/assets/productos/6.jpg"),
                        new Producto("Manta Tradicional", "Manta tejida a mano", "Lana natural",
                                new BigDecimal("95.00"), 4, "disponible", mantas, jose, "/assets/productos/4.jpg")
                ));
            }
            if (clients.count() == 0) {
                clients.saveAll(List.of(
                        new Cliente("Juan Pérez García", "12345678", "juan.perez@example.com", "987654321",
                                "Calle Real 123", "Huancayo", null, "Masculino", "activo"),
                        new Cliente("María López Martínez", "87654321", "maria.lopez@example.com", "912345678",
                                "", "Hualhuas", null, "Femenino", "activo")
                ));
            }
                        if (discounts.count() == 0) {
                                discounts.save(new Descuento("Descuento de bienvenida", "Descuento de demostración en ponchos",
                                                "PORCENTAJE", new BigDecimal("10.00"), java.time.LocalDate.now().minusDays(1),
                                                java.time.LocalDate.now().plusMonths(2), "categoria", 1L, true));
                        }
        };
    }
}

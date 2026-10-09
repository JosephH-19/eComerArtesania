package com.tallerbuendia.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:artesania-test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.h2.console.enabled=false"
})
@AutoConfigureMockMvc
class ApiSmokeTest {
    @Autowired
    private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

    @Test
    void healthAndSeededCatalogAreAvailable() throws Exception {
        mvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
        mvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").exists());
        mvc.perform(get("/api/descuentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Descuento de bienvenida"));
        mvc.perform(get("/api/metricas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoPendiente").exists());
        mvc.perform(get("/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void customerCanBeCreatedAndFoundByDni() throws Exception {
        String payload = """
                {"nombre":"Cliente Test","dni":"55667788","correo":"cliente.test@example.com","telefono":"987654321","estado":"activo"}
                """;
        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni").value("55667788"));
        mvc.perform(get("/api/clientes/dni/55667788"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cliente Test"));
    }

    @Test
    void orderPaymentAndDniLookupWorkTogether() throws Exception {
        String orderPayload = """
                {"nombre":"Pedido Test","dni":"66778899","correo":"pedido.test@example.com","telefono":"987654321","metodoEntrega":"RECOJO_TALLER","items":[{"productoId":1,"cantidad":1}]}
                """;
        String orderJson = mvc.perform(post("/api/pedidos").contentType(MediaType.APPLICATION_JSON).content(orderPayload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode order = objectMapper.readTree(orderJson);
        long orderId = order.get("id").asLong();

        String paymentPayload = """
                {"pedidoId":%d,"metodo":"Yape","monto":%s,"referencia":"Test"}
                """.formatted(orderId, order.get("total").decimalValue().toPlainString());
        String paymentJson = mvc.perform(post("/api/pagos").contentType(MediaType.APPLICATION_JSON).content(paymentPayload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long paymentId = objectMapper.readTree(paymentJson).get("id").asLong();

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/pagos/{id}/confirmar", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CONFIRMADO"));
        mvc.perform(get("/api/pedidos/dni/66778899"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("PAGO_CONFIRMADO"));

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/pedidos/{id}/estado", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"CANCELADO\",\"observacion\":\"Test\"}"))
                .andExpect(status().isOk());
        mvc.perform(get("/api/pedidos/dni/66778899"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}

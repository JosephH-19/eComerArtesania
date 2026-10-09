# Backend Spring Boot

Backend REST en Spring Boot 3.5, Java 17+ y Spring Data JPA. Usa H2 en archivo para conservar datos entre reinicios de desarrollo. El build copia el frontend existente desde `public/`, `admin/`, `css/` y `assets/` al classpath estático de Spring.

## Ejecutar

Desde la raíz del repositorio:

```bash
backend/mvnw -f backend/pom.xml spring-boot:run
```

Abrir `http://localhost:8080/`. La salud de la API está en `http://localhost:8080/api/health`; la consola H2 en `http://localhost:8080/h2-console` con JDBC URL `jdbc:h2:file:./data/taller-buendia`, usuario `sa` y contraseña vacía.

## Probar

```bash
backend/mvnw -f backend/pom.xml test
```

## Rutas principales

- `GET/POST /api/productos`, `GET/PUT/DELETE /api/productos/{id}`; filtros `categoriaId` y `q`.
- `GET/POST /api/categorias`, `GET/PUT/DELETE /api/categorias/{id}`.
- `GET/POST /api/artesanos`, `GET/PUT/DELETE /api/artesanos/{id}`.
- `GET /api/clientes?q=&estado=`, `GET /api/clientes/{id}`, `GET /api/clientes/dni/{dni}`, `POST /api/clientes`, `DELETE /api/clientes/{id}`. La baja de cliente es lógica para preservar pedidos.
- `GET/POST /api/descuentos`, `GET/PUT/DELETE /api/descuentos/{id}`.
- `POST /api/pedidos` crea pedido y detalles, verifica y descuenta stock; `GET /api/pedidos`, `GET /api/pedidos/{id}`, `GET /api/pedidos/dni/{dni}`, `PATCH /api/pedidos/{id}/estado`.
- `GET/POST /api/pagos`, `GET /api/pagos/{id}`, `PATCH /api/pagos/{id}/confirmar`.
- `GET /api/metricas` calcula indicadores desde pedidos, pagos y productos.

Estados de pedido: `PENDIENTE_PAGO`, `PAGO_CONFIRMADO`, `EN_PREPARACION`, `LISTO_PARA_ENTREGA`, `ENTREGADO`, `CANCELADO`. Los pagos son registros internos de demostración; no se conecta una pasarela real.

El frontend ya llama a la API al ejecutarse desde `localhost:8080`. Vercel sigue publicando una versión estática; para que el frontend desplegado use esta API se necesita configurar una URL pública para `window.API_BASE_URL` y desplegar el backend en un host que ejecute Java.

# Acuerdo de integración — Renzo y Elmer

Este documento es una **propuesta de contrato**. No confirma compatibilidad con código de Renzo que todavía no hemos visto.

## Responsabilidades

Renzo crea la compra, valida productos y cantidades, aplica los descuentos acordados, calcula el total en el servidor y reserva/descuenta stock. Elmer lee ese mismo pedido y registra estados, seguimiento, pagos e indicadores. El módulo de Elmer no incluye un endpoint para fabricar pedidos ni copia el carrito.

## Tablas compartidas propuestas

El esquema exacto, tipos y restricciones está en `src/main/resources/elmer-schema.sql`. Los nombres se eligieron para coincidir con la versión Java previa de la conversación, pero deberán adaptarse si el equipo ya acordó otros.

| Tabla | Escritor inicial | Campos relevantes |
|---|---|---|
| `pedido_t` | Renzo | `id`, `codigo`, `cliente_id`, `dni`, `nombre`, `correo`, `telefono`, `direccion`, `entrega`, `metodo`, `estado`, `total`, `descuento`, `cupon`, `creado`, `clave` |
| `detalle_t` | Renzo | `pedido_id`, `producto_id`, `nombre`, `cantidad`, `precio` |
| `pago_t` | Elmer | `pedido_id`, `monto`, `metodo`, `referencia`, `estado`, `creado` |
| `seguimiento_t` | Renzo crea primera entrada; Elmer las siguientes | `pedido_id`, `estado`, `nota`, `creado` |

- `id` se genera en H2 y se usa como vínculo con detalles y pagos.
- `codigo` debe ser único y difícil de adivinar, por ejemplo un código basado en UUID; se entrega al comprador junto con la constancia.
- `clave` es una clave única de idempotencia de la compra, para evitar crear dos pedidos ante reintentos. Renzo la genera antes de confirmar.
- `dni`: cadena de ocho dígitos; conserva ceros iniciales.
- `total`: total definitivo ya calculado con descuentos. `descuento`: importe monetario descontado, no porcentaje.
- `precio`: precio unitario histórico; `cantidad`: entero positivo. Cada variante debe referenciar su producto/SKU definitivo.
- `cliente_id` y `producto_id` se conservan como identificadores externos sin crear tablas de clientes o productos en esta entrega. Las claves foráneas hacia los módulos de compañeros se agregan cuando acuerden las tablas comunes.
- Estado inicial: `PENDIENTE`. Métodos de pago: `YAPE`, `PLIN`, `TRANSFERENCIA`, `EFECTIVO`.
- `creado` tiene valor automático con fecha/hora de base de datos. Ambas partes deben usar el mismo criterio horario.

Renzo debe guardar cabecera, detalles, primera entrada de seguimiento y su movimiento de stock **en una sola transacción**. No se aceptan importes enviados por el navegador como fuente de verdad.

## Estados y pagos

Flujo administrativo: `PENDIENTE → CONFIRMADO → PREPARACION → LISTO → EN_CAMINO → ENTREGADO`. Desde `LISTO` se admite `ENTREGADO` para recojo. Se puede cancelar antes del envío si no hay pagos confirmados o por revisar. No se permite entregar con saldo pendiente. Los pedidos gratuitos se consideran pagados sin crear pagos de importe cero.

El estado del pago se calcula aparte: pendiente, parcial o pagado, sumando solo pagos `CONFIRMADO`. Un aviso público se registra como `PENDIENTE` hasta que el administrador lo revise. Las referencias son únicas y los pagos pendientes también reservan saldo para evitar avisos repetidos que superen el total.

**Esta entrega no repone stock al cancelar**, porque no contiene el módulo de productos ni conoce cómo Renzo reserva unidades. Al juntar el código, deben incorporar la llamada al servicio de stock de Renzo dentro de `PedidoServiceImpl.cambiarEstado`, en la rama `CANCELADO`, usando la misma transacción. Hasta acordar esa conexión, el cambio de estado no debe interpretarse como una devolución de stock. Esta parte queda pendiente de la implementación de Renzo.

## Rutas que ya implementa Elmer

| Acción | Ruta |
|---|---|
| Listado administrativo | `GET /elmer/admin/pedidos` |
| Detalle administrativo | `GET /elmer/admin/pedido/{id}` |
| Cambio de estado | `POST /elmer/admin/pedido/{id}/estado` |
| Registrar pago recibido | `POST /elmer/admin/pedido/{id}/pago` |
| Listado de pagos | `GET /elmer/admin/pagos` |
| Confirmar/rechazar aviso | `POST /elmer/admin/pago/{id}/resolver` |
| Métricas por mes | `GET /elmer/admin/metricas?mes=AAAA-MM` |
| Formulario público | `GET /elmer/consulta` |
| Consulta con DNI y código | `POST /elmer/consulta` |

Los formularios incluyen el token `csrf` de la sesión. Tras la compra, Renzo puede enlazar al formulario `/elmer/consulta` y mostrar al comprador su código. La consulta autoriza en esa sesión la visualización del pedido y el aviso de pago. No se debe enlazar directamente a un ID y dar acceso sin verificación.

## Cómo unir los módulos

1. Comparar este esquema con las clases/tablas de Renzo antes de hacer merge.
2. Elegir un único proyecto Spring Boot y un único `pom.xml` común.
3. Incorporar el paquete `com.example.demo.elmer` debajo del paquete que escanea esa aplicación. No copiar `ElmerApplication` como segundo arranque si ya hay uno.
4. Copiar las vistas `WEB-INF/views/elmer` y el CSS `static/css/elmer.css`.
5. Integrar las tablas nuevas en el script común. No sobrescribir `application.properties`: conservar la conexión elegida por el equipo.
6. Sustituir `AccesoModulo` por la sesión administrativa común manteniendo autorización y protección de formularios.
7. Conectar la cancelación con la reversión del stock de Renzo.
8. Probar con una compra creada por Renzo: debe aparecer en pedidos; un pago debe modificar el saldo y los indicadores.

Crear ramas y hacer Push comparte archivos, pero no comparte las filas de H2 ni completa estos pasos de integración.

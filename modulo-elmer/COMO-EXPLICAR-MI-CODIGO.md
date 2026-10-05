# Cómo explicar mi módulo

«Mi parte es pedidos, pagos y métricas. Renzo crea el pedido y lo guarda en la base. Mi código lee ese pedido, permite registrar sus pagos, cambia su estado y calcula los indicadores.»

## 1. Qué hace cada capa

| Capa | Explicación sencilla |
|---|---|
| Controller | Recibe la acción del usuario y elige qué página mostrar. |
| Service | Valida las reglas y coordina la operación. |
| DAO / Repository | Ejecuta las consultas SQL con JdbcTemplate. |
| JSP | Muestra los datos y contiene los formularios. |

El controlador no crea un pedido ni calcula el carrito: eso corresponde a Renzo.

## 2. Ejemplo: consultar pedidos

En `PedidoController.listar`, los parámetros `q`, `estado` y `pago` vienen del formulario de filtros. El controlador llama a `PedidoService.listar`. El servicio consulta `pedido_t`, suma los pagos confirmados y clasifica cada pedido como pendiente, parcial o pagado. Finalmente, `model.addAttribute` envía la lista a `pedidos.jsp`.

`List` es una lista de resultados. `Map` permite leer una columna por su nombre, como `pedido.get("total")`; el profesor también lo utiliza en el ejemplo de estudiantes. Un `for` recorre cada resultado. No se utilizan streams.

## 3. Ejemplo: registrar un pago

`PagoController.registrar` recibe el identificador del pedido, importe, método y referencia. `PagoServiceImpl.registrarPago` consulta el pedido y valida:

1. Que no esté cancelado.
2. Que el importe sea positivo.
3. Que no supere el saldo disponible.
4. Que la referencia no se haya usado.

Después guarda el pago. Si lo informa el comprador, queda pendiente de revisión. Si lo registra el administrador como recibido, queda confirmado.

`BigDecimal` representa dinero. `compareTo` compara importes: devuelve un número negativo, cero o positivo según el primero sea menor, igual o mayor que el segundo.

## 4. Ejemplo: cambiar estado

`PedidoServiceImpl.cambiarEstado` usa condiciones `if` para verificar el cambio permitido. No permite pasar directamente de pendiente a entregado. Antes de entregar exige el pago completo. Guarda el nuevo estado y una fila de seguimiento con la nota.

`@Transactional` significa que los cambios de esa operación se guardan juntos. Si falla una validación, no debe quedar guardada solo una parte. `FOR UPDATE` evita que dos operaciones modifiquen a la vez el mismo pedido mientras se realiza la validación.

El módulo no toca productos ni stock. La reversión del stock al cancelar se conectará con el código de Renzo cuando esté disponible.

## 5. Métricas

`MetricaService.calcular` recibe un mes y consulta desde el primer día hasta el inicio del siguiente:

- `COUNT`: cuenta pedidos.
- `SUM`: suma importes, pagos o cantidades.
- `GROUP BY estado`: agrupa los pedidos por estado.
- `COALESCE(..., 0)`: devuelve cero si no hay resultados.

Un pedido por S/ 100 con un adelanto confirmado de S/ 30 aporta S/ 100 al importe de pedidos y S/ 30 a cobros. Son indicadores diferentes; no se suman entre sí.

## 6. Dependencia con Renzo

«Necesitamos acordar los nombres de tablas y campos y usar una misma base de datos. Subir los archivos a GitHub permite revisar el código, pero no conecta los módulos automáticamente. Mi entrega no inventa pedidos: estará vacía hasta integrar la compra de Renzo.»

El contrato propuesto está en `INTEGRACION-CON-RENZO.md`. Hay que ajustarlo si el equipo ya utiliza otros campos.

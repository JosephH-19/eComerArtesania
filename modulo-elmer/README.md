# Elmer: pedidos, pagos y métricas

Responsable: **Quispe Merino, Elmer Roly**.

Este directorio contiene únicamente el módulo de Elmer y lo mínimo para ejecutarlo como proyecto Java. Usa Spring Boot, JDBC, H2 y JSP, siguiendo las herramientas de la demo del profesor.

**No incluye datos de prueba, catálogo, carrito ni creación de compras.** Al iniciar crea tablas vacías. Los pedidos deben venir de la parte de Renzo. Compartir ambas ramas en GitHub no conecta automáticamente las bases de datos.

## Funciones

- Listar y filtrar pedidos por código, DNI/nombre, estado del pedido y estado de pago.
- Consultar detalle y seguimiento; actualizar el estado administrativo.
- Consulta pública con DNI y código del pedido. Se requiere el código adicional para no exponer compras solo con conocer un DNI.
- Registrar pagos recibidos o adelantos; recibir avisos del comprador y confirmarlos/rechazarlos administrativamente.
- Rechazar referencias duplicadas, sobrepagos y pagos no positivos.
- Mostrar métricas mensuales desde pedidos y pagos reales: importe de pedidos, cobros confirmados, unidades y estados.

## Archivos para estudiar

Dentro de `src/main/java/com/example/demo/elmer/`:

- `pedidos/PedidoController.java`: solicitudes de listado, detalle, consulta pública y cambio de estado.
- `pedidos/PedidoService.java` y `PedidoServiceImpl.java`: consulta de pedidos y reglas de seguimiento.
- `pagos/PagoController.java`: recepción y revisión de pagos.
- `pagos/PagoService.java` y `PagoServiceImpl.java`: importes, saldo, referencias y confirmación.
- `metricas/MetricaController.java` y `MetricaService.java`: indicadores por mes.
- `comun/SqlDAO.java` y `SqlRepository.java`: consultas parametrizadas con `JdbcTemplate`.
- `config/`: protección mínima del módulo y mensajes de error, sustituibles por la configuración común del equipo.
- `ElmerApplication.java`: permite iniciar esta entrega de forma independiente.

Pantallas: `src/main/webapp/WEB-INF/views/elmer/`.

El flujo es **Controller → Service → DAO/Repository → base de datos → JSP**. Se usan mapas de columnas como en el ejemplo de estudiantes del profesor. No hay código de los otros módulos.

## Ejecutar en IntelliJ

1. Abre este `pom.xml` como proyecto y usa JDK 17.
2. Ejecuta Maven → Lifecycle → install.
3. En la configuración de ejecución de `ElmerApplication`, agrega la variable de entorno `ELMER_CLAVE` con una contraseña elegida por ti. No la escribas en archivos que subas a GitHub. El usuario local es `elmer`.
4. Ejecuta `ElmerApplication`.
5. Consulta pública: http://localhost:8087/elmer/consulta
6. Administración: http://localhost:8087/elmer/admin/pedidos

El navegador solicita el usuario `elmer` y la contraseña configurada. Sin contraseña configurada, la administración permanece bloqueada. Este acceso básico es para ejecutar el módulo localmente y debe sustituirse por la autenticación común al integrar. No se crean usuarios ni claves por defecto.

El puerto 8087 permite abrirlo sin interferir con la demo del profesor que usa 8086. Las listas vacías y los indicadores en cero son correctos hasta que haya pedidos. No copies la base de datos de la tienda anterior: contiene datos de otra versión y puede tener compras de práctica.

## Base compartida y conexión con Renzo

Lee **INTEGRACION-CON-RENZO.md** antes de unir el código. El archivo `src/main/resources/elmer-schema.sql` es el contrato de tablas propuesto, sin INSERT ni datos precargados. Hay que compararlo con la estructura que entregue Renzo; aún no se ha recibido su código Java.

Por defecto esta entrega usa su propia base local `data/elmer.mv.db`. Eso sirve para iniciar el módulo vacío, no para recibir mágicamente pedidos de otra aplicación. En la integración ambos deben usar una sola aplicación y una sola conexión compartida; no ejecuten dos procesos contra el mismo archivo H2.

## Subir solamente tu parte

Coloca esta carpeta como `modulo-elmer` dentro del repositorio clonado `eComerArtesania`. Desde la terminal del repositorio:

```powershell
& "C:\Program Files\Git\cmd\git.exe" branch --show-current
& "C:\Program Files\Git\cmd\git.exe" add modulo-elmer
& "C:\Program Files\Git\cmd\git.exe" diff --cached --stat
& "C:\Program Files\Git\cmd\git.exe" commit -m "Agrega modulo de pedidos pagos y metricas de Elmer"
& "C:\Program Files\Git\cmd\git.exe" push
```

La primera línea debe mostrar `elmer/pedidos-pagos-metricas`. Revisa la lista antes de confirmar. No uses `git add .`: podría incluir archivos personales de IntelliJ. El `.gitignore` de este módulo excluye `target`, `data` y `.idea` dentro de su carpeta.

No se conecta a bancos, no realiza cobros y no emite comprobantes fiscales. Registra importes y referencias de operaciones. No incluye reembolsos ni archivos de comprobante.

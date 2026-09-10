# Informe de funcionalidades - Taller Buendía

## Interfaces del sistema

1. Inicio
2. Nosotros
3. Historia
4. Catálogo de productos
5. Administrar productos (CRUD)
6. Detalle de producto

## Matriz de funcionalidades

| Código | Funcionalidad | Interfaz asociada |
|---|---|---|
| F01 | Registrar un producto nuevo. | Administrar productos |
| F02 | Mostrar la lista de productos. | Catálogo / Administrar productos |
| F03 | Consultar el detalle de un producto. | Detalle de producto |
| F04 | Buscar un producto por nombre. | Catálogo |
| F05 | Filtrar productos por categoría. | Catálogo |
| F06 | Actualizar los datos de un producto. | Administrar productos |
| F07 | Eliminar un producto. | Administrar productos |
| F08 | Consultar la información del taller. | Nosotros |
| F09 | Actualizar la información del taller. | Nosotros |
| F10 | Consultar la historia del taller. | Historia |

## CRUD de productos

- **Create (Crear):** registrar un producto con nombre, precio y categoría.
- **Read (Leer):** mostrar la lista y el detalle de los productos.
- **Update (Actualizar):** modificar los datos de un producto existente.
- **Delete (Eliminar):** quitar un producto de la lista.

## Explicación para exponer

El usuario llena el formulario para registrar un producto. El sistema recibe el nombre, precio y categoría, valida que los campos estén completos y guarda la información en la tabla Producto. Luego, el producto aparece en el catálogo.

En esta primera maqueta se usan solamente HTML y CSS. Por eso los botones de Guardar, Editar y Eliminar solo representan la interfaz. Para que funcionen de verdad se necesitaría JavaScript para procesar los datos y una base de datos para guardarlos.

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Tipos de Productos</title>
</head>
<body>
    <h1>Seleccionar Tipo de Producto</h1>

    <!-- Selector para mostrar los tipos de productos -->
    <form action="/producto/reporte/productoTipoProducto" method="post">
        <label for="tipoProducto">Tipo de Producto:</label>
        <select id="tipoProducto" name="id">
            <c:forEach items="${tipoproductos}" var="tipoProducto">
                <option value="${tipoProducto.id}">${tipoProducto.nombre}</option>
            </c:forEach>
        </select>
        <button type="submit">Enviar</button>
    </form>

</body>
</html>
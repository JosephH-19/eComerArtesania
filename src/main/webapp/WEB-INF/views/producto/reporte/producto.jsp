<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reporte de Productos</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
    <h1>Reporte de Productos

    </h1>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>fechaCreacion</th>
                <th>tipo_producto</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${productos}" var="producto">
                <tr>
                    <td><c:out value="${producto.id}"/></td>
                    <td><c:out value="${producto.nombre}"/></td>
                    <td><c:out value="${producto.fechaCreacion}"/></td>
                    <td><c:out value="${producto.tipoProducto.nombre}"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty productos}">
                <tr>
                    <td colspan="5">No se encontraron productos para mostrar.</td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <p><a href="/producto/list">Volver a la selección</a></p>

</body>
</html>
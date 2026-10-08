<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Productos</title>
</head>
<body>

<h1>Productos registrados</h1>

<p>
    <a href="${pageContext.request.contextPath}/producto/crear">
        Registrar producto
    </a>
</p>

<table border="1" cellpadding="8">
    <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>Material</th>
            <th>Precio (S/)</th>
            <th>Stock</th>
            <th>Estado</th>
            <th>Tipo de producto</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach items="${productos}" var="producto">
            <tr>
                <td><c:out value="${producto.id}" /></td>
                <td><c:out value="${producto.nombre}" /></td>
                <td><c:out value="${producto.descripcion}" /></td>
                <td><c:out value="${producto.material}" /></td>
                <td><c:out value="${producto.precio}" /></td>
                <td><c:out value="${producto.stock}" /></td>
                <td><c:out value="${producto.estado}" /></td>
                <td><c:out value="${producto.tipoProducto.nombre}" /></td>
            </tr>
        </c:forEach>
    </tbody>
</table>

</body>
</html>
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

<form action="${pageContext.request.contextPath}/producto/listar"
      method="get">

    <label for="nombre">Buscar por nombre:</label>

    <input type="text"
           id="nombre"
           name="nombre"
           maxlength="200"
           value="<c:out value='${nombre}' />">

    <button type="submit">Buscar</button>

    <a href="${pageContext.request.contextPath}/producto/listar">
        Mostrar todos
    </a>
</form>

<br>
<c:if test="${empty productos}">
    <p>No se encontraron productos.</p>
</c:if>

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
            <!-- 1. SE AGREGÓ AQUÍ: Al final de los encabezados de la tabla -->
            <th>Acciones</th>
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
                <!-- 2. SE AGREGÓ AQUÍ: Después del tipo de producto y antes de cerrar la fila (</tr>) -->
                <td>
                    <a href="${pageContext.request.contextPath}/producto/detalle?id=${producto.id}">
                        Ver detalle
                    </a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

</body>
</html>

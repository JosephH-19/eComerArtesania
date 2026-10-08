<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Catálogo de artesanías</title>
</head>
<body>

<h1>Catálogo de artesanías textiles</h1>

<form action="${pageContext.request.contextPath}/producto/catalogo"
      method="get">

    <label for="nombre">Buscar:</label>

    <input type="text"
           id="nombre"
           name="nombre"
           maxlength="200"
           value="<c:out value='${nombre}' />">

    <label for="disponibilidad">Disponibilidad:</label>

    <select id="disponibilidad" name="disponibilidad">
        <option value="TODOS"
                ${disponibilidad == 'TODOS' ? 'selected' : ''}>
            Todos
        </option>

        <option value="CON_STOCK"
                ${disponibilidad == 'CON_STOCK' ? 'selected' : ''}>
            Con stock
        </option>

        <option value="SIN_STOCK"
                ${disponibilidad == 'SIN_STOCK' ? 'selected' : ''}>
            Sin stock
        </option>
    </select>

    <button type="submit">Buscar</button>

    <a href="${pageContext.request.contextPath}/producto/catalogo">
        Mostrar todos
    </a>
</form>

<c:if test="${empty productos}">
    <p>No se encontraron productos.</p>
</c:if>

<c:forEach items="${productos}" var="producto">
    <hr>

    <h2><c:out value="${producto.nombre}" /></h2>

    <p><c:out value="${producto.descripcion}" /></p>

    <p>
        Precio: S/ <c:out value="${producto.precio}" />
    </p>

    <c:choose>
        <c:when test="${producto.stock > 0}">
            <p>
                Disponible:
                <c:out value="${producto.stock}" /> unidades
            </p>
        </c:when>

        <c:otherwise>
            <p>Agotado</p>
        </c:otherwise>
    </c:choose>

    <a href="${pageContext.request.contextPath}/producto/detallePublico?id=${producto.id}">
        Ver detalle
    </a>
</c:forEach>

</body>
</html>
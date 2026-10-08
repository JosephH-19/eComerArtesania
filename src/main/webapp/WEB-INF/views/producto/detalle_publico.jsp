<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del producto</title>
</head>
<body>

<h1>Detalle del producto</h1>

<c:choose>
    <c:when test="${not empty error}">
        <p style="color: red;">
            <c:out value="${error}" />
        </p>
    </c:when>

    <c:otherwise>

        <p><strong>Nombre:</strong>
            <c:out value="${producto.nombre}" />
        </p>

        <p><strong>Descripción:</strong>
            <c:out value="${producto.descripcion}" />
        </p>

        <p><strong>Material:</strong>
            <c:out value="${producto.material}" />
        </p>

        <p><strong>Precio:</strong>
            S/ <c:out value="${producto.precio}" />
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

        <p><strong>Tipo de producto:</strong>
            <c:out value="${producto.tipoProducto.nombre}" />
        </p>

    </c:otherwise>
</c:choose>

<a href="${pageContext.request.contextPath}/producto/catalogo">
    Volver al catálogo
</a>

</body>
</html>

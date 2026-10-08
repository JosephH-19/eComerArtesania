<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Taller Buendía - Detalle del producto</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/catalogo.css">
</head>
<body>

<header>
    <strong>TALLER BUENDÍA</strong>
    <p>Taller Buendía - Textil Huanca</p>
</header>

<nav>
    <a href="${pageContext.request.contextPath}/producto/catalogo">
        CATÁLOGO
    </a>
</nav>

<main>

    <c:choose>
        <c:when test="${not empty error}">
            <h1>Detalle del producto</h1>

            <p class="error">
                <c:out value="${error}" />
            </p>
        </c:when>

        <c:otherwise>

            <div class="detalle-producto">

                <div class="detalle-foto">
                    <c:choose>
                        <c:when test="${not empty producto.imagen}">
                            <c:url value="${producto.imagen}"
                                   var="urlImagen" />

                            <img src="<c:out value='${urlImagen}' />"
                                 alt="<c:out value='${producto.nombre}' />">
                        </c:when>

                        <c:otherwise>
                            <p>Este producto todavía no tiene imagen.</p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <section class="detalle-informacion">

                    <h1>
                        <c:out value="${producto.nombre}" />
                    </h1>

                    <p class="precio">
                        S/ <c:out value="${producto.precio}" />
                    </p>

                    <h2>Descripción del producto</h2>

                    <p class="descripcion"><c:out value="${producto.descripcion}" /></p>

                    <p>
                        <strong>Material:</strong>
                        <c:out value="${producto.material}" />
                    </p>

                    <p>
                        <strong>Tipo de producto:</strong>
                        <c:out value="${producto.tipoProducto.nombre}" />
                    </p>

                    <c:choose>
                        <c:when test="${producto.stock > 0}">
                            <p class="disponible">
                                <strong>Disponible:</strong>
                                <c:out value="${producto.stock}" />
                                unidades
                            </p>
                        </c:when>

                        <c:otherwise>
                            <p class="agotado">Agotado</p>
                        </c:otherwise>
                    </c:choose>

                </section>

            </div>

        </c:otherwise>
    </c:choose>

    <p class="volver">
        <a class="boton"
           href="${pageContext.request.contextPath}/producto/catalogo">
            Volver al catálogo
        </a>
    </p>

</main>

<footer>
    <p>Taller Buendía - Textil Huanca</p>
</footer>

</body>
</html>
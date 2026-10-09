<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Taller Buendía - Detalle administrativo</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/catalogo.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/productos-admin.css">
</head>
<body>

<header>
    <strong>TALLER BUENDÍA</strong>
    <p>Zona de gestión de productos</p>
</header>

<nav>
    <a href="${pageContext.request.contextPath}/producto/listar">
        PRODUCTOS
    </a>
</nav>
<%@ include file="../comun/navegacion.jsp" %>

<main>

    <c:choose>
        <c:when test="${not empty error}">
            <h1>Detalle del producto</h1>

            <p class="error" role="alert">
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
                            <p>Sin imagen</p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <section class="detalle-informacion">

                    <h1>
                        <c:out value="${producto.nombre}" />
                    </h1>

                    <p>
                        <strong>ID:</strong>
                        <c:out value="${producto.id}" />
                    </p>

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
                        <strong>Stock:</strong>
                        <c:out value="${producto.stock}" />
                        unidades
                    </p>

                    <p>
                        <strong>Estado:</strong>
                        <c:out value="${producto.estado}" />
                    </p>

                    <p>
                        <strong>Categoría:</strong>
                        <c:out value="${producto.tipoProducto.nombre}" />
                    </p>
                    <p><strong>Artesano:</strong> <c:out value="${producto.artesano.nombreCompleto}" />
                    </p>

                    <p>
                        <strong>Fecha de creación:</strong>
                        <c:out value="${producto.fechaCreacion}" />
                    </p>

                    <div class="acciones-formulario">

                        <a class="boton"
                           href="${pageContext.request.contextPath}/producto/editar?id=${producto.id}">
                            Editar producto
                        </a>

                    </div>

                </section>

            </div>

        </c:otherwise>
    </c:choose>

    <p class="volver">
        <a class="boton"
           href="${pageContext.request.contextPath}/producto/listar">
            Volver al listado
        </a>
    </p>

</main>

<footer>
    <p>Taller Buendía - Zona de gestión</p>
</footer>

</body>
</html>
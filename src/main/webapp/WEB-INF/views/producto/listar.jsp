<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Taller Buendía - Gestión de productos</title>

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

<main>

    <div class="titulo-seccion">
        <div>
            <h1>Gestión de productos</h1>
            <p>Consulta y administra los productos registrados.</p>
        </div>

        <a class="boton"
           href="${pageContext.request.contextPath}/producto/crear">
            Registrar producto
        </a>
    </div>

    <section class="busqueda" aria-label="Búsqueda de productos">

        <form action="${pageContext.request.contextPath}/producto/listar"
              method="get">

            <label for="nombre">Buscar por nombre:</label>

            <input type="text"
                   id="nombre"
                   name="nombre"
                   maxlength="200"
                   value="<c:out value='${nombre}' />">

            <label for="disponibilidad">Stock:</label>

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

            <button type="submit" class="boton">
                Buscar
            </button>

            <a href="${pageContext.request.contextPath}/producto/listar">
                Mostrar todos
            </a>

        </form>
    </section>

    <h2>Productos registrados</h2>

    <c:if test="${empty productos}">
        <p>No se encontraron productos.</p>
    </c:if>

    <div class="productos-admin">

        <c:forEach items="${productos}" var="producto">

            <article class="producto-admin">

                <div class="foto-admin">
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

                <div class="informacion-admin">

                    <h2>
                        <c:out value="${producto.nombre}" />
                    </h2>

                    <p>
                        <strong>ID:</strong>
                        <c:out value="${producto.id}" />
                    </p>

                    <p>
                        <strong>Tipo de producto:</strong>
                        <c:out value="${producto.tipoProducto.nombre}" />
                    </p>

                    <p>
                        <strong>Material:</strong>
                        <c:out value="${producto.material}" />
                    </p>

                    <p class="precio">
                        S/ <c:out value="${producto.precio}" />
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

                </div>

                <div class="acciones-admin">

                    <a class="boton"
                       href="${pageContext.request.contextPath}/producto/detalle?id=${producto.id}">
                        Ver detalle
                    </a>

                    <a class="boton"
                       href="${pageContext.request.contextPath}/producto/editar?id=${producto.id}">
                        Editar
                    </a>

                    <form action="${pageContext.request.contextPath}/producto/cambiarEstado"
                          method="post">

                        <input type="hidden"
                               name="id"
                               value="${producto.id}">

                        <c:choose>
                            <c:when test="${producto.estado == 'ACTIVO'}">

                                <input type="hidden"
                                       name="estado"
                                       value="INACTIVO">

                                <button type="submit"
                                        class="boton boton-desactivar">
                                    Desactivar
                                </button>

                            </c:when>

                            <c:otherwise>

                                <input type="hidden"
                                       name="estado"
                                       value="ACTIVO">

                                <button type="submit"
                                        class="boton boton-activar">
                                    Activar
                                </button>

                            </c:otherwise>
                        </c:choose>

                    </form>

                </div>

            </article>

        </c:forEach>

    </div>

</main>

<footer>
    <p>Taller Buendía - Zona de gestión</p>
</footer>

</body>
</html>
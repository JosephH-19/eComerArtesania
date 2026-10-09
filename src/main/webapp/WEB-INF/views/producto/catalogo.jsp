<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Taller Buendía - Catálogo</title>

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
<%@ include file="../comun/navegacion.jsp" %>

<main>
    <h1>Catálogo de artesanías textiles</h1>

    <div class="catalogo">

        <aside class="filtros">
            <h2>Filtros</h2>

            <form action="${pageContext.request.contextPath}/producto/catalogo"
                  method="get">

                <label for="nombre">Buscar por nombre:</label>

                <input type="text"
                       id="nombre"
                       name="nombre"
                       maxlength="200"
                       value="<c:out value='${nombre}' />">

                <label for="categoria">Categoría:</label>
                <select id="categoria" name="categoria">
                    <option value="0">Todas</option>
                    <c:forEach items="${categorias}" var="tipo">
                        <option value="${tipo.id}" ${categoria == tipo.id ? 'selected' : ''}><c:out value="${tipo.nombre}" /></option>
                    </c:forEach>
                </select>
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

                <label for="precioMinimo">Precio mínimo (S/):</label>

                <input type="number"
                       id="precioMinimo"
                       name="precioMinimo"
                       min="0"
                       step="0.01"
                       value="<c:out value='${precioMinimo}' />">

                <label for="precioMaximo">Precio máximo (S/):</label>

                <input type="number"
                       id="precioMaximo"
                       name="precioMaximo"
                       min="0"
                       step="0.01"
                       value="<c:out value='${precioMaximo}' />">

                <button type="submit" class="boton">
                    Buscar
                </button>

                <a class="mostrar-todos"
                   href="${pageContext.request.contextPath}/producto/catalogo">
                    Mostrar todos
                </a>
            </form>
        </aside>

        <section class="resultados" aria-label="Resultados del catálogo">

            <c:if test="${not empty error}">
                <p class="error">
                    <c:out value="${error}" />
                </p>
            </c:if>

            <c:if test="${empty productos and empty error}">
                <p>No se encontraron productos.</p>
            </c:if>

            <div class="productos">

                <c:forEach items="${productos}" var="producto">

                    <article class="producto-card">

                        <div class="producto-imagen">
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

                        <div class="producto-contenido">

                            <h2>
                                <c:out value="${producto.nombre}" />
                            </h2>

                            <p class="precio">
                                S/ <c:out value="${producto.precioFinal}" />
                            </p>
                            <c:if test="${producto.porcentajeDescuento > 0}">
                                <p>Antes S/ ${producto.precio}. Descuento vigente: ${producto.porcentajeDescuento}%</p>
                            </c:if>

                            <c:choose>
                                <c:when test="${producto.stock > 0}">
                                    <p class="disponible">Disponible</p>
                                </c:when>

                                <c:otherwise>
                                    <p class="agotado">Agotado</p>
                                </c:otherwise>
                            </c:choose>

                            <a class="boton"
                               href="${pageContext.request.contextPath}/producto/detallePublico?id=${producto.id}">
                                Ver detalle
                            </a>

                        </div>
                    </article>

                </c:forEach>

            </div>
        </section>

    </div>
</main>

<footer>
    <p>Taller Buendía - Textil Huanca</p>
</footer>

</body>
</html>

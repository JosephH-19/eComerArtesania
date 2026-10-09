<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Taller Buendía - Registrar producto</title>

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

    <h1>Registrar producto</h1>

    <p>Completa los datos de la artesanía que deseas registrar.</p>

    <form:form
        method="post"
        action="${pageContext.request.contextPath}/producto/guardar"
        modelAttribute="producto"
        cssClass="formulario-producto">
<input type="hidden" name="tokenFormulario" value="${sessionScope.tokenFormulario}">

        <c:if test="${not empty error}">
            <p class="error" role="alert">
                <c:out value="${error}" />
            </p>
        </c:if>

        <p>
            <label for="nombre">Nombre:</label>

            <form:input
                id="nombre"
                path="nombre"
                maxlength="200"
                required="required" />
        </p>

        <p>
            <label for="descripcion">Descripción:</label>

            <form:textarea
                id="descripcion"
                path="descripcion"
                maxlength="1000"
                required="required" />
        </p>

        <p>
            <label for="material">Material:</label>

            <form:input
                id="material"
                path="material"
                maxlength="200"
                required="required" />
        </p>

        <p>
            <label for="precio">Precio en soles:</label>

            <form:input
                id="precio"
                path="precio"
                type="number"
                min="0.01"
                max="99999999.99"
                step="0.01"
                required="required" />
        </p>

        <p>
            <label for="stock">Stock:</label>

            <form:input
                id="stock"
                path="stock"
                type="number"
                min="0"
                step="1"
                required="required" />
        </p>

        <p>
            <label for="tipoProducto">Categoría:</label>

            <form:select
                id="tipoProducto"
                path="tipoProducto.id">

                <form:option value="0" label="Seleccionar" />

                <form:options
                    items="${tipoproductos}"
                    itemValue="id"
                    itemLabel="nombre" />

            </form:select>
        </p>

        <p>
            <label for="artesano">Artesano:</label>
            <form:select id="artesano" path="artesano.id">
                <form:option value="0" label="Seleccionar" />
                <form:options items="${artesanos}" itemValue="id" itemLabel="nombreCompleto" />
            </form:select>
        </p>

        <p>
            <label for="imagen">Ruta de imagen (opcional):</label>

            <form:input
                id="imagen"
                path="imagen"
                maxlength="500"
                placeholder="/imagenes/bufanda.jpg" />
        </p>

        <p class="ayuda">
            Puedes dejar la imagen vacía si todavía no tienes una fotografía.
        </p>

        <div class="acciones-formulario">

            <button type="submit" class="boton">
                Guardar producto
            </button>

            <a href="${pageContext.request.contextPath}/producto/listar">
                Volver al listado
            </a>

        </div>

    </form:form>

</main>

<footer>
    <p>Taller Buendía - Zona de gestión</p>
</footer>

</body>
</html>
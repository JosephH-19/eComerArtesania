<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar producto</title>
</head>
<body>

<h1>Editar producto</h1>

<p style="color: red;">
    <c:out value="${error}" />
</p>

<form:form
    method="post"
    action="${pageContext.request.contextPath}/producto/actualizar"
    modelAttribute="producto">
  <form:hidden path="id" />

    <p>
        <label>Nombre:</label><br>
        <form:input path="nombre" maxlength="200" required="required" />
    </p>

    <p>
        <label>Descripción:</label><br>
        <form:textarea
            path="descripcion"
            maxlength="1000"
            required="required" />
    </p>

    <p>
        <label>Material:</label><br>
        <form:input path="material" maxlength="200" required="required" />
    </p>

    <p>
        <label>Precio en soles:</label><br>
        <form:input
            path="precio"
            type="number"
            min="0.01"
            max="99999999.99"
            step="0.01"
            required="required" />
    </p>

    <p>
        <label>Stock:</label><br>
        <form:input
            path="stock"
            type="number"
            min="0"
            step="1"
            required="required" />
    </p>

    <p>
        <label>Tipo de producto:</label><br>
        <form:select path="tipoProducto.id">
            <form:option value="0" label="Seleccionar" />
            <form:options
                items="${tipoproductos}"
                itemValue="id"
                itemLabel="nombre" />
        </form:select>
    </p>

    <p>
        <label>Ruta de imagen (opcional):</label><br>
        <form:input path="imagen" maxlength="500" />
    </p>

    <button type="submit">Guardar producto</button>

</form:form>

<p>
    <a href="${pageContext.request.contextPath}/producto/listar">
        Volver al listado
    </a>
</p>

</body>
</html>
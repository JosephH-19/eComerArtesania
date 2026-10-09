<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Tipo de Producto</title>
</head>
<body>
    <h1>Crear Tipo de Producto</h1>
   
    <form action="/tipoProducto/registrar" method="post">
<input type="hidden" name="tokenFormulario" value="${sessionScope.tokenFormulario}">
        <label for="nombre">Nombre:</label>
        <input type="text" name="nombre" value="${tipoProducto.nombre}" required><br>
        <label for="Fecha de creacion">Fecha de creacion:</label>
        Fecha de Creación: <input type="date" name="fechaCreacion" value="${tipoProducto.fechaCreacion}" required><br><br>
        <!--  form:input path="fechaCreacion" id="fechaCreacion" type="date" required="true" /-->
        <br>
        <button type="submit">Crear</button>
    </form>
    <a href="/tipoProducto/list">Volver a la lista</a>
</body>
</html>
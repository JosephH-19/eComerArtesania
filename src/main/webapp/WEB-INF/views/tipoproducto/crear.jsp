<!-- filepath: d:\Roberto\utp\2025-I\Material\Desarrollo Web Integrado\Semana 03\demo_sem03\demo\demo\src\main\webapp\WEB-INF\views\tipoproducto\crear.jsp -->
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Tipo de Producto</title>
</head>
<body>
    <h1>Crear Tipo de Producto</h1>
    <!-- El formulario está vinculado al objeto "tipoProducto" -->
    <form:form action="/tipoproducto/crear" method="post" modelAttribute="tipoProducto">
        <label for="nombre">Nombre:</label>
        <form:input path="nombre" id="nombre" required="true" />
        <br>
        <label for="fechaCreacion">Fecha de Creación:</label>
        <form:input path="fechaCreacion" id="fechaCreacion" type="date" required="true" />
        <br>
        <button type="submit">Crear</button>
    </form:form>
    <a href="/tipoproducto/list">Volver a la lista</a>
</body>
</html>
<!-- filepath: d:\Roberto\utp\2025-I\Material\Desarrollo Web Integrado\Semana 03\demo_sem03\demo\demo\src\main\webapp\WEB-INF\views\tipoproducto\obtener.jsp -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    
<head>
    <meta charset="UTF-8">
    <title>Detalle del Tipo de Producto</title>
</head>
<body>
    <h1>Detalle del Tipo de Producto</h1>

    <!-- Verifica si el objeto tipoProducto está disponible -->
    <c:if test="${tipoProducto != null}">
        <p><strong>ID:</strong> ${tipoProducto.id}</p>
        <p><strong>Nombre:</strong> ${tipoProducto.nombre}</p>
        <p><strong>Fecha de Creación:</strong> ${tipoProducto.fechaCreacion}</p>
    </c:if>

    <!-- Mensaje si no se encuentra el producto -->
    <c:if test="${tipoProducto == null}">
        <p>No se encontró el tipo de producto.</p>
    </c:if>

    <a href="/tipoProducto/list">Volver a la lista</a>
</body>
</html>
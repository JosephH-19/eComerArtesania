<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Estudiantes</title>
</head>
<body>

<!-- Ejemplo 1: c:if para verificar si hay productos -->
<c:if test="${empty productos}">
  <p>No hay estudiantes registrados. </p>
</c:if>

<!-- Ejemplo 2: c:forEach para iterar una lista -->
<c:forEach items="${estudiantes}" var="estudiante" varStatus="status">
  <div>
    <h3>${status.count}. ${estudiante.nombre}</h3>
    <p>Apellido: <c:out value="${estudiante.apellido}" /> </p>

    <c:set var="resultado" value="${fn:containsIgnoreCase(estudiante.nombre, 'Roberto')}" />
    <c:if test="${resultado}">
      <p>¡Nombre coincide con 'Roberto'!</p>
    </c:if>
  </div>
</c:forEach>

<!-- Ejemplo 3: c:url para construir URLs con parámetros -->
<c:url value="/productos" var="urlPaginacion">
  <c:param name="pagina" value="${paginaActual + 1}" />
</c:url>
<a href="${urlPaginacion}">Siguiente página</a>

<!-- Verificar si la búsqueda contiene resultados -->


</body>
</html>
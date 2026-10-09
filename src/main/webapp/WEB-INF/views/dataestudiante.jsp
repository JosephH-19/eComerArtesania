<!DOCTYPE>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Lista de Estudiantes</title>
    </head>
    <body>

        <!-- Configurar la fuente de datos JNDI -->
        <sql:setDataSource 
            var="db" 
            driver="org.h2.Driver"
            url="jdbc:h2:mem:testdb"
            user="sa"
            password=""
        />

        <!-- Ejecutar consulta -->
        <sql:query dataSource="${db}" var="result">
            SELECT * FROM estudiante
        </sql:query>

        <!-- Mostrar resultados -->
        <c:forEach items="${result.rows}" var="fila">
            <p>${fila.nombre}, ${fila.apellido} -:- ${fila.fecha_nacimiento}</p>
        </c:forEach>

    </body>
</html>
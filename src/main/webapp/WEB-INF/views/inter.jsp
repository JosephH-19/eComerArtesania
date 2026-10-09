<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${pageContext.response.locale}" />
<fmt:setBundle basename="messages" />
<html>
<head>
    <meta charset="UTF-8">
    <title>Internacionalización</title>
</head>
<body>
    <h1><fmt:message key="saludo.bienvenida" /></h1>
    <p>Cambiar idioma:</p>
    <ul>
        <li><a href="?lang=es">Español</a></li>
        <li><a href="?lang=en">English</a></li>
    </ul>
</body>
</html>
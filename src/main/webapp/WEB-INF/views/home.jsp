<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <h1>${mensaje}</h1>
    <a href="/redirigir?nombre=Juan">Redirigir a /destino</a><br>
    <a href="/reusar">Simular error (forward)</a>
</body>
</html>
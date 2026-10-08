
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="/css/menu.css">
<nav>
    <ul class="menu">
        <li><a href="/home_inter" class="${activePage == 'home_inter' ? 'active' : ''}">Inicio</a></li>
        <li><a href="/about" class="${activePage == 'about' ? 'active' : ''}">Acerca de</a></li>
    </ul>
</nav>

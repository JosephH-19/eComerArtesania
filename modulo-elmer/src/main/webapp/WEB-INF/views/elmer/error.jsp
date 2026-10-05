<%@ page pageEncoding="UTF-8" %><%@ include file="cabecera.jspf" %>
<h1>No se completó la operación</h1><p class="error"><c:out value="${mensaje}"/></p><p>Revisa los datos y vuelve a intentarlo.</p><a href="/elmer/consulta">Consulta pública</a> · <a href="/elmer/admin/pedidos">Administración</a>
<%@ include file="pie.jspf" %>
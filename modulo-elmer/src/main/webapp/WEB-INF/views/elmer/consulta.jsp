<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecera.jspf" %>
<h1>Consulta tu pedido</h1>
<p>Ingresa tu DNI y el código que recibiste al confirmar la compra.</p>
<form method="post" class="estrecho">
<input type="hidden" name="csrf" value="${sessionScope.csrf}">
<label>DNI<input name="dni" pattern="[0-9]{8}" maxlength="8" required>
</label>
<label>Código de pedido<input name="codigo" placeholder="TB-…" maxlength="40" required>
</label>
<button>Consultar seguimiento</button>
</form>
<%@ include file="pie.jspf" %>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecera.jspf" %>
<h1>Métricas · Panel de Elmer</h1>
<form method="get" class="estrecho">
<label>Mes<input type="month" name="mes" value="${mes}" required>
</label>
<button>Consultar</button>
</form>
<div class="grid">
<article class="tarjeta">
<h2>Importe de pedidos</h2>
<p class="numero">S/ ${ventas.importe}</p>
<p>Pedidos creados en el mes, sin cancelados.</p>
</article>
<article class="tarjeta">
<h2>Cobros confirmados</h2>
<p class="numero">S/ ${cobros.importe}</p>
<p>Pagos registrados en el mes y confirmados; incluye adelantos.</p>
</article>
<article class="tarjeta">
<h2>Unidades pedidas</h2>
<p class="numero">${unidades}</p>
<p>Sin pedidos cancelados.</p>
</article>
<article class="tarjeta">
<h2>Pedidos activos del mes</h2>
<p class="numero">${ventas.pedidos}</p>
</article>
</div>
<h2>Estados de pedidos creados en el mes</h2>
<table>
<tr>
<th>Estado</th>
<th>Cantidad</th>
</tr>
<c:forEach items="${resumenEstados}" var="e">
<tr>
<td>${e.estado}</td>
<td>${e.cantidad}</td>
</tr>
</c:forEach>
</table>
<%@ include file="pie.jspf" %>
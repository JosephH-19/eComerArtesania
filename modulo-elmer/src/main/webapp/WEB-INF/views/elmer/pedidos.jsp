<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecera.jspf" %>
<h1>Pedidos · Panel de Elmer</h1>
<form method="get" class="filtros">
<label>Código, DNI o nombre<input name="q" value="<c:out value='${param.q}'/>">
</label>
<label>Estado<select name="estado">
<option value="">Todos</option>
<c:forEach items="${estados}" var="e">
<option ${param.estado == e ? 'selected' : ''}>${e}</option>
</c:forEach>
</select>
</label>
<label>Pago<select name="pago">
<option value="">Todos</option>
<c:forTokens items="PENDIENTE,PARCIAL,PAGADO" delims="," var="e">
<option ${param.pago == e ? 'selected' : ''}>${e}</option>
</c:forTokens>
</select>
</label>
<button>Filtrar</button>
</form>
<div class="tabla">
<table>
<tr>
<th>Pedido</th>
<th>Cliente</th>
<th>Fecha</th>
<th>Total</th>
<th>Estado</th>
<th>Pago</th>
<th>Acciones</th>
</tr>
<c:forEach items="${pedidos}" var="p">
<tr>
<td>${p.codigo}</td>
<td>
<c:out value="${p.nombre}"/>
</td>
<td>${p.creado}</td>
<td>S/ ${p.total}</td>
<td>${p.estado}</td>
<td>${p.pago}</td>
<td>
<a href="/elmer/admin/pedido/${p.id}">Detalle y seguimiento</a>
</td>
</tr>
</c:forEach>
</table>
</div>
<c:if test="${empty pedidos}">
<p>Todavía no hay pedidos recibidos desde el módulo de compra.</p>
</c:if>
<%@ include file="pie.jspf" %>
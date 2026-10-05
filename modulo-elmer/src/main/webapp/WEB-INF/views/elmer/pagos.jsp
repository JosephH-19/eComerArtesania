<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecera.jspf" %>
<h1>Pagos y adelantos</h1>
<p>Confirma únicamente pagos que hayas verificado. Para registrar efectivo, entra al detalle administrativo del pedido.</p>
<div class="tabla">
<table>
<tr>
<th>Pedido</th>
<th>Fecha</th>
<th>Importe</th>
<th>Método</th>
<th>Referencia</th>
<th>Estado</th>
<th>Revisión</th>
</tr>
<c:forEach items="${pagos}" var="p">
<tr>
<td>
<a href="/elmer/admin/pedido/${p.pedido_id}">${p.codigo}</a>
</td>
<td>${p.creado}</td>
<td>S/ ${p.monto}</td>
<td>${p.metodo}</td>
<td>
<c:out value="${p.referencia}"/>
</td>
<td>${p.estado}</td>
<td>
<c:if test="${p.estado == 'PENDIENTE'}">
<form method="post" action="/elmer/admin/pago/${p.id}/resolver">
<input type="hidden" name="csrf" value="${sessionScope.csrf}">
<button name="aprobar" value="true">Confirmar</button>
<button name="aprobar" value="false">Rechazar</button>
</form>
</c:if>
</td>
</tr>
</c:forEach>
</table>
</div>
<%@ include file="pie.jspf" %>
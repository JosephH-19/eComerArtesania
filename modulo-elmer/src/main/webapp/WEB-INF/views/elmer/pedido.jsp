<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecera.jspf" %>
<h1>Pedido <c:out value="${pedido.codigo}"/>
</h1>
<p>Guarda este código para consultar el seguimiento junto con tu DNI.</p>
<p>Cliente: <c:out value="${pedido.nombre}"/> · Estado: <strong>${pedido.estado}</strong>
</p>
<p>Entrega: ${pedido.entrega} · <c:out value="${pedido.direccion}"/>
</p>
<p>Registrado: ${pedido.creado}</p>
<div class="tabla">
<table>
<tr>
<th>Producto</th>
<th>Cantidad</th>
<th>Precio unitario</th>
</tr>
<c:forEach items="${detalles}" var="d">
<tr>
<td>
<c:out value="${d.nombre}"/>
</td>
<td>${d.cantidad}</td>
<td>S/ ${d.precio}</td>
</tr>
</c:forEach>
</table>
</div>
<p>Descuento aplicado: S/ ${pedido.descuento}</p>
<p class="precio">Total S/ ${pedido.total} · Cobrado S/ ${cobrado}</p>
<p>Saldo pendiente de confirmar: S/ ${pedido.total - cobrado}</p>
<p class="muted">Constancia académica del pedido; no es una boleta fiscal.</p>
<h2>Seguimiento</h2>
<ul>
<c:forEach items="${seguimiento}" var="s">
<li>${s.creado} — <strong>${s.estado}</strong>: <c:out value="${s.nota}"/>
</li>
</c:forEach>
</ul>
<h2>Pagos y adelantos</h2>
<table>
<tr>
<th>Método</th>
<th>Monto</th>
<th>Referencia</th>
<th>Estado</th>
</tr>
<c:forEach items="${pagos}" var="p">
<tr>
<td>${p.metodo}</td>
<td>S/ ${p.monto}</td>
<td>
<c:out value="${p.referencia}"/>
</td>
<td>${p.estado}</td>
</tr>
</c:forEach>
</table>
<c:if test="${pedido.estado != 'CANCELADO' && cobrado < pedido.total}">
<h2>${administracion ? 'Registrar pago recibido' : 'Informar pago o adelanto'}</h2>
<p>Registra el número de operación como referencia. No se carga una imagen de comprobante ni se realiza un cobro desde esta página.</p>
<form method="post" action="${administracion ? '/elmer/admin/pedido/' : '/elmer/pedido/'}${administracion ? pedido.id : pedido.id}/pago" class="estrecho">
<input type="hidden" name="csrf" value="${sessionScope.csrf}">
<label>Importe<input name="monto" type="number" min="0.01" max="${pedido.total-cobrado}" step="0.01" required>
</label>
<label>Método de pago<select name="metodo">
<option>YAPE</option>
<option>PLIN</option>
<option>TRANSFERENCIA</option>
<option>EFECTIVO</option>
</select>
</label>
<label>Referencia única o número de operación<input name="referencia" maxlength="100" required>
</label>
<button>${administracion ? 'Registrar pago confirmado' : 'Enviar para revisión'}</button>
</form>
</c:if>
<c:if test="${administracion}">
<h2>Actualizar seguimiento</h2>
<form method="post" action="/elmer/admin/pedido/${pedido.id}/estado" class="estrecho">
<input type="hidden" name="csrf" value="${sessionScope.csrf}">
<label>Nuevo estado<select name="estado">
<c:forEach items="${estados}" var="e">
<option>${e}</option>
</c:forEach>
</select>
</label>
<label>Nota<textarea name="nota" maxlength="500" required>
</textarea>
</label>
<button>Guardar seguimiento</button>
</form>
<p>Secuencia: pendiente → confirmado → preparación → listo → en camino → entregado. Desde listo se permite entrega directa. Solo se cancela antes del envío y sin pagos pendientes o confirmados.</p>
</c:if>
<%@ include file="pie.jspf" %>
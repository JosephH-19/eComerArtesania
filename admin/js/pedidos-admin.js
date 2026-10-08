(() => {
  const table = document.querySelector(".tabla-pedidos");
  if (!table) return;
  const API = window.API_BASE_URL || "/api";
  const body = table.querySelector("tbody");
  const search = document.querySelector(".busqueda input");
  const statusFilter = document.querySelector(".filtros-admin select");
  const notice = document.querySelector("#aviso-pedidos");
  const statuses = ["PENDIENTE_PAGO", "PAGO_CONFIRMADO", "EN_PREPARACION", "LISTO_PARA_ENTREGA", "ENTREGADO", "CANCELADO"];
  let orders = [];
  const request = async (url, options) => {
    const response = await fetch(`${API}${url}`, { headers: { "Content-Type": "application/json" }, ...options });
    const data = await response.json().catch(() => null);
    if (!response.ok) throw new Error(data?.error || `Error ${response.status}`);
    return data;
  };
  const cell = (row, value) => { const td = document.createElement("td"); td.textContent = value ?? ""; row.append(td); return td; };
  const label = (status) => status.replaceAll("_", " ");
  const load = async () => {
    try { orders = await request("/pedidos"); render(); }
    catch (error) { if (notice) notice.textContent = `No se pudieron cargar pedidos: ${error.message}`; }
  };
  const render = () => {
    const term = String(search?.value || "").trim().toLowerCase();
    const filter = String(statusFilter?.value || "todos").toUpperCase().replaceAll(" ", "_");
    const visible = orders.filter((order) => (!term || `${order.codigo} ${order.nombreCliente} ${order.dni}`.toLowerCase().includes(term)) && (filter === "TODOS" || order.estado === filter));
    body.replaceChildren();
    visible.forEach((order) => {
      const row = document.createElement("tr");
      cell(row, order.codigo); cell(row, order.nombreCliente); cell(row, new Date(order.fechaCreacion).toLocaleDateString("es-PE"));
      cell(row, `S/ ${Number(order.total).toFixed(2)}`);
      const stateCell = document.createElement("td");
      const stateSelect = document.createElement("select");
      statuses.forEach((status) => { const option = document.createElement("option"); option.value = status; option.textContent = label(status); stateSelect.append(option); });
      stateSelect.value = order.estado; stateCell.append(stateSelect); row.append(stateCell);
      const actions = document.createElement("td");
      const update = document.createElement("button"); update.type = "button"; update.className = "boton-accion editar"; update.textContent = "ACTUALIZAR";
      update.addEventListener("click", async () => {
        try { await request(`/pedidos/${order.id}/estado`, { method: "PATCH", body: JSON.stringify({ estado: stateSelect.value, observacion: "Actualizado desde el panel" }) }); show("Estado del pedido actualizado."); await load(); }
        catch (error) { show(error.message); }
      });
      const detail = document.createElement("a"); detail.href = `detalle-pedido.html?id=${order.id}`; detail.className = "boton-accion ver"; detail.textContent = "VER";
      actions.append(detail, update); row.append(actions); body.append(row);
    });
    document.querySelectorAll(".estadisticas-pedidos .numero").forEach((item, index) => {
      const values = [orders.length, orders.filter(order => order.estado === "PENDIENTE_PAGO").length, orders.filter(order => order.estado === "ENTREGADO").length,
        `S/ ${orders.reduce((sum, order) => sum + Number(order.total), 0).toFixed(2)}`];
      if (values[index] !== undefined) item.textContent = values[index];
    });
  };
  const show = (message) => { if (notice) notice.textContent = message; };
  search?.addEventListener("input", render); statusFilter?.addEventListener("change", render); load();
})();

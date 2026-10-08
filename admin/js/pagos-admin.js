(() => {
  const table = document.querySelector(".tabla-pagos");
  if (!table) return;
  const API = window.API_BASE_URL || "/api";
  const body = table.querySelector("tbody");
  const termInput = document.querySelector(".buscador input");
  const stateFilter = document.querySelector("#filtro-estado");
  const methodFilter = document.querySelector("#filtro-metodo");
  const notice = document.querySelector("#aviso-pagos");
  let payments = [];
  const request = async (path, options) => {
    const response = await fetch(`${API}${path}`, { headers: { "Content-Type": "application/json" }, ...options });
    const data = await response.json().catch(() => null);
    if (!response.ok) throw new Error(data?.error || `Error ${response.status}`);
    return data;
  };
  const load = async () => {
    try { payments = await request("/pagos"); render(); }
    catch (error) { if (notice) notice.textContent = `No se pudieron cargar pagos: ${error.message}`; }
  };
  const render = () => {
    const term = String(termInput?.value || "").trim().toLowerCase();
    const state = String(stateFilter?.value || "todos").toUpperCase();
    const method = String(methodFilter?.value || "todos").toUpperCase();
    const visible = payments.filter((payment) => (!term || `${payment.codigoPedido} ${payment.cliente} ${payment.referencia}`.toLowerCase().includes(term)) && (state === "TODOS" || payment.estado === state) && (method === "TODOS" || payment.metodo.toUpperCase() === method));
    body.replaceChildren();
    visible.forEach((payment) => {
      const row = document.createElement("tr");
      [payment.codigoPedido, payment.cliente, new Date(payment.fecha).toLocaleString("es-PE"), `S/ ${Number(payment.monto).toFixed(2)}`, payment.metodo].forEach((value) => {
        const td = document.createElement("td"); td.textContent = value; row.append(td);
      });
      const stateCell = document.createElement("td"); const badge = document.createElement("span");
      badge.className = `estado ${payment.estado === "CONFIRMADO" ? "confirmado" : "pendiente"}`; badge.textContent = payment.estado; stateCell.append(badge); row.append(stateCell);
      const dateCell = document.createElement("td"); dateCell.textContent = payment.estado === "CONFIRMADO" ? new Date(payment.fecha).toLocaleString("es-PE") : "-"; row.append(dateCell);
      const actionCell = document.createElement("td");
      if (payment.estado === "PENDIENTE") {
        const button = document.createElement("button"); button.type = "button"; button.className = "boton-accion confirmar"; button.textContent = "CONFIRMAR PAGO";
        button.addEventListener("click", async () => { try { await request(`/pagos/${payment.id}/confirmar`, { method: "PATCH" }); if (notice) notice.textContent = "Pago confirmado y pedido actualizado."; await load(); } catch (error) { if (notice) notice.textContent = error.message; } });
        actionCell.append(button);
      } else actionCell.textContent = payment.referencia || "Confirmado";
      row.append(actionCell); body.append(row);
    });
    const totals = [payments.filter(p => p.estado === "PENDIENTE").length, payments.filter(p => p.estado === "CONFIRMADO").length,
      `S/ ${payments.filter(p => p.estado === "CONFIRMADO").reduce((sum, p) => sum + Number(p.monto), 0).toFixed(2)}`, payments.filter(p => p.estado === "CANCELADO").length];
    document.querySelectorAll(".pagos-estadisticas .numero").forEach((element, index) => { if (totals[index] !== undefined) element.textContent = totals[index]; });
  };
  termInput?.addEventListener("input", render); stateFilter?.addEventListener("change", render); methodFilter?.addEventListener("change", render); load();
})();

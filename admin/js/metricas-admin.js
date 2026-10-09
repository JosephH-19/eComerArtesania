(() => {
  if (!document.querySelector(".serie-tiempo")) return;
  const API = window.API_BASE_URL || "/api";
  fetch(`${API}/metricas`).then((response) => {
    if (!response.ok) throw new Error("No se pudieron cargar las métricas");
    return response.json();
  }).then((stats) => {
    const money = (value) => `S/ ${Number(value || 0).toFixed(2)}`;
    const monthly = [money(stats.ventasMes), stats.pedidosMes, money(stats.ingresosMes), stats.productosVendidosMes, stats.pedidosCancelados];
    document.querySelectorAll(".serie-tiempo .metrica .numero").forEach((element, index) => {
      if (monthly[index] !== undefined) element.textContent = monthly[index];
    });
    const pointMetrics = document.querySelectorAll(".estado-puntual .metrica .numero");
    const values = [money(stats.totalPagado), stats.pedidosPendientesPago, stats.pedidosConfirmados, stats.productosActivos, money(stats.saldoPendiente)];
    pointMetrics.forEach((element, index) => { if (values[index] !== null && values[index] !== undefined) element.textContent = values[index]; });
    const tableRows = document.querySelectorAll(".tabla-metricas tbody tr");
    tableRows.forEach((row) => {
      const label = row.querySelector("td:first-child")?.textContent.trim().toUpperCase();
      if (label === "TOTAL") {
        const countCell = row.querySelector("td:nth-child(2)");
        const ratioCell = row.querySelector("td:nth-child(3)");
        if (countCell) countCell.textContent = stats.totalPedidos;
        if (ratioCell) ratioCell.textContent = "100%";
        return;
      }
      const state = ({
        "PENDIENTE DE PAGO": "PENDIENTE_PAGO",
        "CONFIRMADO": "PAGO_CONFIRMADO",
        "PAGO CONFIRMADO": "PAGO_CONFIRMADO",
        "EN PREPARACIÓN": "EN_PREPARACION",
        "EN PREPARACION": "EN_PREPARACION",
        "LISTO PARA ENTREGA": "LISTO_PARA_ENTREGA"
      })[label] || label?.replaceAll(" ", "_");
      const amount = stats.pedidosPorEstado[state] ?? 0;
      const countCell = row.querySelector("td:nth-child(2)");
      const ratioCell = row.querySelector("td:nth-child(3)");
      if (countCell) countCell.textContent = amount;
      if (ratioCell) ratioCell.textContent = stats.totalPedidos ? `${(amount * 100 / stats.totalPedidos).toFixed(1)}%` : "0%";
    });
  }).catch((error) => {
    const main = document.querySelector("main");
    const notice = document.createElement("p"); notice.className = "consulta-vacia"; notice.textContent = `${error.message}. Se muestran los datos de referencia.`;
    main.prepend(notice);
  });
})();

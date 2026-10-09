(() => {
  const API = window.API_BASE_URL || "/api";
  if (!location.pathname.includes("/admin/promociones/")) return;
  const request = async (path, options = {}) => {
    const response = await fetch(`${API}${path}`, { headers: { "Content-Type": "application/json", ...(options.headers || {}) }, ...options });
    const data = response.status === 204 ? null : await response.json().catch(() => null);
    if (!response.ok) throw new Error(data?.error || `Error ${response.status}`);
    return data;
  };
  const node = (tag, className, value) => { const item = document.createElement(tag); if (className) item.className = className; if (value != null) item.textContent = value; return item; };
  const showError = (target, error) => { let message = document.querySelector("#aviso-descuentos"); if (!message) { message = node("p", "consulta-vacia", ""); message.id = "aviso-descuentos"; target?.before(message); } message.textContent = error.message; };

  const listing = document.querySelector(".promociones-admin");
  if (listing) {
    const search = document.querySelector(".busqueda input");
    const stateFilter = document.querySelector(".filtros-admin select");
    let discounts = [];
    const load = async () => { discounts = await request("/descuentos"); render(); };
    const render = () => {
      const term = String(search?.value || "").toLowerCase().trim();
      const state = String(stateFilter?.value || "todos").toLowerCase();
      listing.replaceChildren();
      discounts.filter((discount) => (!term || discount.nombre.toLowerCase().includes(term)) &&
        (state === "todos" || (state === "activo" ? discount.activo : !discount.activo))).forEach((discount) => {
        const card = node("article", "promocion-admin", "");
        const detail = node("div", "", "");
        detail.append(node("h3", "", discount.nombre), node("p", "", `Descripción: ${discount.descripcion || "-"}`),
          node("p", "", `Descuento: ${discount.valor}${discount.tipo === "PORCENTAJE" ? "%" : " S/"}`),
          node("p", "", `Vigencia: ${discount.fechaInicio || "-"} a ${discount.fechaFin || "-"}`),
          node("p", "", `Estado: ${discount.activo ? "ACTIVO" : "INACTIVO"}`));
        const actions = node("div", "acciones", "");
        const view = node("a", "boton", "VER"); view.href = `detalle-promocion.html?id=${discount.id}`;
        const edit = node("a", "boton", "EDITAR"); edit.href = `editar.html?id=${discount.id}`;
        const toggle = node("button", "btn-danger", discount.activo ? "DESACTIVAR" : "ACTIVAR"); toggle.type = "button";
        toggle.addEventListener("click", async () => { try { await request(`/descuentos/${discount.id}`, { method: "PUT", body: JSON.stringify({ ...discount, activo: !discount.activo }) }); await load(); } catch (error) { showError(listing, error); } });
        actions.append(view, edit, toggle); card.append(detail, actions); listing.append(card);
      });
    };
    load().catch((error) => showError(listing, error));
    search?.addEventListener("input", render); stateFilter?.addEventListener("change", render);
  }

  const id = new URLSearchParams(location.search).get("id");
  const detailPanel = document.querySelector(".detalle-admin");
  if (detailPanel && id) {
    request(`/descuentos/${id}`).then((discount) => {
      const targets = detailPanel.querySelectorAll(".detalle-informacion p");
      const replacements = [discount.id, discount.nombre, discount.descripcion, discount.tipo, discount.valor,
        discount.aplicableA, discount.fechaInicio, discount.fechaFin, discount.activo ? "ACTIVO" : "INACTIVO"];
      targets.forEach((p, index) => { const label = p.querySelector("b"); if (label && replacements[index] !== undefined) { p.replaceChildren(label, document.createTextNode(` ${replacements[index]}`)); } });
      const edit = detailPanel.querySelector("a[href='editar.html']"); if (edit) edit.href = `editar.html?id=${discount.id}`;
    }).catch((error) => showError(detailPanel, error));
  }

  const form = document.querySelector("form.formulario-admin");
  if (!form) return;
  const fields = Object.fromEntries(["nombre", "descripcion", "tipo-descuento", "valor", "fecha-inicio", "fecha-fin", "aplicable-a", "estado"]
    .map((name) => [name, form.elements.namedItem(name)]));
  const populate = (discount) => {
    if (fields.nombre) fields.nombre.value = discount.nombre || "";
    if (fields.descripcion) fields.descripcion.value = discount.descripcion || "";
    if (fields["tipo-descuento"]) fields["tipo-descuento"].value = String(discount.tipo || "PORCENTAJE").toLowerCase();
    if (fields.valor) fields.valor.value = discount.valor || "";
    if (fields["fecha-inicio"]) fields["fecha-inicio"].value = discount.fechaInicio || "";
    if (fields["fecha-fin"]) fields["fecha-fin"].value = discount.fechaFin || "";
    if (fields["aplicable-a"]) fields["aplicable-a"].value = discount.aplicableA || "todos";
    if (fields.estado) fields.estado.value = discount.activo ? "activo" : "inactivo";
  };
  if (id && location.pathname.includes("editar.html")) request(`/descuentos/${id}`).then(populate).catch((error) => showError(form, error));
  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const data = new FormData(form);
    const payload = { nombre: data.get("nombre"), descripcion: data.get("descripcion"), tipo: String(data.get("tipo-descuento") || "PORCENTAJE").toUpperCase(),
      valor: Number(data.get("valor")), fechaInicio: data.get("fecha-inicio") || null, fechaFin: data.get("fecha-fin") || null,
      aplicableA: data.get("aplicable-a") || "todos", objetivoId: null, activo: data.get("estado") !== "inactivo" };
    try {
      await request(id && location.pathname.includes("editar.html") ? `/descuentos/${id}` : "/descuentos", {
        method: id && location.pathname.includes("editar.html") ? "PUT" : "POST", body: JSON.stringify(payload)
      });
      location.href = "promociones.html";
    } catch (error) { showError(form, error); }
  });
})();

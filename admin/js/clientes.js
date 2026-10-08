(() => {
  const api = window.API_BASE_URL || "/api";
  const request = async (path, options = {}) => {
    const response = await fetch(`${api}${path}`, {
      headers: { "Content-Type": "application/json", ...(options.headers || {}) },
      ...options
    });
    if (!response.ok) {
      const body = await response.json().catch(() => ({}));
      throw new Error(body.error || `Solicitud fallida (${response.status})`);
    }
    return response.status === 204 ? null : response.json();
  };
  const show = (selector, message) => {
    const element = document.querySelector(selector);
    if (element) element.textContent = message;
  };
  const labelState = (value) => String(value || "activo").toUpperCase();

  const table = document.querySelector(".tabla-clientes");
  if (table) {
    const body = table.querySelector("tbody");
    const search = document.querySelector(".buscador input");
    const filter = document.querySelector(".filtros-admin select");
    const count = document.querySelector(".paginacion span");
    let clients = [];

    const render = () => {
      const term = String(search?.value || "").trim().toLocaleLowerCase("es");
      const status = String(filter?.value || "todos").toLowerCase();
      const visible = clients.filter((client) => {
        const text = [client.id, client.nombre, client.dni, client.correo, client.telefono].join(" ").toLocaleLowerCase("es");
        return (!term || text.includes(term)) && (status === "todos" || client.estado === status);
      });
      body.replaceChildren();
      visible.forEach((client) => {
        const row = document.createElement("tr");
        [client.id, client.nombre, client.dni, client.correo, client.telefono, "0"].forEach((value) => {
          const cell = document.createElement("td");
          cell.textContent = value ?? "";
          row.append(cell);
        });
        const stateCell = document.createElement("td");
        const badge = document.createElement("span");
        badge.className = `estado ${client.estado === "activo" ? "activo" : "inactivo"}`;
        badge.textContent = labelState(client.estado);
        stateCell.append(badge);
        row.append(stateCell);
        const actions = document.createElement("td");
        const view = document.createElement("a");
        view.className = "boton-accion ver";
        view.href = `detalle-cliente.html?id=${client.id}`;
        view.textContent = "VER";
        const remove = document.createElement("button");
        remove.type = "button";
        remove.className = "boton-accion eliminar-cliente";
        remove.dataset.id = client.id;
        remove.textContent = "ELIMINAR";
        actions.append(view, remove);
        row.append(actions);
        body.append(row);
      });
      if (count) count.textContent = `Mostrando ${visible.length} de ${clients.length} clientes`;
    };

    const load = async () => {
      try {
        clients = await request("/clientes");
        render();
      } catch (error) {
        show("#aviso-clientes", `No se pudo cargar clientes: ${error.message}`);
      }
    };

    search?.addEventListener("input", render);
    document.querySelector(".buscador button")?.addEventListener("click", render);
    filter?.addEventListener("change", render);
    body.addEventListener("click", async (event) => {
      const button = event.target.closest(".eliminar-cliente");
      if (!button) return;
      const client = clients.find((entry) => String(entry.id) === button.dataset.id);
      if (!client || !window.confirm(`¿Eliminar al cliente ${client.nombre}?`)) return;
      try {
        await request(`/clientes/${client.id}`, { method: "DELETE" });
        show("#aviso-clientes", "Cliente eliminado.");
        await load();
      } catch (error) {
        show("#aviso-clientes", `No se pudo eliminar: ${error.message}`);
      }
    });
    load();
    const query = new URLSearchParams(location.search);
    if (query.has("registrado")) show("#aviso-clientes", "Cliente registrado.");
    if (query.has("eliminado")) show("#aviso-clientes", "Cliente eliminado.");
  }

  const form = document.querySelector("#form-cliente");
  if (form) {
    form.addEventListener("submit", async (event) => {
      event.preventDefault();
      const data = new FormData(form);
      const payload = {
        nombre: data.get("nombre"),
        dni: data.get("dni"),
        correo: data.get("correo"),
        telefono: data.get("telefono"),
        direccion: data.get("direccion"),
        distrito: data.get("distrito"),
        fechaNacimiento: data.get("fecha-nacimiento") || null,
        genero: data.get("genero"),
        estado: data.get("estado")
      };
      try {
        await request("/clientes", { method: "POST", body: JSON.stringify(payload) });
        location.href = "clientes.html?registrado=1";
      } catch (error) {
        show("#aviso-registro-cliente", `No se pudo registrar: ${error.message}`);
      }
    });
  }

  const detail = document.querySelector(".detalle-container");
  if (detail) {
    const id = new URLSearchParams(location.search).get("id");
    const loadDetail = async () => {
      try {
        const client = await request(`/clientes/${encodeURIComponent(id)}`);
        const values = {
          "detalle-id": client.id,
          "detalle-nombre": client.nombre,
          "detalle-dni": client.dni,
          "detalle-fecha-nacimiento": client.fechaNacimiento || "No registrada",
          "detalle-genero": client.genero || "No especificado",
          "detalle-correo": client.correo,
          "detalle-telefono": client.telefono,
          "detalle-direccion": client.direccion || "No registrada",
          "detalle-distrito": client.distrito || "No especificado",
          "detalle-estado": labelState(client.estado)
        };
        Object.entries(values).forEach(([field, value]) => {
          const node = document.getElementById(field);
          if (node) node.textContent = value ?? "";
        });
        const remove = document.querySelector("#eliminar-cliente");
        remove?.addEventListener("click", async () => {
          if (!window.confirm(`¿Eliminar al cliente ${client.nombre}?`)) return;
          try {
            await request(`/clientes/${client.id}`, { method: "DELETE" });
            location.href = "clientes.html?eliminado=1";
          } catch (error) {
            show("#aviso-clientes", `No se pudo eliminar: ${error.message}`);
          }
        });
      } catch (error) {
        detail.replaceChildren(Object.assign(document.createElement("p"), { textContent: error.message }));
      }
    };
    loadDetail();
  }
})();

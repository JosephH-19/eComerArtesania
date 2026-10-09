(() => {
  const storageKey = "tallerBuendiaClientes";
  const seed = [
    { id: "CLI-001", name: "Juan Pérez García", dni: "12345678", email: "juan.perez@example.com", phone: "987654321", orders: 3, status: "activo", birthDate: "1985-05-15", gender: "Masculino", address: "Calle Real 123, Huancayo, Junín", district: "Huancayo" },
    { id: "CLI-002", name: "María López Martínez", dni: "87654321", email: "maria.lopez@example.com", phone: "912345678", orders: 2, status: "activo" },
    { id: "CLI-003", name: "Carlos Gómez Sánchez", dni: "11223344", email: "carlos.gomez@example.com", phone: "922334455", orders: 1, status: "inactivo" }
  ];

  const readClients = () => {
    try {
      const saved = JSON.parse(localStorage.getItem(storageKey) || "null");
      return Array.isArray(saved) ? saved : seed;
    } catch {
      return seed;
    }
  };
  const saveClients = (clients) => localStorage.setItem(storageKey, JSON.stringify(clients));
  const fullName = (client) => client.name || "Sin nombre";
  const cell = (row, text) => {
    const element = document.createElement("td");
    element.textContent = text || "";
    row.append(element);
    return element;
  };
  const statusLabel = (status) => String(status || "activo").toUpperCase();

  const list = document.querySelector(".tabla-clientes");
  if (list) {
    if (localStorage.getItem(storageKey) === null) saveClients(seed);
    const body = list.querySelector("tbody");
    const search = document.querySelector(".buscador input");
    const filter = document.querySelector(".filtros-admin select");
    const notice = document.querySelector("#aviso-clientes");

    const render = () => {
      const clients = readClients();
      const term = String(search?.value || "").trim().toLocaleLowerCase("es");
      const selectedStatus = String(filter?.value || "todos").toLowerCase();
      const visible = clients.filter((client) => {
        const values = [client.id, client.name, client.dni, client.email, client.phone].join(" ").toLocaleLowerCase("es");
        return (!term || values.includes(term)) && (selectedStatus === "todos" || client.status === selectedStatus);
      });
      body.replaceChildren();
      visible.forEach((client) => {
        const row = document.createElement("tr");
        cell(row, client.id);
        cell(row, fullName(client));
        cell(row, client.dni);
        cell(row, client.email);
        cell(row, client.phone);
        cell(row, String(client.orders || 0));
        const stateCell = document.createElement("td");
        const badge = document.createElement("span");
        badge.className = `estado ${client.status === "activo" ? "activo" : "inactivo"}`;
        badge.textContent = statusLabel(client.status);
        stateCell.append(badge);
        row.append(stateCell);
        const actions = document.createElement("td");
        const view = document.createElement("a");
        view.href = `detalle-cliente.html?id=${encodeURIComponent(client.id)}`;
        view.className = "boton-accion ver";
        view.textContent = "VER";
        const remove = document.createElement("button");
        remove.type = "button";
        remove.className = "boton-accion eliminar-cliente";
        remove.dataset.clienteId = client.id;
        remove.textContent = "ELIMINAR";
        actions.append(view, remove);
        row.append(actions);
        body.append(row);
      });
      const count = document.querySelector(".paginacion span");
      if (count) count.textContent = `Mostrando ${visible.length} de ${clients.length} clientes`;
    };

    const removeClient = (id) => {
      const clients = readClients();
      const target = clients.find((client) => client.id === id);
      if (!target || !window.confirm(`¿Eliminar al cliente ${fullName(target)}?`)) return false;
      saveClients(clients.filter((client) => client.id !== id));
      return true;
    };

    search?.addEventListener("input", render);
    document.querySelector(".buscador button")?.addEventListener("click", render);
    filter?.addEventListener("change", render);
    body.addEventListener("click", (event) => {
      const button = event.target.closest(".eliminar-cliente");
      if (!button || !removeClient(button.dataset.clienteId)) return;
      render();
      if (notice) notice.textContent = "Cliente eliminado.";
    });
    render();
    const query = new URLSearchParams(location.search);
    if (notice && query.has("registrado")) notice.textContent = "Cliente registrado.";
    if (notice && query.has("eliminado")) notice.textContent = "Cliente eliminado.";
  }

  const registration = document.querySelector("#form-cliente");
  if (registration) {
    registration.addEventListener("submit", (event) => {
      event.preventDefault();
      const data = new FormData(registration);
      const clients = readClients();
      const dni = String(data.get("dni") || "").trim();
      const notice = document.querySelector("#aviso-registro-cliente");
      if (clients.some((client) => client.dni === dni)) {
        if (notice) notice.textContent = "Ya existe un cliente con ese DNI.";
        return;
      }
      const nextId = clients.reduce((max, client) => Math.max(max, Number(String(client.id).replace(/\D/g, "")) || 0), 0) + 1;
      clients.push({
        id: `CLI-${String(nextId).padStart(3, "0")}`,
        name: String(data.get("nombre") || "").trim(),
        dni,
        email: String(data.get("correo") || "").trim(),
        phone: String(data.get("telefono") || "").trim(),
        orders: 0,
        status: String(data.get("estado") || "activo"),
        birthDate: String(data.get("fecha-nacimiento") || ""),
        gender: String(data.get("genero") || ""),
        address: String(data.get("direccion") || "").trim(),
        district: String(data.get("distrito") || "")
      });
      saveClients(clients);
      location.href = "clientes.html?registrado=1";
    });
  }

  const detail = document.querySelector(".detalle-container");
  if (detail) {
    const id = new URLSearchParams(location.search).get("id") || "CLI-001";
    const client = readClients().find((entry) => entry.id === id);
    if (!client) {
      const message = document.createElement("p");
      message.className = "consulta-vacia";
      message.textContent = "No se encontró el cliente solicitado.";
      detail.replaceChildren(message);
      return;
    }
    const fields = {
      "detalle-id": client.id,
      "detalle-nombre": fullName(client),
      "detalle-dni": client.dni,
      "detalle-fecha-nacimiento": client.birthDate || "No registrada",
      "detalle-genero": client.gender || "No especificado",
      "detalle-correo": client.email,
      "detalle-telefono": client.phone,
      "detalle-direccion": client.address || "No registrada",
      "detalle-distrito": client.district || "No especificado",
      "detalle-estado": statusLabel(client.status)
    };
    Object.entries(fields).forEach(([id, value]) => {
      const element = document.getElementById(id);
      if (element) element.textContent = value;
    });
    const state = document.querySelector("#detalle-estado");
    if (state) state.className = `estado ${client.status === "activo" ? "activo" : "inactivo"}`;
    document.querySelector("#eliminar-cliente")?.addEventListener("click", () => {
      if (!window.confirm(`¿Eliminar al cliente ${fullName(client)}?`)) return;
      saveClients(readClients().filter((entry) => entry.id !== id));
      location.href = "clientes.html?eliminado=1";
    });
  }
})();

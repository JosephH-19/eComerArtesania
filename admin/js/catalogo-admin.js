(() => {
  const API = window.API_BASE_URL || "/api";
  const path = location.pathname;
  const moduleName = path.includes("/productos/") ? "productos" : path.includes("/categorias/") ? "categorias" : path.includes("/artesanos/") ? "artesanos" : null;
  if (!moduleName) return;

  const request = async (url, options = {}) => {
    const response = await fetch(`${API}${url}`, { headers: { "Content-Type": "application/json", ...(options.headers || {}) }, ...options });
    const data = response.status === 204 ? null : await response.json().catch(() => null);
    if (!response.ok) throw new Error(data?.error || `Error ${response.status}`);
    return data;
  };
  const node = (tag, className, value) => {
    const element = document.createElement(tag);
    if (className) element.className = className;
    if (value !== undefined && value !== null) element.textContent = value;
    return element;
  };
  const notice = (form) => {
    let message = document.querySelector("#aviso-api-admin");
    if (!message) { message = node("p", "consulta-vacia", ""); message.id = "aviso-api-admin"; form?.before(message); }
    return message;
  };
  const field = (form, name) => form.elements.namedItem(name);
  const setOptions = (select, records, getText, selected) => {
    if (!select) return;
    select.replaceChildren(new Option("Seleccionar...", ""));
    records.forEach((record) => select.add(new Option(getText(record), record.id)));
    if (selected != null) select.value = String(selected);
  };

  const categoryList = document.querySelector(".categorias-admin");
  const artisanList = document.querySelector(".artesanos-admin");
  const productList = document.querySelector(".productos-admin");

  if (categoryList) {
    const render = async () => {
      const categories = await request("/categorias");
      const term = document.querySelector(".busqueda input")?.value.trim().toLowerCase() || "";
      categoryList.replaceChildren();
      categories.filter((item) => item.nombre.toLowerCase().includes(term)).forEach((category) => {
        const card = node("article", "categoria-admin");
        card.append(node("h3", "", category.nombre), node("p", "", `Descripción: ${category.descripcion || "-"}`),
          node("p", "", `Estado: ${category.activa ? "Activo" : "Inactivo"}`));
        const actions = node("div", "acciones", "");
        const edit = node("a", "boton", "EDITAR"); edit.href = `editar.html?id=${category.id}`;
        const toggle = node("button", "btn-danger", category.activa ? "DESACTIVAR" : "ACTIVAR"); toggle.type = "button";
        toggle.addEventListener("click", async () => { await request(`/categorias/${category.id}`, { method: "PUT", body: JSON.stringify({ nombre: category.nombre, descripcion: category.descripcion, activa: !category.activa }) }); await render(); });
        actions.append(edit, toggle); card.append(actions); categoryList.append(card);
      });
    };
    render().catch((error) => { categoryList.prepend(node("p", "consulta-vacia", error.message)); });
    document.querySelector(".busqueda input")?.addEventListener("input", () => render().catch(() => {}));
  }

  if (artisanList) {
    const render = async () => {
      const artisans = await request("/artesanos");
      const term = document.querySelector(".busqueda input")?.value.trim().toLowerCase() || "";
      artisanList.replaceChildren();
      artisans.filter((item) => item.nombre.toLowerCase().includes(term)).forEach((artisan, index) => {
        const card = node("article", "artesano-admin");
        const image = node("img", "", ""); image.src = index % 2 ? "../../assets/artesanos/ArtesanosHistoria2.jpg" : "../../assets/artesanos/ArtesanoHistoria.jpeg"; image.alt = artisan.nombre;
        const info = node("div", "", "");
        info.append(node("h3", "", artisan.nombre), node("p", "", `Especialidad: ${artisan.especialidad || "-"}`),
          node("p", "", `Experiencia: ${artisan.aniosExperiencia || 0} años`), node("p", "", `Estado: ${artisan.activo ? "Activo" : "Inactivo"}`));
        const actions = node("div", "acciones", "");
        const edit = node("a", "boton", "EDITAR"); edit.href = `editar.html?id=${artisan.id}`;
        const toggle = node("button", "btn-danger", artisan.activo ? "DESACTIVAR" : "ACTIVAR"); toggle.type = "button";
        toggle.addEventListener("click", async () => { await request(`/artesanos/${artisan.id}`, { method: "PUT", body: JSON.stringify({ nombre: artisan.nombre, especialidad: artisan.especialidad, aniosExperiencia: artisan.aniosExperiencia, correo: artisan.correo, telefono: artisan.telefono, activo: !artisan.activo }) }); await render(); });
        actions.append(edit, toggle); card.append(image, info, actions); artisanList.append(card);
      });
    };
    render().catch((error) => { artisanList.prepend(node("p", "consulta-vacia", error.message)); });
    document.querySelector(".busqueda input")?.addEventListener("input", () => render().catch(() => {}));
  }

  if (productList) {
    const render = async () => {
      const products = await request("/productos");
      const term = document.querySelector(".busqueda input")?.value.trim().toLowerCase() || "";
      productList.replaceChildren();
      products.filter((item) => item.nombre.toLowerCase().includes(term)).forEach((product) => {
        const card = node("article", "producto-admin");
        const image = node("img", "", ""); image.src = product.imagen || "../../assets/productos/1.jpg"; image.alt = product.nombre;
        const info = node("div", "", "");
        info.append(node("h3", "", product.nombre), node("p", "", `Categoría: ${product.categoria?.nombre || "-"}`),
          node("p", "", `Artesano: ${product.artesano?.nombre || "-"}`), node("p", "", `Precio: S/ ${Number(product.precio).toFixed(2)}`),
          node("p", "", `Stock: ${product.stock}`), node("p", "", `Estado: ${product.estado}`));
        const actions = node("div", "acciones", "");
        const view = node("a", "boton", "VER"); view.href = `detalle-producto.html?id=${product.id}`;
        const edit = node("a", "boton", "EDITAR"); edit.href = `editar.html?id=${product.id}`;
        const remove = node("button", "btn-danger", "ELIMINAR"); remove.type = "button";
        remove.addEventListener("click", async () => { if (!window.confirm(`¿Eliminar ${product.nombre}?`)) return; await request(`/productos/${product.id}`, { method: "DELETE" }); await render(); });
        actions.append(view, edit, remove); card.append(image, info, actions); productList.append(card);
      });
    };
    render().catch((error) => { productList.prepend(node("p", "consulta-vacia", error.message)); });
    document.querySelector(".busqueda input")?.addEventListener("input", () => render().catch(() => {}));
  }

  const form = document.querySelector("form.formulario-admin");
  if (!form) return;
  const editId = new URLSearchParams(location.search).get("id");
  const isEdit = Boolean(editId);
  const state = field(form, "estado");
  const setStateActive = (select) => { if (select) select.value = select.querySelector("option[value='activo']") ? "activo" : "disponible"; };

  const setupProductForm = async () => {
    const [categories, artisans] = await Promise.all([request("/categorias"), request("/artesanos")]);
    setOptions(field(form, "categoria"), categories, (entry) => entry.nombre, isEdit ? undefined : undefined);
    setOptions(field(form, "artesano"), artisans, (entry) => entry.nombre);
    let existing;
    if (isEdit) {
      existing = await request(`/productos/${editId}`);
      field(form, "nombre").value = existing.nombre;
      field(form, "descripcion").value = existing.descripcion || "";
      field(form, "precio").value = existing.precio;
      field(form, "stock").value = existing.stock;
      if (existing.categoria) field(form, "categoria").value = String(existing.categoria.id);
      if (existing.artesano) field(form, "artesano").value = String(existing.artesano.id);
      if (state) state.value = existing.estado;
    }
    form.addEventListener("submit", async (event) => {
      event.preventDefault();
      const data = new FormData(form);
      const payload = { nombre: data.get("nombre"), descripcion: data.get("descripcion"), material: "Artesanía textil",
        precio: Number(data.get("precio")), stock: Number(data.get("stock")), estado: data.get("estado"),
        categoriaId: Number(data.get("categoria")) || null, artesanoId: Number(data.get("artesano")) || null,
        imagen: existing?.imagen || "/assets/productos/1.jpg" };
      try { await request(isEdit ? `/productos/${editId}` : "/productos", { method: isEdit ? "PUT" : "POST", body: JSON.stringify(payload) }); location.href = "productos.html"; }
      catch (error) { notice(form).textContent = error.message; }
    });
  };

  const setupCategoryForm = async () => {
    if (isEdit) {
      const category = await request(`/categorias/${editId}`);
      field(form, "nombre").value = category.nombre;
      field(form, "descripcion").value = category.descripcion || "";
      if (state) state.value = category.activa ? "activo" : "inactivo";
    }
    form.addEventListener("submit", async (event) => {
      event.preventDefault(); const data = new FormData(form);
      const payload = { nombre: data.get("nombre"), descripcion: data.get("descripcion"), activa: data.get("estado") !== "inactivo" };
      try { await request(isEdit ? `/categorias/${editId}` : "/categorias", { method: isEdit ? "PUT" : "POST", body: JSON.stringify(payload) }); location.href = "categorias.html"; }
      catch (error) { notice(form).textContent = error.message; }
    });
  };

  const setupArtisanForm = async () => {
    let existing;
    if (isEdit) {
      existing = await request(`/artesanos/${editId}`);
      field(form, "nombre").value = existing.nombre;
      field(form, "especialidad").value = existing.especialidad || "";
      field(form, "anios-experiencia").value = existing.aniosExperiencia || 0;
      field(form, "correo").value = existing.correo || "";
      field(form, "telefono").value = existing.telefono || "";
      if (state) state.value = existing.activo ? "activo" : "inactivo";
    }
    form.addEventListener("submit", async (event) => {
      event.preventDefault(); const data = new FormData(form);
      const payload = { nombre: data.get("nombre"), especialidad: data.get("especialidad"), aniosExperiencia: Number(data.get("anios-experiencia")) || 0,
        correo: data.get("correo"), telefono: data.get("telefono"), activo: data.get("estado") !== "inactivo" };
      try { await request(isEdit ? `/artesanos/${editId}` : "/artesanos", { method: isEdit ? "PUT" : "POST", body: JSON.stringify(payload) }); location.href = "artesanos.html"; }
      catch (error) { notice(form).textContent = error.message; }
    });
  };

  const setup = moduleName === "productos" ? setupProductForm : moduleName === "categorias" ? setupCategoryForm : setupArtisanForm;
  setup().catch((error) => { notice(form).textContent = error.message; });
})();

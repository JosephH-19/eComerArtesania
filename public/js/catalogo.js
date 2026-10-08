(() => {
  const API = window.API_BASE_URL || "/api";
  const CART_KEY = "tallerBuendiaCarrito";
  const categoryFilter = document.querySelector("#filtro-categoria");
  const priceFilter = document.querySelector("#filtro-precio");
  const availabilityFilter = document.querySelector("#filtro-disponibilidad");
  const container = document.querySelector(".productos");
  if (!container) return;

  const readCart = () => {
    try { return JSON.parse(localStorage.getItem(CART_KEY) || "[]"); }
    catch { return []; }
  };
  const saveCart = (cart) => localStorage.setItem(CART_KEY, JSON.stringify(cart));
  const text = (tag, className, value) => {
    const element = document.createElement(tag);
    if (className) element.className = className;
    element.textContent = value ?? "";
    return element;
  };

  let products = [];
  const matchesPrice = (price, range) => {
    if (!range || range === "todos") return true;
    if (range === "200+") return price > 200;
    const [minimum, maximum] = range.split("-").map(Number);
    return price >= minimum && price <= maximum;
  };
  const render = () => {
    const category = String(categoryFilter?.value || "todos").toLowerCase();
    const range = String(priceFilter?.value || "todos");
    const availability = String(availabilityFilter?.value || "todos");
    const visible = products.filter((product) => {
      const categoryName = product.categoria?.nombre?.toLowerCase() || "";
      const categoryMatch = category === "todos" || categoryName.includes(category) || categoryName === "ponchos" && category === "textiles";
      const stockMatch = availability === "todos" || (availability === "disponible" ? product.stock > 0 : product.stock <= 0);
      return categoryMatch && matchesPrice(Number(product.precio), range) && stockMatch;
    });

    container.replaceChildren();
    visible.forEach((product) => {
      const card = text("article", "producto-card");
      card.dataset.productId = product.id;
      const imageFrame = text("div", "producto-imagen", "");
      const image = document.createElement("img");
      image.src = product.imagen || "/assets/productos/1.jpg";
      image.alt = product.nombre;
      imageFrame.append(image);
      const content = text("div", "producto-contenido", "");
      content.append(text("h3", "", product.nombre));
      content.append(text("p", "", `${product.material || "Artesanía textil"} · Stock: ${product.stock}`));
      content.append(text("p", "precio", `S/ ${Number(product.precio).toFixed(2)}`));
      const actions = text("div", "acciones-producto", "");
      const details = text("a", "", "VER DETALLES");
      details.href = `detalle-producto.html?id=${encodeURIComponent(product.id)}`;
      const add = text("button", "agregar-carrito", "AGREGAR AL CARRITO");
      add.type = "button";
      add.dataset.productId = product.id;
      actions.append(details, add);
      content.append(actions);
      card.append(imageFrame, content);
      container.append(card);
    });
    if (!visible.length) container.append(text("p", "consulta-vacia", "No hay productos que coincidan con los filtros."));
  };

  [categoryFilter, priceFilter, availabilityFilter].forEach((filter) => filter?.addEventListener("change", render));
  container.addEventListener("click", async (event) => {
    const button = event.target.closest(".agregar-carrito");
    if (!button) return;
    const product = products.find((entry) => String(entry.id) === button.dataset.productId);
    if (!product) return;
    const cart = readCart();
    const existing = cart.find((entry) => Number(entry.productId) === Number(product.id));
    if (existing) existing.quantity += 1;
    else cart.push({ productId: product.id, name: product.nombre, image: product.imagen || "/assets/productos/1.jpg", unitPrice: Number(product.precio), quantity: 1 });
    saveCart(cart);
    button.textContent = "AGREGADO";
    window.setTimeout(() => { button.textContent = "AGREGAR AL CARRITO"; }, 1000);
  });

  fetch(`${API}/productos`)
    .then((response) => { if (!response.ok) throw new Error("No se pudo cargar el catálogo"); return response.json(); })
    .then((data) => { products = data; render(); })
    .catch(() => container.prepend(text("p", "consulta-vacia", "No se pudo conectar con el catálogo del servidor.")));
})();

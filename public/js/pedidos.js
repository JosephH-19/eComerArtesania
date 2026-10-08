(() => {
  const API = window.API_BASE_URL || "/api";
  const CART_KEY = "tallerBuendiaCarrito";
  const LAST_ORDER_KEY = "tallerBuendiaUltimoPedido";
  const money = (value) => `S/ ${Number(value || 0).toFixed(2)}`;
  const jsonRequest = async (path, options = {}) => {
    const response = await fetch(`${API}${path}`, {
      headers: { "Content-Type": "application/json", ...(options.headers || {}) },
      ...options
    });
    const data = response.status === 204 ? null : await response.json().catch(() => null);
    if (!response.ok) throw new Error(data?.error || `Error ${response.status}`);
    return data;
  };
  const readCart = () => {
    try { return JSON.parse(localStorage.getItem(CART_KEY) || "[]"); }
    catch { return []; }
  };
  const saveCart = (items) => localStorage.setItem(CART_KEY, JSON.stringify(items));
  const node = (tag, className, text) => {
    const element = document.createElement(tag);
    if (className) element.className = className;
    if (text !== undefined) element.textContent = text;
    return element;
  };
  const showError = (message) => {
    const target = document.querySelector("#aviso");
    if (target) { target.className = "error"; target.textContent = message; }
  };

  const cartRoot = document.querySelector("#carrito");
  if (cartRoot) {
    let items = readCart();
    if (!items.length) {
      items = Array.from(cartRoot.querySelectorAll(".producto-carrito")).map((row, index) => ({
        productId: Number(row.dataset.productId || index + 1),
        name: row.querySelector("h3")?.textContent.trim() || "Producto artesanal",
        image: row.querySelector("img")?.getAttribute("src") || "",
        unitPrice: Number((row.querySelector(".precio-producto")?.textContent || "0").replace(/[^\d.]/g, "")) || 0,
        quantity: Number(row.querySelector(".cantidad-producto input")?.value || 1)
      }));
      saveCart(items);
    }

    const renderCart = () => {
      cartRoot.replaceChildren();
      items.forEach((item) => {
        const row = node("div", "producto-carrito");
        row.dataset.productId = String(item.productId);
        const image = node("img", "img-producto"); image.src = item.image; image.alt = item.name;
        const info = node("div", "info-producto");
        info.append(node("h3", "", item.name), node("p", "precio-producto", money(item.unitPrice)));
        const quantity = node("div", "cantidad-producto");
        const minus = node("button", "menos", "-"); minus.type = "button"; minus.setAttribute("aria-label", "Disminuir cantidad");
        const input = node("input", "", String(item.quantity)); input.type = "number"; input.min = "1"; input.setAttribute("aria-label", "Cantidad");
        const plus = node("button", "mas", "+"); plus.type = "button"; plus.setAttribute("aria-label", "Aumentar cantidad");
        quantity.append(minus, input, plus);
        const subtotal = node("p", "subtotal-producto", money(item.unitPrice * item.quantity));
        const remove = node("button", "eliminar-producto", "×"); remove.type = "button"; remove.setAttribute("aria-label", "Eliminar producto");
        row.append(image, info, quantity, subtotal, remove);
        cartRoot.append(row);
      });
      const total = items.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0);
      const summary = document.querySelectorAll(".resumen-compra .fila strong");
      if (summary[0]) summary[0].textContent = money(total);
      if (summary[1]) summary[1].textContent = money(total);
      saveCart(items);
    };

    cartRoot.addEventListener("click", (event) => {
      const row = event.target.closest(".producto-carrito");
      if (!row) return;
      const item = items.find((entry) => String(entry.productId) === row.dataset.productId);
      if (!item) return;
      if (event.target.closest(".mas")) item.quantity += 1;
      else if (event.target.closest(".menos")) item.quantity = Math.max(1, item.quantity - 1);
      else if (event.target.closest(".eliminar-producto")) items = items.filter((entry) => entry !== item);
      else return;
      renderCart();
    });
    cartRoot.addEventListener("change", (event) => {
      if (!event.target.matches(".cantidad-producto input")) return;
      const row = event.target.closest(".producto-carrito");
      const item = items.find((entry) => String(entry.productId) === row.dataset.productId);
      if (item) item.quantity = Math.max(1, Number(event.target.value) || 1);
      renderCart();
    });
    document.querySelector("#vaciar")?.addEventListener("click", () => { items = []; renderCart(); });
    document.querySelector(".continuar-pedido")?.addEventListener("click", (event) => {
      if (!items.length) { event.preventDefault(); showError("Agrega al menos un producto antes de continuar."); }
    });
    renderCart();
  }

  const checkoutForm = document.querySelector("#form-confirmar-pedido");
  if (checkoutForm) {
    const items = readCart();
    const list = document.querySelector("#lista-productos-pedido");
    if (list) {
      list.replaceChildren();
      items.forEach((item) => {
        const row = node("div", "producto-resumen");
        const img = node("img", "img-resumen"); img.src = item.image; img.alt = item.name;
        const info = node("div", "info-resumen");
        info.append(node("h3", "", item.name), node("p", "", `Cantidad: ${item.quantity} | ${money(item.unitPrice * item.quantity)}`));
        row.append(img, info); list.append(row);
      });
    }
    const total = items.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0);
    const totalNode = document.querySelector("#total-pedido");
    if (totalNode) totalNode.textContent = money(total);
    const submit = checkoutForm.querySelector("[type='submit']");
    if (!items.length && submit) submit.disabled = true;

    checkoutForm.addEventListener("submit", async (event) => {
      event.preventDefault();
      if (!items.length) return showError("El carrito está vacío.");
      const data = new FormData(checkoutForm);
      try {
        const order = await jsonRequest("/pedidos", { method: "POST", body: JSON.stringify({
          nombre: data.get("nombre"), dni: data.get("dni"), correo: data.get("correo"),
          telefono: data.get("telefono"), direccion: data.get("direccion"), distrito: "",
          metodoEntrega: "RECOJO_TALLER",
          items: items.map((item) => ({ productoId: Number(item.productId), cantidad: Number(item.quantity) }))
        }) });
        const payment = await jsonRequest("/pagos", { method: "POST", body: JSON.stringify({
          pedidoId: order.id, metodo: data.get("metodo-pago"), monto: order.total,
          referencia: "Pago pendiente de confirmación"
        }) });
        localStorage.setItem(LAST_ORDER_KEY, JSON.stringify({ orderId: order.id, paymentId: payment.id }));
        localStorage.removeItem(CART_KEY);
        location.href = "resultado-pedido.html";
      } catch (error) {
        showError(`No se pudo registrar el pedido: ${error.message}`);
      }
    });
  }

  const result = document.querySelector("#resultado-pedido");
  if (result) {
    const saved = JSON.parse(localStorage.getItem(LAST_ORDER_KEY) || "null");
    result.replaceChildren();
    if (!saved?.orderId) {
      result.append(node("h1", "", "No hay un pedido reciente"), node("p", "", "Confirma un pedido para ver su resultado."));
    } else {
      jsonRequest(`/pedidos/${saved.orderId}`).then((order) => {
        result.append(node("div", "resultado-icono", "✓"), node("h1", "", "PEDIDO REGISTRADO"),
          node("p", "", `Código: ${order.codigo}`), node("p", "", `Estado: ${order.estado}`),
          node("p", "", `Total: ${money(order.total)}`),
          node("p", "", "El pedido quedará confirmado cuando el pago sea validado."));
      }).catch((error) => result.append(node("p", "consulta-vacia", error.message)));
    }
  }

  const lookup = document.querySelector("#consulta-dni");
  if (lookup) {
    lookup.addEventListener("submit", async (event) => {
      event.preventDefault();
      const dni = String(new FormData(lookup).get("dni") || "").replace(/\D/g, "");
      const results = document.querySelector("#resultados-pedidos");
      results.replaceChildren();
      try {
        const orders = await jsonRequest(`/pedidos/dni/${encodeURIComponent(dni)}`);
        if (!orders.length) return results.append(node("p", "consulta-vacia", "No encontramos pedidos activos con ese DNI."));
        orders.forEach((order) => {
          const card = node("article", "pedido-card");
          const header = node("div", "pedido-header");
          header.append(node("h2", "", `Pedido ${order.codigo}`), node("span", "estado-pedido confirmado", order.estado));
          const body = node("div", "pedido-body");
          body.append(node("p", "", `Fecha: ${new Date(order.fechaCreacion).toLocaleDateString("es-PE")}`),
            node("p", "", `Total: ${money(order.total)}`), node("p", "", `Productos: ${order.items.reduce((sum, item) => sum + item.cantidad, 0)}`));
          card.append(header, body); results.append(card);
        });
      } catch (error) {
        results.append(node("p", "consulta-vacia", error.message));
      }
    });
  }
})();

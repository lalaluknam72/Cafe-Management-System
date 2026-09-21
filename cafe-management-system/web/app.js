const state = {
  products: [],
  cart: [],       // [{productId, name, price, quantity}]
  tables: [],
  category: "ทั้งหมด",
};


async function api(method, url, body) {
  const options = { method, headers: {} };
  if (body !== undefined) {
    options.headers["Content-Type"] = "application/json";
    options.body = JSON.stringify(body);
  }
  const res = await fetch(url, options);
  const data = await res.json().catch(() => ({}));
  if (!res.ok) {
    throw new Error(data.error || "เกิดข้อผิดพลาดที่ไม่ทราบสาเหตุ");
  }
  return data;
}

function escapeHtml(str) {
  if (str === null || str === undefined) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;");
}

function money(n) {
  return Number(n).toFixed(2);
}


function setupTabs() {
  document.querySelectorAll(".tab-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
      document.querySelectorAll(".tab-btn").forEach((b) => b.classList.remove("active"));
      document.querySelectorAll(".tab-panel").forEach((p) => p.classList.remove("active"));
      btn.classList.add("active");
      document.getElementById("tab-" + btn.dataset.tab).classList.add("active");
      if (btn.dataset.tab === "reservation") { loadTables(); }
      if (btn.dataset.tab === "staff") { loadStaffOrders(); }
    });
  });

  document.querySelectorAll(".subtab-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
      document.querySelectorAll(".subtab-btn").forEach((b) => b.classList.remove("active"));
      document.querySelectorAll(".subtab-panel").forEach((p) => p.classList.remove("active"));
      btn.classList.add("active");
      document.getElementById(btn.dataset.subtab).classList.add("active");

      const map = {
        staffOrders: loadStaffOrders,
        staffMenu: loadStaffProducts,
        staffReservations: loadStaffReservations,
        staffIngredients: loadIngredients,
        staffReports: loadReport,
      };
      if (map[btn.dataset.subtab]) map[btn.dataset.subtab]();
    });
  });
}


async function loadMenu() {
  state.products = await api("GET", "/api/products");
  renderCategoryFilter();
  renderProducts();
}

function renderCategoryFilter() {
  const cats = ["ทั้งหมด", ...new Set(state.products.map((p) => p.category))];
  const wrap = document.getElementById("categoryFilter");
  wrap.innerHTML = "";
  cats.forEach((cat) => {
    const btn = document.createElement("button");
    btn.textContent = cat;
    if (cat === state.category) btn.classList.add("active");
    btn.addEventListener("click", () => {
      state.category = cat;
      renderCategoryFilter();
      renderProducts();
    });
    wrap.appendChild(btn);
  });
}

function renderProducts() {
  const list = state.products.filter(
    (p) => state.category === "ทั้งหมด" || p.category === state.category
  );
  const wrap = document.getElementById("productList");
  wrap.innerHTML = "";

  if (list.length === 0) {
    wrap.innerHTML = "<p>ยังไม่มีเมนูในหมวดนี้</p>";
    return;
  }

  list.forEach((p) => {
    const card = document.createElement("div");
    card.className = "product-card" + (p.available ? "" : " unavailable");
    card.innerHTML = `
      <img src="${p.image}" class="product-img" alt="${p.name}">
      <div class="pname">${escapeHtml(p.name)}</div>
      <div class="pprice">${money(p.price)} บาท</div>
      <button class="btn-add" ${p.available ? "" : "disabled"}>
        ${p.available ? "เพิ่มลงตะกร้า" : "สินค้าหมด"}
      </button>
    `;
    card.querySelector(".btn-add").addEventListener("click", () => addToCart(p));
    wrap.appendChild(card);
  });
}

function addToCart(product) {
  const existing = state.cart.find((i) => i.productId === product.id);
  if (existing) {
    existing.quantity += 1;
  } else {
    state.cart.push({
      productId: product.id,
      name: product.name,
      price: product.price,
      quantity: 1,
    });
  }
  renderCart();
}

function renderCart() {
  const wrap = document.getElementById("cartItems");
  wrap.innerHTML = "";

  if (state.cart.length === 0) {
    wrap.innerHTML = "<p style='color:#8a7a6c'>ยังไม่มีสินค้าในตะกร้า</p>";
  }

  let total = 0;
  state.cart.forEach((item, idx) => {
    const subtotal = item.price * item.quantity;
    total += subtotal;
    const row = document.createElement("div");
    row.className = "cart-item";
    row.innerHTML = `
      <span>${escapeHtml(item.name)} x${item.quantity} = ${money(subtotal)} บาท</span>
      <button title="ลบออก">✕</button>
    `;
    row.querySelector("button").addEventListener("click", () => {
      state.cart.splice(idx, 1);
      renderCart();
    });
    wrap.appendChild(row);
  });

  document.getElementById("cartTotal").textContent = money(total);
}

async function checkout() {
  const msg = document.getElementById("orderResultMsg");
  msg.className = "hint";
  msg.textContent = "";

  if (state.cart.length === 0) {
    msg.textContent = "กรุณาเพิ่มสินค้าลงตะกร้าก่อน";
    msg.className = "hint error";
    return;
  }

  const body = {
    orderType: "ONLINE",
    pickupType: document.getElementById("pickupType").value,
    pickupTime: document.getElementById("pickupTime").value,
    items: state.cart.map((i) => ({ productId: i.productId, quantity: i.quantity })),
  };

  try {
    const order = await api("POST", "/api/orders", body);
    state.cart = [];
    renderCart();
    msg.textContent = `สั่งซื้อสำเร็จ! หมายเลขออเดอร์ของคุณคือ #${order.id} (จดไว้เพื่อติดตามสถานะ)`;
    document.getElementById("orderIdInput").value = order.id;
  } catch (e) {
    msg.textContent = e.message;
    msg.className = "hint error";
  }
}

async function loadTables() {
  state.tables = await api("GET", "/api/tables");
  renderTableSelect();
  renderTableGrid();
}

function renderTableSelect() {
  const select = document.getElementById("resTable");
  select.innerHTML = "";
  state.tables.forEach((t) => {
    const opt = document.createElement("option");
    opt.value = t.id;
    opt.textContent = `โต๊ะ ${t.id} (นั่งได้ ${t.capacity} คน) - ${t.status}`;
    if (t.status !== "ว่าง") opt.disabled = true;
    select.appendChild(opt);
  });
}

function renderTableGrid() {
  const wrap = document.getElementById("tableGrid");
  wrap.innerHTML = "";
  state.tables.forEach((t) => {
    const chip = document.createElement("div");
    chip.className = "table-chip " + (t.status === "ว่าง" ? "available" : "taken");
    chip.innerHTML = `<div><b>โต๊ะ ${t.id}</b></div><div class="tstatus">${escapeHtml(t.status)} · ${t.capacity} ที่นั่ง</div>`;
    wrap.appendChild(chip);
  });
}

async function submitReservation(e) {
  e.preventDefault();
  const msg = document.getElementById("reservationResultMsg");
  msg.className = "hint";

  const body = {
    date: document.getElementById("resDate").value,
    time: document.getElementById("resTime").value,
    numberOfPeople: Number(document.getElementById("resPeople").value),
    serviceType: "นั่งที่ร้าน",
    tableId: Number(document.getElementById("resTable").value),
  };

  try {
    const reservation = await api("POST", "/api/reservations", body);
    msg.textContent = `จองโต๊ะสำเร็จ! หมายเลขการจอง #${reservation.id} - รอพนักงานยืนยัน`;
    loadTables();
  } catch (e2) {
    msg.textContent = e2.message;
    msg.className = "hint error";
  }
}

function orderStatusBadgeClass(status) {
  return "status-badge";
}

function renderOrderCard(order, opts) {
  opts = opts || {};
  const itemsHtml = order.items
    .map((i) => `${escapeHtml(i.name)} x${i.quantity} = ${money(i.subtotal)} บาท`)
    .join("<br>");

  const paid = order.payment && order.payment.status === "ชำระแล้ว";

  const div = document.createElement("div");
  div.className = "order-card";
  div.innerHTML = `
    <div class="ohead">
      <b>ออเดอร์ #${order.id}</b>
      <span class="status-badge">${escapeHtml(order.status)}</span>
    </div>
    <div class="items">${itemsHtml}</div>
    <div><b>รวม: ${money(order.total)} บาท</b> ${paid ? "· ชำระเงินแล้ว " : "· ยังไม่ชำระเงิน"}</div>
    <div class="actions" data-actions></div>
  `;

  const actions = div.querySelector("[data-actions]");

  if (opts.showPayButton && !paid) {
    const payBtn = document.createElement("button");
    payBtn.textContent = "ชำระเงิน";
    payBtn.addEventListener("click", () => payOrder(order.id));
    actions.appendChild(payBtn);
  }

  if (opts.staffControls) {
    const statuses = ["รอดำเนินการ", "รับออเดอร์แล้ว", "กำลังทำ", "เสร็จแล้ว", "ยกเลิก"];
    const select = document.createElement("select");
    statuses.forEach((s) => {
      const o = document.createElement("option");
      o.value = s;
      o.textContent = s;
      if (s === order.status) o.selected = true;
      select.appendChild(o);
    });
    select.addEventListener("change", async () => {
      await api("PUT", `/api/orders/${order.id}/status`, { status: select.value });
      loadStaffOrders();
    });
    actions.appendChild(select);

    if (!paid) {
      const payBtn = document.createElement("button");
      payBtn.textContent = "รับชำระเงิน";
      payBtn.addEventListener("click", () => payOrder(order.id, true));
      actions.appendChild(payBtn);
    }
  }

  return div;
}

async function payOrder(orderId, isStaff) {
  const cashStr = window.prompt("รับเงินสดกี่บาท?");
  if (cashStr === null) return;
  const cashReceived = Number(cashStr);
  if (isNaN(cashReceived)) {
    alert("กรุณากรอกตัวเลข");
    return;
  }
  try {
    const order = await api("POST", `/api/orders/${orderId}/payment`, {
      paymentMethod: "เงินสด",
      cashReceived: cashReceived,
    });
    const change = order.payment ? order.payment.change : 0;
    if (change > 0) {
      alert(`ชำระเงินสำเร็จ! เงินทอน ${money(change)} บาท`);
    } else {
      alert("ชำระเงินสำเร็จ!");
    }
    if (isStaff) {
      loadStaffOrders();
    } else {
      searchOrder();
    }
  } catch (e) {
    alert(e.message);
  }
}

async function searchOrder() {
  const id = document.getElementById("orderIdInput").value;
  const wrap = document.getElementById("orderStatusResult");
  wrap.innerHTML = "";
  if (!id) return;

  try {
    const order = await api("GET", `/api/orders/${id}`);
    wrap.appendChild(renderOrderCard(order, { showPayButton: true }));
  } catch (e) {
    wrap.innerHTML = `<p class="hint error">${escapeHtml(e.message)}</p>`;
  }
}


async function loadStaffOrders() {
  const orders = await api("GET", "/api/orders");
  const wrap = document.getElementById("staffOrderList");
  wrap.innerHTML = "";

  if (orders.length === 0) {
    wrap.innerHTML = "<p>ยังไม่มีออเดอร์เข้ามา</p>";
    return;
  }

  orders.forEach((order) => {
    wrap.appendChild(renderOrderCard(order, { staffControls: true }));
  });
}


async function loadStaffProducts() {
  const products = await api("GET", "/api/products");
  const wrap = document.getElementById("staffProductList");
  wrap.innerHTML = "";

  products.forEach((p) => {
    const row = document.createElement("div");
    row.className = "staff-product-row";
    row.innerHTML = `
      <span class="spname">${escapeHtml(p.image || "")} ${escapeHtml(p.name)}</span>
      <span>${escapeHtml(p.category)}</span>
      <input type="number" min="0" step="0.01" value="${p.price}" data-price>
      <button data-save>บันทึกราคา</button>
      <button data-toggle class="${p.available ? "" : "toggle-off"}">
        ${p.available ? "เปิดขายอยู่ (กดเพื่อปิด)" : "ปิดขายอยู่ (กดเพื่อเปิด)"}
      </button>
    `;

    row.querySelector("[data-save]").addEventListener("click", async () => {
      const price = Number(row.querySelector("[data-price]").value);
      await api("PUT", `/api/products/${p.id}/price`, { price });
      loadStaffProducts();
      loadMenu();
    });

    row.querySelector("[data-toggle]").addEventListener("click", async () => {
      await api("PUT", `/api/products/${p.id}/availability`, { available: !p.available });
      loadStaffProducts();
      loadMenu();
    });

    wrap.appendChild(row);
  });
}

async function submitNewProduct(e) {
  e.preventDefault();
  const body = {
    name: document.getElementById("newProductName").value,
    category: document.getElementById("newProductCategory").value,
    price: Number(document.getElementById("newProductPrice").value),
    description: document.getElementById("newProductDesc").value,
    image: document.getElementById("newProductImage").value || "🥤",
    available: true,
  };
  await api("POST", "/api/products", body);
  document.getElementById("addProductForm").reset();
  loadStaffProducts();
  loadMenu();
}


async function loadStaffReservations() {
  const reservations = await api("GET", "/api/reservations");
  const wrap = document.getElementById("staffReservationList");
  wrap.innerHTML = "";

  if (reservations.length === 0) {
    wrap.innerHTML = "<p>ยังไม่มีการจองโต๊ะ</p>";
    return;
  }

  reservations.forEach((r) => {
    const div = document.createElement("div");
    div.className = "res-card";
    div.innerHTML = `
      <div class="ohead">
        <b>การจอง #${r.id}</b>
        <span class="status-badge">${escapeHtml(r.status)}</span>
      </div>
      <div>วันที่ ${escapeHtml(r.date)} เวลา ${escapeHtml(r.time)} · ${r.numberOfPeople} คน
        · โต๊ะ ${r.table ? r.table.id : "-"}</div>
      <div class="actions" data-actions></div>
    `;
    const actions = div.querySelector("[data-actions]");

    if (r.status === "รอยืนยัน") {
      const confirmBtn = document.createElement("button");
      confirmBtn.textContent = "ยืนยันการจอง";
      confirmBtn.addEventListener("click", async () => {
        await api("PUT", `/api/reservations/${r.id}/confirm`);
        loadStaffReservations();
      });
      actions.appendChild(confirmBtn);
    }

    if (r.status !== "ยกเลิก") {
      const cancelBtn = document.createElement("button");
      cancelBtn.textContent = "ยกเลิกการจอง";
      cancelBtn.addEventListener("click", async () => {
        await api("PUT", `/api/reservations/${r.id}/cancel`);
        loadStaffReservations();
      });
      actions.appendChild(cancelBtn);
    }

    wrap.appendChild(div);
  });
}


async function loadIngredients() {
  const ingredients = await api("GET", "/api/ingredients");
  const wrap = document.getElementById("ingredientList");
  wrap.innerHTML = "";

  ingredients.forEach((ing) => {
    const row = document.createElement("div");
    row.className = "staff-product-row";
    row.innerHTML = `
      <span class="spname">${escapeHtml(ing.name)} ${ing.lowStock ? "⚠ ใกล้หมด" : ""}</span>
      <span>${money(ing.quantity)} ${escapeHtml(ing.unit)} (ขั้นต่ำ ${money(ing.minimumStock)})</span>
      <button data-add>+ เพิ่มสต๊อก</button>
      <button data-reduce>- ลดสต๊อก</button>
    `;
    row.querySelector("[data-add]").addEventListener("click", async () => {
      const amt = Number(window.prompt("เพิ่มจำนวนเท่าไร?", "10"));
      if (!isNaN(amt)) {
        await api("PUT", `/api/ingredients/${ing.id}/stock`, { amount: amt, mode: "add" });
        loadIngredients();
      }
    });
    row.querySelector("[data-reduce]").addEventListener("click", async () => {
      const amt = Number(window.prompt("ลดจำนวนเท่าไร?", "10"));
      if (!isNaN(amt)) {
        await api("PUT", `/api/ingredients/${ing.id}/stock`, { amount: amt, mode: "reduce" });
        loadIngredients();
      }
    });
    wrap.appendChild(row);
  });
}

async function submitNewIngredient(e) {
  e.preventDefault();
  const body = {
    name: document.getElementById("newIngName").value,
    unit: document.getElementById("newIngUnit").value,
    quantity: Number(document.getElementById("newIngQty").value),
    minimumStock: Number(document.getElementById("newIngMin").value),
  };
  await api("POST", "/api/ingredients", body);
  document.getElementById("addIngredientForm").reset();
  loadIngredients();
}


async function loadReport() {
  const report = await api("GET", "/api/reports");

  document.getElementById("reportSummary").innerHTML = `
    <div class="report-box"><div class="label">รายรับรวม</div><div class="value">${money(report.totalIncome)}</div></div>
    <div class="report-box"><div class="label">รายจ่ายรวม</div><div class="value">${money(report.totalExpense)}</div></div>
    <div class="report-box"><div class="label">กำไรสุทธิ</div><div class="value">${money(report.profit)}</div></div>
  `;

  const incomeWrap = document.getElementById("incomeList");
  incomeWrap.innerHTML = report.incomes
    .map((i) => `<div class="report-line">[${escapeHtml(i.date)}] ${escapeHtml(i.description)}: +${money(i.amount)} บาท</div>`)
    .join("") || "<p>ยังไม่มีรายรับ</p>";

  const expenseWrap = document.getElementById("expenseList");
  expenseWrap.innerHTML = report.expenses
    .map((e) => `<div class="report-line">[${escapeHtml(e.date)}] ${escapeHtml(e.description)} (${escapeHtml(e.category)}): -${money(e.amount)} บาท</div>`)
    .join("") || "<p>ยังไม่มีรายจ่าย</p>";
}

async function submitNewExpense(e) {
  e.preventDefault();
  const body = {
    description: document.getElementById("expDesc").value,
    amount: Number(document.getElementById("expAmount").value),
    category: document.getElementById("expCategory").value,
  };
  await api("POST", "/api/expenses", body);
  document.getElementById("addExpenseForm").reset();
  loadReport();
}


document.addEventListener("DOMContentLoaded", () => {
  setupTabs();

  document.getElementById("btnCheckout").addEventListener("click", checkout);
  document.getElementById("reservationForm").addEventListener("submit", submitReservation);
  document.getElementById("btnSearchOrder").addEventListener("click", searchOrder);
  document.getElementById("addProductForm").addEventListener("submit", submitNewProduct);
  document.getElementById("addIngredientForm").addEventListener("submit", submitNewIngredient);
  document.getElementById("addExpenseForm").addEventListener("submit", submitNewExpense);

  loadMenu();
});

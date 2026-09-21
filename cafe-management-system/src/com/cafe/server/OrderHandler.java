package com.cafe.server;

import com.cafe.model.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OrderHandler implements HttpHandler {

    private final CafeStore store = CafeStore.INSTANCE;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<String> parts = HttpUtil.splitPath(exchange.getRequestURI().getPath());
        String method = exchange.getRequestMethod();

        try {
            if (parts.size() == 2 && method.equals("GET")) {
                listOrders(exchange);
            } else if (parts.size() == 2 && method.equals("POST")) {
                createOrder(exchange);
            } else if (parts.size() == 3 && method.equals("GET")) {
                getOrder(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else if (parts.size() == 4 && method.equals("PUT") && parts.get(3).equals("status")) {
                updateStatus(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else if (parts.size() == 4 && method.equals("POST") && parts.get(3).equals("payment")) {
                processPayment(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else {
                HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบ endpoint นี้"));
            }
        } catch (Exception e) {
            HttpUtil.sendJson(exchange, 400, Dto.error("เกิดข้อผิดพลาด: " + e.getMessage()));
        }
    }

    private void listOrders(HttpExchange exchange) throws IOException {
        List<Object> list = new ArrayList<>();
        List<Order> sorted = new ArrayList<>(store.orders.values());
        sorted.sort((a, b) -> Integer.compare(b.getOrderId(), a.getOrderId()));
        for (Order o : sorted) {
            list.add(Dto.order(o));
        }
        HttpUtil.sendJson(exchange, 200, list);
    }

    private void getOrder(HttpExchange exchange, int id) throws IOException {
        Order o = store.orders.get(id);
        if (o == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบออเดอร์นี้"));
            return;
        }
        HttpUtil.sendJson(exchange, 200, Dto.order(o));
    }

    @SuppressWarnings("unchecked")
    private void createOrder(HttpExchange exchange) throws IOException {
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);

        int id = store.nextOrderId();
        String today = LocalDate.now().toString();
        Order order = new Order(
                id,
                today,
                Json.getString(body, "orderType", "ONLINE"),
                Json.getString(body, "pickupType", "DINE_IN"),
                Json.getString(body, "pickupDate", today),
                Json.getString(body, "pickupTime", "")
        );

        Object itemsObj = body.get("items");
        if (itemsObj instanceof List) {
            for (Object itemObj : (List<Object>) itemsObj) {
                Map<String, Object> itemMap = (Map<String, Object>) itemObj;
                int productId = Json.getInt(itemMap, "productId", -1);
                int quantity = Json.getInt(itemMap, "quantity", 1);
                Product product = store.products.get(productId);
                if (product == null) {
                    HttpUtil.sendJson(exchange, 400, Dto.error("ไม่พบสินค้ารหัส " + productId));
                    return;
                }
                order.addItem(new OrderItem(product, quantity));
            }
        }

        if (order.getItems().isEmpty()) {
            HttpUtil.sendJson(exchange, 400, Dto.error("ออเดอร์ต้องมีอย่างน้อย 1 รายการ"));
            return;
        }

        order.confirmOrder();
        store.orders.put(id, order);
        HttpUtil.sendJson(exchange, 201, Dto.order(order));
    }

    private void updateStatus(HttpExchange exchange, int id) throws IOException {
        Order order = store.orders.get(id);
        if (order == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบออเดอร์นี้"));
            return;
        }
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        order.updateStatus(Json.getString(body, "status", order.getStatus()));
        HttpUtil.sendJson(exchange, 200, Dto.order(order));
    }

    private void processPayment(HttpExchange exchange, int id) throws IOException {
        Order order = store.orders.get(id);
        if (order == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบออเดอร์นี้"));
            return;
        }
                Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        String method = Json.getString(body, "paymentMethod", "เงินสด");
        double cashReceived = Json.getDouble(body, "cashReceived", order.getTotal());

        if (cashReceived < order.getTotal()) {
            double shortfall = order.getTotal() - cashReceived;
            HttpUtil.sendJson(exchange, 400,
                    Dto.error(String.format("เงินไม่พอ ยอดที่ต้องจ่าย %.2f บาท ยังขาดอีก %.2f บาท",
                            order.getTotal(), shortfall)));
            return;
        }

        Payment payment = new Payment(store.nextPaymentId(), order.getTotal(), method);
        payment.processPayment(cashReceived);
        order.setPayment(payment);
        order.updateStatus("ชำระเงินแล้ว");

        // บันทึกรายรับอัตโนมัติ
        Income income = new Income(
                store.nextIncomeId(),
                "ขายสินค้า ออเดอร์ #" + order.getOrderId(),
                payment.getAmount(),
                LocalDate.now().toString(),
                "ขายหน้าร้าน"
        );
        store.incomes.add(income);

        HttpUtil.sendJson(exchange, 200, Dto.order(order));
    }
}

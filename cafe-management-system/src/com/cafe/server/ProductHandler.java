package com.cafe.server;

import com.cafe.model.Product;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductHandler implements HttpHandler {

    private final CafeStore store = CafeStore.INSTANCE;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<String> parts = HttpUtil.splitPath(exchange.getRequestURI().getPath());
        String method = exchange.getRequestMethod();

        try {
            if (parts.size() == 2 && method.equals("GET")) {
                listProducts(exchange);
            } else if (parts.size() == 2 && method.equals("POST")) {
                createProduct(exchange);
            } else if (parts.size() == 3 && method.equals("PUT")) {
                updateProduct(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else if (parts.size() == 4 && method.equals("PUT") && parts.get(3).equals("price")) {
                updatePrice(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else if (parts.size() == 4 && method.equals("PUT") && parts.get(3).equals("availability")) {
                updateAvailability(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else {
                HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบ endpoint นี้"));
            }
        } catch (Exception e) {
            HttpUtil.sendJson(exchange, 400, Dto.error("เกิดข้อผิดพลาด: " + e.getMessage()));
        }
    }

    private void listProducts(HttpExchange exchange) throws IOException {
        List<Object> list = new ArrayList<>();
        for (Product p : store.products.values()) {
            list.add(Dto.product(p));
        }
        HttpUtil.sendJson(exchange, 200, list);
    }

    private void createProduct(HttpExchange exchange) throws IOException {
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        int id = store.nextProductId();
        Product p = new Product(
                id,
                Json.getString(body, "name", "เมนูใหม่"),
                Json.getString(body, "category", "เครื่องดื่ม"),
                Json.getDouble(body, "price", 0),
                Json.getString(body, "description", ""),
                Json.getString(body, "image", "🥤"),
                Json.getBoolean(body, "available", true)
        );
        store.products.put(id, p);
        HttpUtil.sendJson(exchange, 201, Dto.product(p));
    }

    private void updateProduct(HttpExchange exchange, int id) throws IOException {
        Product p = store.products.get(id);
        if (p == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบเมนูนี้"));
            return;
        }
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        p.updateProduct(
                Json.getString(body, "name", p.getName()),
                Json.getString(body, "category", p.getCategory()),
                Json.getString(body, "description", p.getDescription()),
                Json.getString(body, "image", p.getImage())
        );
        HttpUtil.sendJson(exchange, 200, Dto.product(p));
    }

    private void updatePrice(HttpExchange exchange, int id) throws IOException {
        Product p = store.products.get(id);
        if (p == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบเมนูนี้"));
            return;
        }
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        p.updatePrice(Json.getDouble(body, "price", p.getPrice()));
        HttpUtil.sendJson(exchange, 200, Dto.product(p));
    }

    private void updateAvailability(HttpExchange exchange, int id) throws IOException {
        Product p = store.products.get(id);
        if (p == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบเมนูนี้"));
            return;
        }
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        p.setAvailability(Json.getBoolean(body, "available", p.isAvailable()));
        HttpUtil.sendJson(exchange, 200, Dto.product(p));
    }
}

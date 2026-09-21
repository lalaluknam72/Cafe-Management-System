package com.cafe.server;

import com.cafe.model.Ingredient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class IngredientHandler implements HttpHandler {

    private final CafeStore store = CafeStore.INSTANCE;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<String> parts = HttpUtil.splitPath(exchange.getRequestURI().getPath());
        String method = exchange.getRequestMethod();

        try {
            if (parts.size() == 2 && method.equals("GET")) {
                listIngredients(exchange);
            } else if (parts.size() == 2 && method.equals("POST")) {
                createIngredient(exchange);
            } else if (parts.size() == 4 && method.equals("PUT") && parts.get(3).equals("stock")) {
                updateStock(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else {
                HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบ endpoint นี้"));
            }
        } catch (Exception e) {
            HttpUtil.sendJson(exchange, 400, Dto.error("เกิดข้อผิดพลาด: " + e.getMessage()));
        }
    }

    private void listIngredients(HttpExchange exchange) throws IOException {
        List<Object> list = new ArrayList<>();
        for (Ingredient i : store.inventory.getIngredients()) {
            list.add(Dto.ingredient(i));
        }
        HttpUtil.sendJson(exchange, 200, list);
    }

    private void createIngredient(HttpExchange exchange) throws IOException {
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        int id = store.nextIngredientId();
        Ingredient ing = new Ingredient(
                id,
                Json.getString(body, "name", "วัตถุดิบใหม่"),
                Json.getString(body, "unit", "หน่วย"),
                Json.getDouble(body, "quantity", 0),
                Json.getDouble(body, "minimumStock", 0)
        );
        store.inventory.addIngredient(ing);
        HttpUtil.sendJson(exchange, 201, Dto.ingredient(ing));
    }

    private void updateStock(HttpExchange exchange, int id) throws IOException {
        Ingredient ing = store.inventory.getIngredient(id);
        if (ing == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบวัตถุดิบนี้"));
            return;
        }
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        double amount = Json.getDouble(body, "amount", 0);
        String mode = Json.getString(body, "mode", "set"); // "add" | "reduce" | "set"

        if (mode.equals("add")) {
            ing.addStock(amount);
        } else if (mode.equals("reduce")) {
            ing.reduceStock(amount);
        } else {
            ing.updateStock(amount);
        }
        HttpUtil.sendJson(exchange, 200, Dto.ingredient(ing));
    }
}

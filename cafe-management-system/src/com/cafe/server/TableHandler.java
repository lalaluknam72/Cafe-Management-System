package com.cafe.server;

import com.cafe.model.Table;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TableHandler implements HttpHandler {

    private final CafeStore store = CafeStore.INSTANCE;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<String> parts = HttpUtil.splitPath(exchange.getRequestURI().getPath());
        String method = exchange.getRequestMethod();

        try {
            if (parts.size() == 2 && method.equals("GET")) {
                listTables(exchange);
            } else if (parts.size() == 4 && method.equals("PUT") && parts.get(3).equals("status")) {
                updateStatus(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else {
                HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบ endpoint นี้"));
            }
        } catch (Exception e) {
            HttpUtil.sendJson(exchange, 400, Dto.error("เกิดข้อผิดพลาด: " + e.getMessage()));
        }
    }

    private void listTables(HttpExchange exchange) throws IOException {
        List<Object> list = new ArrayList<>();
        for (Table t : store.tables.values()) {
            list.add(Dto.table(t));
        }
        HttpUtil.sendJson(exchange, 200, list);
    }

    private void updateStatus(HttpExchange exchange, int id) throws IOException {
        Table t = store.tables.get(id);
        if (t == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบโต๊ะนี้"));
            return;
        }
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        t.updateStatus(Json.getString(body, "status", t.getStatus()));
        HttpUtil.sendJson(exchange, 200, Dto.table(t));
    }
}

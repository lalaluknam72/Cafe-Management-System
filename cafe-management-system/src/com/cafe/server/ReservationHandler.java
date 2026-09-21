package com.cafe.server;

import com.cafe.model.Reservation;
import com.cafe.model.Table;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ReservationHandler implements HttpHandler {

    private final CafeStore store = CafeStore.INSTANCE;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<String> parts = HttpUtil.splitPath(exchange.getRequestURI().getPath());
        String method = exchange.getRequestMethod();

        try {
            if (parts.size() == 2 && method.equals("GET")) {
                listReservations(exchange);
            } else if (parts.size() == 2 && method.equals("POST")) {
                createReservation(exchange);
            } else if (parts.size() == 4 && method.equals("PUT") && parts.get(3).equals("confirm")) {
                confirmReservation(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else if (parts.size() == 4 && method.equals("PUT") && parts.get(3).equals("cancel")) {
                cancelReservation(exchange, HttpUtil.parseIntSafe(parts.get(2)));
            } else {
                HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบ endpoint นี้"));
            }
        } catch (Exception e) {
            HttpUtil.sendJson(exchange, 400, Dto.error("เกิดข้อผิดพลาด: " + e.getMessage()));
        }
    }

    private void listReservations(HttpExchange exchange) throws IOException {
        List<Object> list = new ArrayList<>();
        List<Reservation> sorted = new ArrayList<>(store.reservations.values());
        sorted.sort((a, b) -> Integer.compare(b.getReservationId(), a.getReservationId()));
        for (Reservation r : sorted) {
            list.add(Dto.reservation(r));
        }
        HttpUtil.sendJson(exchange, 200, list);
    }

    private void createReservation(HttpExchange exchange) throws IOException {
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        int tableId = Json.getInt(body, "tableId", -1);
        Table table = store.tables.get(tableId);

        if (table == null) {
            HttpUtil.sendJson(exchange, 400, Dto.error("ไม่พบโต๊ะที่เลือก"));
            return;
        }

        int id = store.nextReservationId();
        Reservation reservation = new Reservation(
                id,
                Json.getString(body, "date", ""),
                Json.getString(body, "time", ""),
                Json.getInt(body, "numberOfPeople", 1),
                Json.getString(body, "serviceType", "นั่งที่ร้าน")
        );

        boolean success = reservation.createReservation(table);
        if (!success) {
            HttpUtil.sendJson(exchange, 400,
                    Dto.error("โต๊ะนี้ไม่ว่าง /โต๊ะนี้รองรับจำนวนคนไม่พอ ลองเลือกโต๊ะอื่น"));
            return;
        }

        store.reservations.put(id, reservation);
        HttpUtil.sendJson(exchange, 201, Dto.reservation(reservation));
    }

    private void confirmReservation(HttpExchange exchange, int id) throws IOException {
        Reservation r = store.reservations.get(id);
        if (r == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบรายการจองนี้"));
            return;
        }
        r.confirmReservation();
        HttpUtil.sendJson(exchange, 200, Dto.reservation(r));
    }

    private void cancelReservation(HttpExchange exchange, int id) throws IOException {
        Reservation r = store.reservations.get(id);
        if (r == null) {
            HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบรายการจองนี้"));
            return;
        }
        r.cancelReservation();
        HttpUtil.sendJson(exchange, 200, Dto.reservation(r));
    }
}

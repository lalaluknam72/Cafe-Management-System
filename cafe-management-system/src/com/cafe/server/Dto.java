package com.cafe.server;

import com.cafe.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dto {

    public static Map<String, Object> product(Product p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getProductId());
        m.put("name", p.getName());
        m.put("category", p.getCategory());
        m.put("price", p.getPrice());
        m.put("description", p.getDescription());
        m.put("image", p.getImage());
        m.put("available", p.isAvailable());
        return m;
    }

    public static Map<String, Object> orderItem(OrderItem item) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("productId", item.getProduct().getProductId());
        m.put("name", item.getProduct().getName());
        m.put("quantity", item.getQuantity());
        m.put("unitPrice", item.getUnitPrice());
        m.put("subtotal", item.calculateSubtotal());
        return m;
    }

    public static Map<String, Object> order(Order o) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", o.getOrderId());
        m.put("orderDate", o.getOrderDate());
        m.put("orderType", o.getOrderType());
        m.put("pickupType", o.getPickupType());
        m.put("pickupDate", o.getPickupDate());
        m.put("pickupTime", o.getPickupTime());
        m.put("status", o.getStatus());
        m.put("total", o.getTotal());

        List<Object> items = new ArrayList<>();
        for (OrderItem item : o.getItems()) {
            items.add(orderItem(item));
        }
        m.put("items", items);
        m.put("payment", o.getPayment() == null ? null : payment(o.getPayment()));
        return m;
    }

    public static Map<String, Object> payment(Payment p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getPaymentId());
        m.put("amount", p.getAmount());
        m.put("paymentMethod", p.getPaymentMethod());
        m.put("cashReceived", p.getCashReceived());
        m.put("change", p.getChange());
        m.put("status", p.getPaymentStatus());
        return m;
    }

    public static Map<String, Object> table(Table t) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", t.getTableId());
        m.put("capacity", t.getCapacity());
        m.put("status", t.getStatus());
        return m;
    }

    public static Map<String, Object> reservation(Reservation r) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", r.getReservationId());
        m.put("date", r.getDate());
        m.put("time", r.getTime());
        m.put("numberOfPeople", r.getNumberOfPeople());
        m.put("serviceType", r.getServiceType());
        m.put("status", r.getStatus());
        m.put("table", r.getTable() == null ? null : table(r.getTable()));
        return m;
    }

    public static Map<String, Object> ingredient(Ingredient i) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", i.getIngredientId());
        m.put("name", i.getName());
        m.put("unit", i.getUnit());
        m.put("quantity", i.getQuantity());
        m.put("minimumStock", i.getMinimumStock());
        m.put("lowStock", i.checkLowStock());
        return m;
    }

    public static Map<String, Object> income(Income inc) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", inc.getIncomeId());
        m.put("description", inc.getDescription());
        m.put("amount", inc.getAmount());
        m.put("date", inc.getDate());
        m.put("source", inc.getSource());
        return m;
    }

    public static Map<String, Object> expense(Expense exp) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", exp.getExpenseId());
        m.put("description", exp.getDescription());
        m.put("amount", exp.getAmount());
        m.put("date", exp.getDate());
        m.put("category", exp.getCategory());
        return m;
    }

    public static Map<String, Object> error(String message) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("error", message);
        return m;
    }
}

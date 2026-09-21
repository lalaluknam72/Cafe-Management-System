package com.cafe.server;

import com.cafe.model.Expense;
import com.cafe.model.Income;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ReportHandler implements HttpHandler {

    private final CafeStore store = CafeStore.INSTANCE;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<String> parts = HttpUtil.splitPath(exchange.getRequestURI().getPath());
        String method = exchange.getRequestMethod();

        try {
            if (parts.size() == 2 && parts.get(1).equals("reports") && method.equals("GET")) {
                getReport(exchange);
            } else if (parts.size() == 2 && parts.get(1).equals("expenses") && method.equals("POST")) {
                addExpense(exchange);
            } else {
                HttpUtil.sendJson(exchange, 404, Dto.error("ไม่พบ endpoint นี้"));
            }
        } catch (Exception e) {
            HttpUtil.sendJson(exchange, 400, Dto.error("เกิดข้อผิดพลาด: " + e.getMessage()));
        }
    }

    private void getReport(HttpExchange exchange) throws IOException {
        double totalIncome = 0;
        double totalExpense = 0;

        List<Object> incomeList = new ArrayList<>();
        for (Income inc : store.incomes) {
            incomeList.add(Dto.income(inc));
            totalIncome += inc.getAmount();
        }

        List<Object> expenseList = new ArrayList<>();
        for (Expense exp : store.expenses) {
            expenseList.add(Dto.expense(exp));
            totalExpense += exp.getAmount();
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("profit", totalIncome - totalExpense);
        result.put("incomes", incomeList);
        result.put("expenses", expenseList);

        HttpUtil.sendJson(exchange, 200, result);
    }

    private void addExpense(HttpExchange exchange) throws IOException {
        Map<String, Object> body = HttpUtil.readJsonObject(exchange);
        Expense exp = new Expense(
                store.nextExpenseId(),
                Json.getString(body, "description", ""),
                Json.getDouble(body, "amount", 0),
                Json.getString(body, "date", LocalDate.now().toString()),
                Json.getString(body, "category", "อื่นๆ")
        );
        store.expenses.add(exp);
        HttpUtil.sendJson(exchange, 201, Dto.expense(exp));
    }
}

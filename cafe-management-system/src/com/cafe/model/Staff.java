package com.cafe.model;

import java.util.ArrayList;
import java.util.List;

public class Staff extends User {

    public Staff(int userId, String name, String phone, String username, String password) {
        super(userId, name, phone, username, password);
    }

    //receiveOrder(): รับออเดอร์เข้ามาในระบบ
    public void receiveOrder(Order order) {
        order.updateStatus("รับออเดอร์แล้ว");
        System.out.println(name + " รับออเดอร์ #" + order.getOrderId() + " แล้ว");
    }

    //prepareDrink(): เริ่มทำเครื่องดื่ม/อัปเดตสถานะ
    public void prepareDrink(Order order) {
        order.updateStatus("กำลังทำ");
        System.out.println(name + " กำลังทำออเดอร์ #" + order.getOrderId());
    }

    //manageOrders(): อัปเดตสถานะออเดอร์
    public void manageOrders(Order order, String newStatus) {
        order.updateStatus(newStatus);
    }

    //processPayment(): คิดเงิน/รับเงินจากลูกค้า
    public Income processPayment(Order order, Payment payment, double cashReceived, int incomeId, String date) {
        payment.processPayment(cashReceived);
        order.setPayment(payment);
        return new Income(incomeId, "ขายสินค้า ออเดอร์ #" + order.getOrderId(),
                payment.getAmount(), date, "ขายหน้าร้าน");
    }

    //manageReservations(): จัดการการจองโต๊ะ
    public void manageReservations(Reservation reservation, boolean confirm) {
        if (confirm) {
            reservation.confirmReservation();
        } else {
            reservation.cancelReservation();
        }
    }

    //manageTables(): อัปเดตสถานะโต๊ะ
    public void manageTables(Table table, String status) {
        table.updateStatus(status);
    }

    //manageIngredients(): จัดการวัตถุดิบในคลัง
    public void manageIngredients(Inventory inventory, Ingredient ingredient) {
        inventory.addIngredient(ingredient);
    }

    //manageMenu(): จัดการเมนู(เพิ่ม/แก้ไข/ลบสินค้า)
    public void manageMenu(List<Product> menu, Product newProduct) {
        menu.add(newProduct);
        System.out.println(name + " เพิ่มเมนู: " + newProduct.getDetails());
    }

    //viewReports(): ดูรายงานสรุปรายรับ-รายจ่าย
    public void viewReports(List<Income> incomeList, List<Expense> expenseList) {
        double totalIncome = 0;
        double totalExpense = 0;

        System.out.println("=== รายงานรายรับ ===");
        for (Income inc : incomeList) {
            System.out.println(inc.getIncomeDetails());
            totalIncome += inc.getAmount();
        }

        System.out.println("=== รายงานรายจ่าย ===");
        for (Expense exp : expenseList) {
            System.out.println(exp.getExpenseDetails());
            totalExpense += exp.getAmount();
        }

        System.out.printf("รวมรายรับ: %.2f บาท | รวมรายจ่าย: %.2f บาท | กำไรสุทธิ: %.2f บาท%n",
                totalIncome, totalExpense, totalIncome - totalExpense);
    }
}

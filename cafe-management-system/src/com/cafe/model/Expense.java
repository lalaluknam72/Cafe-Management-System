package com.cafe.model;

public class Expense {

    //Attributes
    private int expenseId;
    private String description;
    private double amount;
    private String date;
    private String category;

    //Methods
    public Expense(int expenseId, String description, double amount, String date, String category) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    //addExpense(): บันทึกรายจ่าย
    public void addExpense() {
        System.out.println("บันทึกรายจ่าย: " + getExpenseDetails());
    }

    //getExpenseDetails(): คืนค่ารายละเอียดรายจ่ายเป็นข้อความ
    public String getExpenseDetails() {
        return String.format("[%s] %s: -%.2f บาท (%s)", date, description, amount, category);
    }

    //Getter
    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
}

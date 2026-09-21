package com.cafe.model;

public class Income {

    //Attributes
    private int incomeId;
    private String description;
    private double amount;
    private String date;
    private String source;

    //Methods
    public Income(int incomeId, String description, double amount, String date, String source) {
        this.incomeId = incomeId;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.source = source;
    }

    //addIncome(): บันทึกรายรับ
    public void addIncome() {
        System.out.println("บันทึกรายรับ: " + getIncomeDetails());
    }

    //getIncomeDetails(): คืนค่ารายละเอียดรายรับเป็นข้อความ
    public String getIncomeDetails() {
        return String.format("[%s] %s: +%.2f บาท (จาก %s)", date, description, amount, source);
    }

    //Getter
    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getIncomeId() {
        return incomeId;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }
}

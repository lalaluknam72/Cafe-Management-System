package com.cafe.model;

public class Payment {

    //Attributes
    private int paymentId;
    private double amount;
    private String paymentMethod;
    private double cashReceived;
    private double change;
    private String paymentStatus;
    //Methods
    public Payment(int paymentId, double amount, String paymentMethod) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "รอชำระ";
    }

    //processPayment(): ดำเนินการรับชำระเงิน
    public void processPayment(double cashReceived) {
        this.cashReceived = cashReceived;
        calculateChange();
        confirmPayment();
    }

    //calculateChange(): คำนวณเงินทอน = เงินที่รับมา - ยอดที่ต้องจ่าย
    public double calculateChange() {
        this.change = cashReceived - amount;
        return this.change;
    }

    //confirmPayment(): ยืนยันว่าชำระเงินสำเร็จ
    public void confirmPayment() {
        this.paymentStatus = "ชำระแล้ว";
    }

    //Getter
    public int getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public double getChange() {
        return change;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getCashReceived() {
        return cashReceived;
    }
}

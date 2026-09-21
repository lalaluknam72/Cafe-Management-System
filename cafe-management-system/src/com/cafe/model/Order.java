package com.cafe.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    //Attributes
    private int orderId;
    private String orderDate;
    private String orderType;      // ONLINE หรือ WALK_IN
    private String pickupType;     // DINE_INหรือ TAKEAWAY
    private String pickupDate;
    private String pickupTime;
    private String status;         //รอดำเนินการ, กำลังทำ, เสร็จแล้ว, ยกเลิก
    private double total;

    private List<OrderItem> items; 
    private Payment payment;
    //Methods
    public Order(int orderId, String orderDate, String orderType,
                 String pickupType, String pickupDate, String pickupTime) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.pickupType = pickupType;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.status = "รอดำเนินการ";
        this.total = 0.0;
        this.items = new ArrayList<>();
    }

    // ---------- Methods ตาม Class Diagram ----------

    //addItem(): เพิ่มรายการสินค้าเข้าไปในออเดอร์ แล้วคำนวณยอดรวมใหม่ทันที
    public void addItem(OrderItem item) {
        items.add(item);
        calculateTotal();
    }

    //removeItem(): ลบรายการสินค้าออกจากออเดอร์
    public void removeItem(OrderItem item) {
        items.remove(item);
        calculateTotal();
    }

    //calculateTotal(): รวมยอดเงินทั้งหมดจากทุกรายการในออเดอร์
    public double calculateTotal() {
        double sum = 0.0;
        for (OrderItem item : items) {
            sum += item.calculateSubtotal();
        }
        this.total = sum;
        return this.total;
    }

    //confirmOrder(): ยืนยันออเดอร์
    public void confirmOrder() {
        this.status = "ยืนยันแล้ว";
    }

    //updateStatus(): พนักงานอัปเดตสถานะออเดอร์
    public void updateStatus(String status) {
        this.status = status;
    }

    //getOrderDetails(): สรุปรายละเอียดออเดอร์เป็นข้อความ
    public String getOrderDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("ออเดอร์ #").append(orderId).append(" (").append(status).append(")\n");
        for (OrderItem item : items) {
            sb.append("  - ").append(item).append("\n");
        }
        sb.append("รวมทั้งหมด: ").append(total).append(" บาท");
        return sb.toString();
    }

    //setPayment(): ผูก Payment
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    //Getter
    public int getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public double getTotal() {
        return total;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Payment getPayment() {
        return payment;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getPickupType() {
        return pickupType;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }
}

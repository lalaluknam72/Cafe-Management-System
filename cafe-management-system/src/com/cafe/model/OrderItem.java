package com.cafe.model;

public class OrderItem {

    //Attributes
    private Product product;
    private int quantity;
    private double unitPrice;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    //calculateSubtotal(): คำนวณยอดรวมของรายการนี้ = ราคาต่อหน่วย * จำนวน
    public double calculateSubtotal() {
        return unitPrice * quantity;
    }

    //updateQuantity(): เปลี่ยนจำนวนที่สั่ง
    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    //Getter
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return String.format("%s x%d = %.2f บาท", product.getName(), quantity, calculateSubtotal());
    }
}

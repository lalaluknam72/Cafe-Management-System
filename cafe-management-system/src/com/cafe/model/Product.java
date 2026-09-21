package com.cafe.model;

public class Product {

    //Attributes
    private int productId;
    private String name;
    private String category;
    private double price;
    private String description;
    private String image;
    private boolean available;
    //Methods
    public Product(int productId, String name, String category, double price,
                   String description, String image, boolean available) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.image = image;
        this.available = available;
    }

    //getDetails(): คืนค่ารายละเอียดสินค้าเป็นข้อความ
    public String getDetails() {
        return String.format("[%d] %s (%s) - %.2f บาท - %s",
                productId, name, category, price, available ? "มีขาย" : "ของหมด");
    }

    //updateProduct(): แก้ไขข้อมูลสินค้า
    public void updateProduct(String name, String category, String description, String image) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.image = image;
    }

    //updatePrice(): เปลี่ยนราคาสินค้า
    public void updatePrice(double price) {
        this.price = price;
    }

    //setAvailability(): เปิด/ปิดการขายสินค้านี้
    public void setAvailability(boolean status) {
        this.available = status;
    }

    //Getter
    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}

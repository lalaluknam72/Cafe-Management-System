package com.cafe.model;

public class Ingredient {

    //Attributes
    private int ingredientId;
    private String name;
    private String unit;
    private double quantity;
    private double minimumStock;
    //Medthods
    public Ingredient(int ingredientId, String name, String unit, double quantity, double minimumStock) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
    }

    //addStock(): เพิ่มจำนวนวัตถุดิบ
    public void addStock(double amount) {
        this.quantity += amount;
    }

    //reduceStock(): ลดจำนวนวัตถุดิบ
    public void reduceStock(double amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }

    //updateStock(): ตั้งค่าจำนวนวัตถุดิบใหม่ตรงๆ
    public void updateStock(double amount) {
        this.quantity = amount;
    }

    //checkLowStock(): เช็กว่าวัตถุดิบนี้ใกล้หมดหรือยัง
    public boolean checkLowStock() {
        return this.quantity < this.minimumStock;
    }

    //Getter
    public int getIngredientId() {
        return ingredientId;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public double getMinimumStock() {
        return minimumStock;
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f %s (ขั้นต่ำ %.2f)%s",
                name, quantity, unit, minimumStock, checkLowStock() ? "ใกล้หมด" : "");
    }
}

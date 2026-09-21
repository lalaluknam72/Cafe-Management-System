package com.cafe.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    //Attributes
    private List<Ingredient> ingredients;

    public Inventory() {
        this.ingredients = new ArrayList<>();
    }

    //addIngredient(): เพิ่มวัตถุดิบชนิดใหม่เข้าคลัง
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    //removeIngredient(): ลบวัตถุดิบออกจากคลัง
    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    //updateIngredient(): แทนที่ข้อมูลวัตถุดิบเดิมด้วยข้อมูลใหม่
    public void updateIngredient(Ingredient ingredient) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getIngredientId() == ingredient.getIngredientId()) {
                ingredients.set(i, ingredient);
                return;
            }
        }
    }

    //getIngredient(): ค้นหาวัตถุดิบด้วย id
    public Ingredient getIngredient(int id) {
        for (Ingredient ing : ingredients) {
            if (ing.getIngredientId() == id) {
                return ing;
            }
        }
        return null;
    }

    //checkLowStock(): คืนรายการวัตถุดิบทั้งหมดที่ใกล้หมด
    public List<Ingredient> checkLowStock() {
        List<Ingredient> lowStockList = new ArrayList<>();
        for (Ingredient ing : ingredients) {
            if (ing.checkLowStock()) {
                lowStockList.add(ing);
            }
        }
        return lowStockList;
    }

    //Getter
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}

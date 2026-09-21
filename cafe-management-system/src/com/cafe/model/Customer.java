package com.cafe.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {

    //Attributes
    private List<Order> orders;
    private List<Reservation> reservations;
    //Medthods
    public Customer(int userId, String name, String phone, String username, String password) {
        super(userId, name, phone, username, password); // เรียก constructor ของ User
        this.orders = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    //viewMenu(): แสดงเมนูทั้งหมดที่เปิดขายอยู่
    public void viewMenu(List<Product> allProducts) {
        System.out.println("=== เมนูร้าน ===");
        for (Product p : allProducts) {
            if (p.isAvailable()) {
                System.out.println(p.getDetails());
            }
        }
    }

    //placeOrder(): สั่งซื้อสินค้า
    public Order placeOrder(Order order) {
        orders.add(order);
        order.confirmOrder();
        return order;
    }

    //makeReservation(): จองโต๊ะ
    public Reservation makeReservation(Reservation reservation, Table table) {
        reservation.createReservation(table);
        reservations.add(reservation);
        return reservation;
    }

    //viewOrderStatus(): เช็กสถานะออเดอร์ล่าสุด 
    public void viewOrderStatus(Order order) {
        System.out.println(order.getOrderDetails());
    }

    //viewOrderHistory(): แสดงประวัติการสั่งซื้อ
    public void viewOrderHistory() {
        System.out.println("=== ประวัติการสั่งซื้อของ " + name + " ===");
        for (Order o : orders) {
            System.out.println(o.getOrderDetails());
        }
    }

    //Getter
    public List<Order> getOrders() {
        return orders;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}

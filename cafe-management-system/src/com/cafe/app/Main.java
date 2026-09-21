package com.cafe.app;

import com.cafe.model.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1) เตรียมเมนูสินค้า (Product) - ส่วนนี้แก้ไข/เพิ่มเมนูเองได้เลย
        List<Product> menu = setupMenu();

        // 2) เตรียมคลังวัตถุดิบ
        Inventory inventory = new Inventory();
        inventory.addIngredient(new Ingredient(1, "เมล็ดกาแฟ", "กรัม", 500, 200));
        inventory.addIngredient(new Ingredient(2, "นมสด", "มล.", 1000, 300));

        // 3)ลูกค้า 1 คน และพนักงาน 1 คน
        Customer customer = new Customer(1, "คุณสมชาย", "0891234567", "somchai", "1234");
        Staff staff = new Staff(1, "พนักงาน", "0899999999", "staff", "1234");

        // 4) ลูกค้าดูเมนู (Use Case: ดูเมนู)
        customer.viewMenu(menu);

        //5) ลูกค้าสั่งเครื่องดื่ม(สั่งผ่านระบบ)
        Order order = new Order(1001, "2026-09-21", "ONLINE", "TAKEAWAY", "2026-09-21", "10:30");
        order.addItem(new OrderItem(menu.get(0), 2)); // สั่งเมนูแรก 2 แก้ว
        order.addItem(new OrderItem(menu.get(2), 1)); // สั่งเมนูที่ 3 (ของหวาน) 1 ชิ้น
        customer.placeOrder(order);

        System.out.println();
        customer.viewOrderStatus(order);

        //6) พนักงานรับออเดอร์และเตรียมเครื่องดื่ม
        System.out.println();
        staff.receiveOrder(order);
        staff.prepareDrink(order);
        staff.manageOrders(order, "เสร็จแล้ว");

        // 7) คิดเงิน/รับเงิน
        Payment payment = new Payment(5001, order.getTotal(), "เงินสด");
        Income income = staff.processPayment(order, payment, 200, 9001, "2026-09-21");
        System.out.println();
        System.out.printf("ยอดที่ต้องจ่าย: %.2f บาท | รับเงิน: 200.00 บาท | เงินทอน: %.2f บาท%n",
                payment.getAmount(), payment.getChange());

        // 8)จองโต๊ะ
        Table table1 = new Table(1, 4);
        Reservation reservation = new Reservation(2001, "2026-09-22", "18:00", 3, "นั่งที่ร้าน");
        customer.makeReservation(reservation, table1);
        System.out.println();
        System.out.println("สถานะการจองโต๊ะ: " + reservation.getStatus() + " | โต๊ะ: " + table1.getTableId());
        staff.manageReservations(reservation, true); // พนักงานยืนยันการจอง

        // 9) ดูประวัติการสั่งซื้อของลูกค้า
        System.out.println();
        customer.viewOrderHistory();

        // 10) ดูรายงานรายรับ-รายจ่าย
        List<Income> incomeList = new ArrayList<>();
        incomeList.add(income);
        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(new Expense(8001, "ซื้อเมล็ดกาแฟเพิ่ม", 1500, "2026-09-20", "ค่าวัตถุดิบ"));

        System.out.println();
        staff.viewReports(incomeList, expenseList);

        //11) เช็กวัตถุดิบ
        System.out.println();
        System.out.println("=== วัตถุดิบใกล้หมด ===");
        for (Ingredient ing : inventory.checkLowStock()) {
            System.out.println(ing);
        }
    }

    private static List<Product> setupMenu() {
        List<Product> menu = new ArrayList<>();

        //COFFEE (กาแฟ)
        menu.add(new Product(1, "เอสเพรสโซ่ (HOT)", "COFFEE", 30.0, "เอสเพรสโซ่ร้อน", "https://www.nespresso.com/ecom/medias/sys_master/public/46591974866974/shutterstock-2524508273-1200x800.jpg", true));
        menu.add(new Product(2, "เอสเพรสโซ่ (ICED)", "COFFEE", 40.0, "เอสเพรสโซ่เย็น", "https://img.wongnai.com/p/1920x0/2021/09/23/49bc4c3b4fca45eaae9cfdc53c1c945a.jpg", true));
        menu.add(new Product(3, "เอสเพรสโซ่ (FRAPPE)", "COFFEE", 45.0, "เอสเพรสโซ่ปั่น", "https://cdn.shopify.com/s/files/1/0778/0591/2351/files/benefit_coffee_frappe_1024x1024.jpg?v=1725614702", true));
        menu.add(new Product(4, "คาปูชิโน่ (ICED)", "COFFEE", 40.0, "คาปูชิโน่เย็น", "https://www.nestleprofessional.co.th/sites/default/files/styles/np_recipe_detail/public/2023-04/BANNER_%E0%B8%84%E0%B8%B2%E0%B8%9B%E0%B8%B9%E0%B8%8A%E0%B8%B4%E0%B9%82%E0%B8%99%E0%B9%88%20%E0%B9%80%E0%B8%A2%E0%B9%87%E0%B8%99%20540x400%20px.jpg?itok=_jPfaN4n", true));
        menu.add(new Product(5, "ลาเต้ (ICED)", "COFFEE", 40.0, "ลาเต้เย็น", "https://www.nespresso.com/ecom/medias/sys_master/public/47204036706334/shutterstock-2170419379-1024x683.jpg", true));

        //TEA (ชา)
        menu.add(new Product(6, "ชาเขียว (ICED)", "TEA", 35.0, "ชาเขียวเย็น", "https://s.isanook.com/wo/0/ud/37/188993/4.jpg?ip/resize/w728/q80/jpg", true));
        menu.add(new Product(7, "ชาไทย (ICED)", "TEA", 35.0, "ชาไทยเย็น", "https://benothailand.com/cdn/shop/articles/Iced_thai_milk_tea_in_glass_9c10e268-b2bd-4406-a25f-e27d39f03d92.jpg?v=1780021592", true));

        //TOPPING
        menu.add(new Product(8, "บราวนี่", "DESSERT", 65.0, "บราวนี่ช็อกโกแลตเข้มข้น", "images/brownie.jpg", true));
        menu.add(new Product(9, "เพิ่มวิปครีม", "TOPPING", 10.0, "วิปครีม", "https://tecnogasthai.com/wp-content/uploads/2023/09/1.-%E0%B8%A7%E0%B8%B4%E0%B8%9B%E0%B8%9B%E0%B8%B4%E0%B9%89%E0%B8%87%E0%B8%84%E0%B8%A3%E0%B8%B5%E0%B8%A1%E0%B9%81%E0%B8%A5%E0%B8%B0%E0%B8%A7%E0%B8%B4%E0%B8%9B%E0%B8%84%E0%B8%A3%E0%B8%B5%E0%B8%A1%E0%B9%80%E0%B8%AB%E0%B8%A1%E0%B8%B7%E0%B8%AD%E0%B8%99%E0%B8%81%E0%B8%B1%E0%B8%99%E0%B8%A1%E0%B8%B1%E0%B9%89%E0%B8%A2_.png", true));

        return menu;
    }
}

package com.cafe.model;

public class Table {

    //Attributes
    private int tableId;
    private int capacity;   // จำนวนที่นั่งสูงสุด
    private String status;  // เช่น "ว่าง", "ไม่ว่าง", "จองแล้ว"

    public Table(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.status = "ว่าง";
    }

    //Methods

    //checkAvailability(): เช็กว่าโต๊ะนี้ว่างอยู่หรือไม่
    public boolean checkAvailability() {
        return "ว่าง".equals(status);
    }

    //reserveTable(): จองโต๊ะนี้
    public void reserveTable() {
        this.status = "จองแล้ว";
    }

    //updateStatus(): เปลี่ยนสถานะโต๊ะ
    public void updateStatus(String status) {
        this.status = status;
    }

    //Getter
    public int getTableId() {
        return tableId;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getStatus() {
        return status;
    }
}

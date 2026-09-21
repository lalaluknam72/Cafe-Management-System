package com.cafe.model;

public class Reservation {

    //Attributes
    private int reservationId;
    private String date;
    private String time;
    private int numberOfPeople;
    private String serviceType;
    private String status;

    private Table table;
    //Methods
    public Reservation(int reservationId, String date, String time, int numberOfPeople, String serviceType) {
        this.reservationId = reservationId;
        this.date = date;
        this.time = time;
        this.numberOfPeople = numberOfPeople;
        this.serviceType = serviceType;
        this.status = "รอยืนยัน";
    }

    //createReservation(): สร้างการจอง
    public boolean createReservation(Table table) {
        if (table.checkAvailability() && table.getCapacity() >= numberOfPeople) {
            this.table = table;
            table.reserveTable();
            return true;
        }
        return false;
    }

    //confirmReservation(): ยืนยันการจอง
    public void confirmReservation() {
        this.status = "ยืนยันแล้ว";
    }

    //cancelReservation(): ยกเลิกการจอง
    public void cancelReservation() {
        this.status = "ยกเลิก";
        if (table != null) {
            table.updateStatus("ว่าง");
        }
    }

    //checkAvailability(): เช็กว่าการจองนี้ยังใช้งานได้อยู่มั้ย
    public boolean checkAvailability() {
        return !"ยกเลิก".equals(status);
    }

    //Getter
    public int getReservationId() {
        return reservationId;
    }

    public String getStatus() {
        return status;
    }

    public Table getTable() {
        return table;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public String getServiceType() {
        return serviceType;
    }
}

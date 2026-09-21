package com.cafe.model;

public abstract class User {

    //Attributes
    protected int userId;
    protected String name;
    protected String phone;
    protected String username;
    protected String password;

    public User(int userId, String name, String phone, String username, String password) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    //Methods

    //login(): ตรวจสอบ username/password
    public boolean login(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    //logout(): ออกจากระบบ
    public void logout() {
        System.out.println(name + " ออกจากระบบแล้ว");
    }

    //updateProfile(): แก้ไขข้อมูลส่วนตัว
    public void updateProfile(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    //Getter
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }
}

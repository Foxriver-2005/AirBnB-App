package com.lelei.airbnb.Models;

public class AirBnBLogIn {
    String phone, password,name;
    AirBnBLogIn()
    {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AirBnBLogIn(String phone, String password, String name) {
        this.phone = phone;
        this.password = password;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
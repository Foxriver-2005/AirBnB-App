package com.lelei.airbnb.Models;

public class SaveAndFav {
    String name;
    SaveAndFav()
    {

    }
    public SaveAndFav(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
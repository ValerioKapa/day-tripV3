package com.example.day_tripv3;


public class Costumer {
    String name, sirname, hotel, id;

    public Costumer(){}

    public Costumer(String name, String sirname, String hotel, String id) {
        this.name = name;
        this.sirname = sirname;
        this.hotel = hotel;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSirname() {
        return sirname;
    }

    public void setSirname(String sirname) {
        this.sirname = sirname;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }
}

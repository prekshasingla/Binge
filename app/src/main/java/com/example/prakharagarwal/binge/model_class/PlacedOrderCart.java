package com.example.prakharagarwal.binge.model_class;

import com.example.prakharagarwal.binge.Menu.Menu;

import java.util.HashMap;

public class PlacedOrderCart {

    public HashMap<String,String> dishes;
    public float cart_value;
    public String userId;
    public String status="recived";
    public String payment="notPaid";
    public double location_lat;
    public double location_long;
    public String id;

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String tableNo;

    public HashMap<String, String> getDishes() {
        return dishes;
    }

    public void setDishes(HashMap<String, String> dishes) {
        this.dishes = dishes;
    }

    public float getCart_value() {
        return cart_value;
    }

    public void setCart_value(float cart_value) {
        this.cart_value = cart_value;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(double location_lat) {
        this.location_lat = location_lat;
    }

    public double getLocation_long() {
        return location_long;
    }

    public void setLocation_long(double location_long) {
        this.location_long = location_long;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.example.prakharagarwal.binge.model_class;

import com.example.prakharagarwal.binge.Menu.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacedOrderCart {

    public List<HashMap<String,String>> dishes=new ArrayList<>();
    public float cart_value;
    public String userId;
    public String status="pending";
    public String payment="notPaid";
    public double location_lat;
    public double location_long;
    public String id;
    String timestamp="";
    String ordertype="";

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String tableNo;

    public List<HashMap<String,String>> getDishes() {
        return dishes;
    }

    public void setDishes(HashMap<String, String> dishes) {
        this.dishes.add(dishes);
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

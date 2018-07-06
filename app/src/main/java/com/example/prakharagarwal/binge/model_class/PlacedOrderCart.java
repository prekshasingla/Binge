package com.example.prakharagarwal.binge.model_class;

import com.example.prakharagarwal.binge.Menu.Menu;

import java.util.HashMap;

public class PlacedOrderCart {

    private HashMap<String,String> OrderDishes;
    private int totalCost;
    private String userID;
    private String gst;

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public HashMap<String, String> getDishesHashMap() {
        return OrderDishes;
    }

    public void setDishesHashMap(HashMap<String, String> dishesHashMap) {
        this.OrderDishes = dishesHashMap;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

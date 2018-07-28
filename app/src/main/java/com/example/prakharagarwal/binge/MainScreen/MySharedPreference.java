package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

   public MySharedPreference(Context context) {
        preferences = context.getSharedPreferences("AppData", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void savedmapactivity_set_latitude_longitude_flag(Double latitude, Double longitude) {
        editor.putFloat("latitude", Float.parseFloat(new Double(latitude).toString()));
        editor.putFloat("longitude", Float.parseFloat(new Double(longitude).toString()));
        editor.commit();
    }

    public void savedmapactivity_set_flag(Boolean flag)
    {
        editor.putBoolean("flagopen", flag);
        editor.commit();
    }

    public void savedmapactivity_set_orderID_restaurant_id(String orderID, String restaurantID) {
        editor.putString("orderId", orderID);
        editor.putString("resturant_id", restaurantID);
        editor.commit();
    }

    public float savedmapactivity_get_latitude() {
        return preferences.getFloat("latitude", 0);
    }

    public float savedmapactivity_get_longitude() {
        return preferences.getFloat("longitude", 0);
    }

    public Boolean savedmapactivity_get_flag() {
        return preferences.getBoolean("flagopen", false);
    }

    public String savedmapactivity_get_orderID() {
        return preferences.getString("orderId", "");
    }

    public String savedmapactivity_restaurantID() {
        return preferences.getString("resturant_id", "");
    }

}

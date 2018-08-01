package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreference {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

   public MySharedPreference(Context context) {
        preferences = context.getSharedPreferences("AppData", Context.MODE_PRIVATE);
        editor = preferences.edit();
        this.context=context;
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

    public void set_insideorderpayment(boolean payment)
    {
        editor.putBoolean("insidepayment",payment);
        editor.commit();
    }

    public boolean get_insideorderpayment()
    {
        return preferences.getBoolean("insidepayment",false);
    }

    public void set_insideorderID(String orderID)
    {
        editor.putString("insideorderID",orderID);
        editor.commit();
    }
    public String get_insideorderID()
    {
        return preferences.getString("insideorderID",null);
    }

    public void set_inside_order_restaurant_id(String id)
    {
        editor.putString("inside_restaurant_id",id).commit();
    }

    public String get_inside_order_restaurant_id()
    {
        return preferences.getString("inside_restaurant_id",null);
    }

    public  void set_saveplacedordermap(String map)
    {
        editor.putString("placedorderhashmap",map);
        editor.commit();
    }

    public String get_saveplacedordermap()
    {
        return preferences.getString("placedorderhashmap",null);
    }

    public void set_menu_image(String url)
    {
        editor.putString("photo",url);
        editor.commit();
    }

    public String get_menu_image()
    {
        return preferences.getString("photo",null);
    }
}

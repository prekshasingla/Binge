package com.example.prakharagarwal.binge.Menu;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by prekshasingla on 11/07/17.
 */
public class Menu implements Serializable{

    String video_url;
    String name;
    String desc;
    String price;
    Long veg;
    Long has_video;
    int cart_quantity;
    String restaurantName;
  // String poster_url="https://firebasestorage.googleapis.com/v0/b/bingetesting.appspot.com/o/main_screen%2FMinistry%20of%20Beer%2Fchicken%20quiche.jpg?alt=media&token=b9504b59-0330-41ec-b7e5-0a0ae323eca7";
    String poster_url="https://www.soomska.com/wp-content/uploads/2017/12/5.jpg";
    String category;
    String restaurant_id;
    Long course_meal=0l;
    int totalcartItem=0;
    String latitude;
    String longitude;
    long gst=5;

    public long getGst() {
        return gst;
    }

    public void setGst(long gst) {
        this.gst = gst;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getTotalcartItem() {
        return totalcartItem;
    }

    public void setTotalcartItem(int totalcartItem) {
        this.totalcartItem = totalcartItem;
    }

    public Long getCourse_meal() {
        return course_meal;
    }

    public void setCourse_meal(Long course_meal) {
        this.course_meal = course_meal;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public int getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(int cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getHas_video() {
        return has_video;
    }

    public void setHas_video(Long has_video) {
        this.has_video = has_video;
    }

    public Long getVeg() {
        return veg;
    }

    public void setVeg(Long veg) {
        this.veg = veg;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

}
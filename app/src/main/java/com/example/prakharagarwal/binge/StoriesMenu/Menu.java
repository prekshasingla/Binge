package com.example.prakharagarwal.binge.StoriesMenu;

/**
 * Created by prekshasingla on 11/07/17.
 */
public class Menu {

    String video_url;
    String name;
    String desc;
    String price;
    Long veg;
    Long has_video;
    int cart_quantity;


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

}
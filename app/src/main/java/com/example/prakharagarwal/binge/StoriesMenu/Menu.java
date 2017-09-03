package com.example.prakharagarwal.binge.StoriesMenu;

/**
 * Created by prekshasingla on 11/07/17.
 */
public class Menu {

    String image;
    String name;
    String description;
    String price;
    boolean veg;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Menu(){
    image=null;
        name=null;
        description=null;
        price=null;
        veg=false;
    }
    public Menu(String image){
        this.image=image;
    }

    public  Menu(String name, String description, String price, boolean veg){
        this.name=name;
        this.description=description;
        this.price=price;
        this.veg=veg;
        this.image=null;
    }
}

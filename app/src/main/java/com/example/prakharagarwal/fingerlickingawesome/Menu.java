package com.example.prakharagarwal.fingerlickingawesome;

/**
 * Created by prekshasingla on 11/07/17.
 */
public class Menu {

    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Menu(){
    image=null;
    }
    public Menu(String image){
        this.image=image;
    }
}

package com.example.prakharagarwal.binge.cart;

import java.io.Serializable;

/**
 * Created by prakharagarwal on 07/03/18.
 */

public class CartItem implements Serializable{
    private String name;
    private String price;
    private String qty;
    private Long veg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Long getVeg() {
        return veg;
    }

    public void setVeg(Long veg) {
        this.veg = veg;
    }
}

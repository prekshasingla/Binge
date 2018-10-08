package com.example.prakharagarwal.binge.MainScreen;

/**
 * Created by prakharagarwal on 25/05/18.
 */

public class Category1 {
    private String category_id;
    private String category_name;
    private String logo_url;
    private Long item=0l;

    public Long getItem() {
        return item;
    }

    public void setItem(Long item) {
        this.item = item;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }
}

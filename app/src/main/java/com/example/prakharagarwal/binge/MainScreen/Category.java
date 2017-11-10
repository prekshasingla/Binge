package com.example.prakharagarwal.binge.MainScreen;

import com.example.prakharagarwal.binge.StoriesMenu.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prekshasingla on 10/11/17.
 */

public class Category {
    String category;
    List<Menu> categoryMenu;

    Category(){
        categoryMenu=new ArrayList<Menu>();
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Menu> getCategoryMenu() {
        return categoryMenu;
    }

    public void setCategoryMenu(List<Menu> categoryMenu) {
        this.categoryMenu = categoryMenu;
    }
    public void addCategoryMenu(Menu menu){
        categoryMenu.add(menu);
    }
}

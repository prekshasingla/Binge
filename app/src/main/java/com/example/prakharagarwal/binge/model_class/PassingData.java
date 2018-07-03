package com.example.prakharagarwal.binge.model_class;

import com.example.prakharagarwal.binge.Menu.Menu;
import java.util.List;

public class PassingData {
    public static List<Menu> getMenuList() {
        return menuList;
    }

    public static void setMenuList(List<Menu> menuList) {
        PassingData.menuList = menuList;
    }

    public static List<Menu> menuList;

    public static Menu menu;

    public static Menu getMenu() {
        return menu;
    }

    public static void setMenu(Menu menu) {
        PassingData.menu = menu;
    }

}

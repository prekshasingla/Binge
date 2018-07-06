package com.example.prakharagarwal.binge.model_class;

import com.example.prakharagarwal.binge.Menu.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PassingCartItem {

    public static List<Menu> menuArrayList = new ArrayList<>();
    public static List<Integer> integerArrayList = new ArrayList<>();
    public static HashMap<Menu, Integer> menuIntegerHashMap = new HashMap<>();
    public static HashMap<Menu,Integer> placed_order_hashmap=new HashMap<>();

    public static void addmenu(Menu menu, Integer totalitem) {
        menuIntegerHashMap.put(menu,totalitem);
    }

    public static HashMap<Menu, Integer> getMenuHashmap() {
        return menuIntegerHashMap;
    }

    public static void addMenuArrayList(Menu menus, Integer item) {

        if(menuArrayList.contains(menus))
        {
            menuArrayList.add(menuArrayList.indexOf(menus),menus);
            integerArrayList.add(menuArrayList.indexOf(menus),item);
        }
        else
        {
            menuArrayList.add(menus);
            integerArrayList.add(item);
        }
    }

    public static List<Menu> getMenuArrayList() {
        return menuArrayList;
    }

    public static List<Integer> getIntegerArrayList() {
        return integerArrayList;
    }

}

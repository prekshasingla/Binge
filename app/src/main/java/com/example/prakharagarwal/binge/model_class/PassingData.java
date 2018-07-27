package com.example.prakharagarwal.binge.model_class;

import com.example.prakharagarwal.binge.Menu.Menu;
import java.util.List;

public class PassingData {

    public static String resturant_Id;

   public static Double latitude;
   public static Double longitude;
   public static String tableNo;

    public static String getTableNo() {
        return tableNo;
    }

    public static void setTableNo(String tableNo) {
        PassingData.tableNo = tableNo;
    }

    public static Double getLatitude() {
        return latitude;
    }

    public static void setLatitude(Double latitude) {
        PassingData.latitude = latitude;
    }

    public static Double getLongitude() {
        return longitude;
    }

    public static void setLongitude(Double longitude) {
        PassingData.longitude = longitude;
    }

    public static String resturantName;

    public static String getResturantName() {
        return resturantName;
    }

    public static void setResturantName(String resturantName) {
        PassingData.resturantName = resturantName;
    }

    public static String getResturant_Id() {
        return resturant_Id;
    }

    public static void setResturant_Id(String resturant_Id) {
        PassingData.resturant_Id = resturant_Id;
    }

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

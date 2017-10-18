package com.example.prakharagarwal.binge.MainScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by prakharagarwal on 25/06/17.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//
//    ArrayList<Fragment> fragments = new ArrayList<>();
//    ArrayList<String> tabTitles = new ArrayList<>();

    int tabCount;
    public ViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Tab1 tab1 = new Tab1();
                return tab1;
            case 1:
                Tab2 tab2 = new Tab2();
                return tab2;
            case 2:
                Tab3 tab3 = new Tab3();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}



//    public void addFragment(Fragment fragment, String title) {
//        this.fragments.add(fragment);
//
//        this.tabTitles.add(title);
//    }
//
//    public ViewPagerAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return fragments.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return fragments.size();
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabTitles.get(position);
//    }
//    }


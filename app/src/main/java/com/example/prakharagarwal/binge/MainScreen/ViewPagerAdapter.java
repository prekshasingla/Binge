package com.example.prakharagarwal.binge.MainScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prekshasingla on 22/10/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();

    public void addFragment(Fragment fragment,String title){
        this.mFragmentList.add(fragment);
        this.tabTitles.add(title);
    }

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    public void addFragment(Fragment fragment) {

        mFragmentList.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

}
package com.example.prakharagarwal.binge.MainScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;

public class RestaurantActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static final int MENU_ITEM_ID_FIRST =0;
    private int MENU_ITEM_ID_LAST =2;
    private ViewPager viewPager;
    MenuItem prevMenuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//            viewPager.setCurrentItem(0);
//            return true;

            switch (item.getItemId()) {

                case 0 :
                    mTextMessage.setText(R.string.title_home);
                    viewPager.setCurrentItem(0);
                    return true;
                case 1:
                    mTextMessage.setText(R.string.title_dashboard);
                    viewPager.setCurrentItem(0);
                    return true;
                case 2:
                    viewPager.setCurrentItem(0);
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        mTextMessage = (TextView) findViewById(R.id.message);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainActivityFragment());
        adapter.addFragment(new MainActivityFragment());
        viewPager.setAdapter(adapter);
    }


}

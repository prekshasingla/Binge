package com.example.prakharagarwal.binge.MainScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;

import java.util.List;

public class RestaurantActivity extends AppCompatActivity {


    private ViewPager viewPager;

    TextView textViewLocation;
    private ImageView searchIcon;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //String restaurantID = getIntent().getStringExtra("restaurantID");
        //String restaurantName = getIntent().getStringExtra("restaurantName");
        textViewLocation = (TextView) findViewById(R.id.user_location);
       // textViewLocation.setText(restaurantName);
       // textViewLocation.setText("some text");

        //Initializing the tablayout
        //tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        searchIcon = (ImageView) findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        //Adding the tabs using addTab() method
//        tabLayout.addTab(tabLayout.newTab().setText("Fine & Dining"));
//        tabLayout.addTab(tabLayout.newTab().setText("Cafes & more"));
//        tabLayout.addTab(tabLayout.newTab().setText("Drinks & Nighlife"));

        //tabLayout.setBackground(R.drawable.primary_btn);


        String category_id=getIntent().getStringExtra("category");
        textViewLocation.setText(category_id.substring(0,1).toUpperCase()+category_id.substring(1).toLowerCase());
       // MainActivityFragment.FoodList foodList= (MainActivityFragment.FoodList) getIntent().getSerializableExtra("mfood");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(RestaurantActivityFragment.newInstance(category_id));
        viewPager.setAdapter(viewPagerAdapter);

    }

}

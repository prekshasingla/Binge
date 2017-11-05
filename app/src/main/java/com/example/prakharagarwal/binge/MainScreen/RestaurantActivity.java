package com.example.prakharagarwal.binge.MainScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.StoriesMenu.MenuAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    private RecyclerView bottomRecycler;
    List<com.example.prakharagarwal.binge.StoriesMenu.Menu> menus;
    private RecyclerView menuRecyler;
    private MenuAdapter menuAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        bottomRecycler=(RecyclerView)findViewById(R.id.restaurant_bottom_recycler);
        RestaurantBottomAdapter restaurantBottomAdapter= new RestaurantBottomAdapter(null,this);
        bottomRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        bottomRecycler.setAdapter(restaurantBottomAdapter);

        menus = new ArrayList<>();

        menuRecyler = (RecyclerView) findViewById(R.id.menu_recycler);
        menuAdapter = new MenuAdapter(this, menus);
        menuRecyler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        try {
            menuRecyler.setAdapter(menuAdapter);
        } catch (NoClassDefFoundError e) {

        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("menu");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getData(DataSnapshot dataSnapshot) {

        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
            if (child1.getKey().equals("too_indian_delhi"))
                for (DataSnapshot child2 : child1.getChildren()) {
                    if (child2.getKey() != null) {
                        for (DataSnapshot child3 : child2.getChildren()) {
                            com.example.prakharagarwal.binge.StoriesMenu.Menu menu = child3.getValue(com.example.prakharagarwal.binge.StoriesMenu.Menu.class);
                            menus.add(menu);
                        }
                    }
                }
        }
        addAllMenus(menus);
    }
    public void addAllMenus(List<com.example.prakharagarwal.binge.StoriesMenu.Menu> menus) {
        menuAdapter.addAll(menus);
        menuAdapter.notifyDataSetChanged();
//        checkIfEmpty();


    }

}

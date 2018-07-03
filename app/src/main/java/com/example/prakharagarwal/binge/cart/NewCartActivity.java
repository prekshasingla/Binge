package com.example.prakharagarwal.binge.cart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.model_class.PassingCartItem;

import java.util.List;

public class NewCartActivity extends AppCompatActivity {


    List<Menu> menuList;
    List<Integer> integerList;
    RecyclerView recyclerView;
    RecyclerViewCartAdapter adapter;
    TextView resturant_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cart);

        recyclerView = findViewById(R.id.recyclerview_cart);
        menuList = PassingCartItem.getMenuArrayList();
        integerList = PassingCartItem.getIntegerArrayList();
        resturant_name=findViewById(R.id.resturatant_cart_name);
        resturant_name.setText(menuList.get(0).getRestaurantName());
        adapter = new RecyclerViewCartAdapter(menuList, integerList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

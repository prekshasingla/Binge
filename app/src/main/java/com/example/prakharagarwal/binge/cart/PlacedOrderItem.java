package com.example.prakharagarwal.binge.cart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.model_class.PassingCartItem;
import com.example.prakharagarwal.binge.model_class.PlacedOrderCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlacedOrderItem extends AppCompatActivity {

    private RecyclerView placed_order;
    private RecyclerViewPlacedOrderAdapter placedOrderAdapter;
    static TextView totalpricetext;
    static TextView gstpricetext;
    static TextView paypricetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order_item);

        placed_order = findViewById(R.id.recyclerview_cart_placedorder);
        totalpricetext = findViewById(R.id.total_item_price);
        gstpricetext = findViewById(R.id.gst_price);
        paypricetext = findViewById(R.id.to_pay_price);

        List<Menu> localmenu = new ArrayList<>();
        List<Integer> localinterger = new ArrayList<>();
        int totalprice = 0;
        int gstpri = 0;

        for (Map.Entry<Menu, Integer> hashmap : PassingCartItem.placed_order_hashmap.entrySet()) {

            localmenu.add(hashmap.getKey());
            localinterger.add(hashmap.getValue());
            totalprice += Integer.parseInt(hashmap.getKey().getPrice()) * hashmap.getValue();
        }
        gstpri = (totalprice * 4) / 100;

        totalpricetext.setText("₹" + totalprice);
        gstpricetext.setText("₹" + gstpri);
        int total = gstpri + totalprice;
        paypricetext.setText("₹" + total);
//
//        resturant_name.setText(localmenu.get(0).getRestaurant_id());
//        Picasso.with(this).load(localmenu.get(0).getPoster_url()).into(dish_image);
//
        placedOrderAdapter = new RecyclerViewPlacedOrderAdapter(PlacedOrderItem.this, localmenu, localinterger);
        placed_order.setLayoutManager(new LinearLayoutManager(PlacedOrderItem.this));
        placed_order.setAdapter(placedOrderAdapter);
    }
}

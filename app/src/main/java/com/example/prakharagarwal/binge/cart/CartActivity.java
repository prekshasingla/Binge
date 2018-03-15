package com.example.prakharagarwal.binge.cart;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CartActivity extends AppCompatActivity {
    public HashMap<String, CartItem> cartListMap;
    private List<CartItem> cartList;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Float cartValue=0f;
    private TextView cart_total;
    private TextView cart_gst;

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
        setContentView(R.layout.activity_cart);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cartList=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.cart_recycler);
        if (getIntent() != null) {
            cartListMap = (HashMap<String, CartItem>) getIntent().getSerializableExtra("cartListMap");
            makeList();
        }
        CartAdapter cartAdapter = new CartAdapter(this, cartList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(cartAdapter);

        cart_total=(TextView)findViewById(R.id.cart_total);
        cart_total=(TextView)findViewById(R.id.cart_gst);

        calculateBill();
    }

    private void calculateBill() {
        Iterator it = cartListMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            CartItem cartItem=(CartItem) pair.getValue();
            float price= Float.parseFloat(cartItem.getPrice())*Float.parseFloat(cartItem.getQty());
            cartValue+=price;
//            it.remove(); // avoids a ConcurrentModificationException
        }
        updateBillLayout();
    }

    private void updateBillLayout() {
        cart_total.setText(cartValue+"");
        float gst=(cartValue*5)/100;
        cart_gst.setText(gst+"");

    }

    private void makeList() {
        Iterator it = cartListMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            CartItem cartItem=(CartItem) pair.getValue();
            cartList.add(cartItem);
//            it.remove(); // avoids a ConcurrentModificationException
        }
    }

}

package com.example.prakharagarwal.binge.cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    FirebaseFirestore db;
    private String TAG="Cart Activity";
    String orderID;

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

         db = FirebaseFirestore.getInstance();
         findViewById(R.id.cart_pay).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 placeOrderOnFirestore();
             }
         });

    }

    private void placeOrderOnFirestore() {
        // Create a new user with a first and last name
        Map<String, Object> order = new HashMap<>();
        order.put("userId","prakhar");
        order.put("cart_value", cartValue);

        Map<String,Object> dishes=new HashMap<>();
        Iterator it = cartListMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            CartItem cartItem=(CartItem) pair.getValue();
            dishes.put(cartItem.getName(),Integer.parseInt(cartItem.getQty()));
        }
        order.put("dishes",dishes);

// Add a new document with a generated ID
        db.collection("orders/too_indian_delhi/order")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        orderID=documentReference.getId();
                        Intent intent=new Intent(CartActivity.this,CartSuccess.class);
                        intent.putExtra("orderId",orderID);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
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
//        cart_gst.setText(gst+"");

    }

    private void makeList() {
        Iterator it = cartListMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            CartItem cartItem=(CartItem) pair.getValue();
            cartList.add(cartItem);

        }
    }

}

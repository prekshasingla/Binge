package com.example.prakharagarwal.binge.cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.LoginActivity;
import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.model_class.PassingCartItem;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.example.prakharagarwal.binge.model_class.PlacedOrderCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewCartActivity extends AppCompatActivity {


    private List<Menu> menuList;
    private List<Integer> integerList;
    private RecyclerView recyclerView;
    private RecyclerViewCartAdapter adapter;
    private TextView resturant_name;
    private ImageView dish_image;

    private RecyclerView placed_order;
    private RecyclerViewPlacedOrderAdapter placedOrderAdapter;
    private Button placed_order_btn;
    static TextView totalpricetext;
    static TextView gstpricetext;
    static TextView paypricetext;


    TextView backbutton;
    FirebaseFirestore firebaseFirestore;
    private Intent intent;

    TextView placed_Order_text;
    View line1;
    View line2;
    RelativeLayout coupon;
    CardView cardView;

    TextView pending_Item;
    Button payNowbtn;
    TextView add_item_text;
    Boolean preOrderStatus=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cart);

        cardView = findViewById(R.id.cardView);
        recyclerView = findViewById(R.id.recyclerview_cart);
        placed_order = findViewById(R.id.recyclerview_cart_placedorder);
        totalpricetext = findViewById(R.id.total_item_price);
        gstpricetext = findViewById(R.id.gst_price);
        paypricetext = findViewById(R.id.to_pay_price);
        placed_order_btn = findViewById(R.id.placed_order_btn);
        resturant_name = findViewById(R.id.resturatant_cart_name);
        dish_image = findViewById(R.id.resturant_cart_image);
        backbutton = findViewById(R.id.back_btn);
        placed_Order_text = findViewById(R.id.placed_order_text);
        line1 = findViewById(R.id.line_1);
        line2 = findViewById(R.id.line_2);
        coupon = findViewById(R.id.cardView_cart);
        pending_Item = findViewById(R.id.Pending_item);
        payNowbtn=findViewById(R.id.pay_now_btn);
        add_item_text=findViewById(R.id.add_item_text);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        placed_Order_text.setVisibility(View.GONE);
        placed_order.setVisibility(View.GONE);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
        coupon.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        payNowbtn.setVisibility(View.GONE);
        add_item_text.setVisibility(View.GONE);

        menuList = PassingCartItem.getMenuArrayList();
        integerList = PassingCartItem.getIntegerArrayList();

        intent = getIntent();
        if (intent != null)
            if (intent.getStringExtra("flag") != null)
                if (intent.getStringExtra("flag").equals("preOrder"))
                {
                    preOrderStatus=true;
                    placed_order_btn.setText("Pay Now");
                    placed_order_btn.setVisibility(View.GONE);
                    payNowbtn.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.VISIBLE);
                    RecyclerViewCartAdapter.addingToBill();
                }


        resturant_name.setText(intent.getStringExtra("resturant_name"));
        Picasso.with(this).load(intent.getStringExtra("photo_url")).into(dish_image);

        firebaseFirestore = FirebaseFirestore.getInstance();
        adapter = new RecyclerViewCartAdapter(menuList, integerList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        placed_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String uID = prefs.getString("username", null);
                if (uID != null)
                    new MyTask().execute();
                else {
                    startActivity(new Intent(NewCartActivity.this, LoginActivity.class));
                }
            }
        });

        payNowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preOrderStatus) {
                    SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    String uID = prefs.getString("username", null);
                    if (uID != null)
                        new MyTask().execute();
                    else {
                        startActivity(new Intent(NewCartActivity.this, LoginActivity.class));
                    }
                }
                else
                {
                    Toast.makeText(NewCartActivity.this, "open the payment mode", Toast.LENGTH_SHORT).show();
                }
            }

        });

         // RecyclerViewCartAdapter.addingToBill();

        showplacedItem();

        if (!menuList.isEmpty() && !integerList.isEmpty()){}
            //placed_order_btn.setVisibility(View.VISIBLE);

    }

    public void showplacedItem() {
        if (!PassingCartItem.placed_order_hashmap.isEmpty()) {
            //  placed_order_btn.setVisibility(View.INVISIBLE);
            //  pendingitem.setVisibility(View.INVISIBLE);

            placed_Order_text.setVisibility(View.VISIBLE);
            placed_order.setVisibility(View.VISIBLE);
            payNowbtn.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            coupon.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);

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
            resturant_name.setText(localmenu.get(0).getRestaurant_id());
            Picasso.with(this).load(localmenu.get(0).getPoster_url()).into(dish_image);

            placedOrderAdapter = new RecyclerViewPlacedOrderAdapter(NewCartActivity.this, localmenu, localinterger);
            placed_order.setLayoutManager(new LinearLayoutManager(NewCartActivity.this));
            placed_order.setAdapter(placedOrderAdapter);

        }
    }


    class MyTask extends AsyncTask<Void, Void, Void> {
        HashMap<Menu, Integer> cartitem;
        int size;
        List<Menu> menuListsec;
        List<Integer> integerListsec;
        int totalpricesum;
        int gstprice;
        Menu localmenu = new Menu();
        HashMap<String, String> stringHashMap;

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("RISHABH RAWAT", "onbackground method");
            size = menuList.size();
//            menuList.clear();
//            integerList.clear();

            cartitem = PassingCartItem.getMenuHashmap();
            if (cartitem.isEmpty())
                return null;

            //PassingCartItem.menuArrayList.clear();
            //PassingCartItem.integerArrayList.clear();
            menuListsec = new ArrayList<>();
            integerListsec = new ArrayList<>();
            stringHashMap = new HashMap<>();
            // Menu localmenu=new Menu();

            if (PassingCartItem.placed_order_hashmap.isEmpty()) {
                for (Map.Entry<Menu, Integer> entry : PassingCartItem.getMenuHashmap().entrySet()) {
                    if (entry.getValue() != 0) {
                        PassingCartItem.addMenuArrayList(entry.getKey(), entry.getValue());
                        localmenu = entry.getKey();
                        menuListsec.add(entry.getKey());
                        integerListsec.add(entry.getValue());
                        totalpricesum += Integer.parseInt(localmenu.getPrice()) * entry.getValue();
                        // PassingCartItem.placed_order_hashmap.put(entry.getKey(), entry.getValue());
                        PassingCartItem.addplacedorder_item(entry.getKey(), entry.getValue());
                        stringHashMap.put(entry.getKey().getName(), entry.getValue().toString());
                        System.out.println("Rishabh Rawat123456789" + entry.getKey() + " = " + entry.getValue());
                    }
                }
            } else {
                for (Map.Entry<Menu, Integer> entry : PassingCartItem.getMenuHashmap().entrySet()) {
                    if (entry.getValue() != 0) {
                        PassingCartItem.addplacedorder_item(entry.getKey(), entry.getValue());
                    }
                }

                for (Map.Entry<Menu, Integer> entry : PassingCartItem.placed_order_hashmap.entrySet()) {
                    if (entry.getValue() != 0) {
                        PassingCartItem.addMenuArrayList(entry.getKey(), entry.getValue());
                        localmenu = entry.getKey();
                        menuListsec.add(entry.getKey());
                        integerListsec.add(entry.getValue());
                        totalpricesum += Integer.parseInt(localmenu.getPrice()) * entry.getValue();
                        // PassingCartItem.placed_order_hashmap.put(entry.getKey(), entry.getValue());
                        //       PassingCartItem.addplacedorder_item(entry.getKey(), entry.getValue());
                        stringHashMap.put(entry.getKey().getName(), entry.getValue().toString());
                        System.out.println("Rishabh Rawat123456789" + entry.getKey() + " = " + entry.getValue());
                    }
                }


            }
            gstprice = (totalpricesum * 4) / 100;
            Log.d("RISHABH RAWAT", "onbackground method complete");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("RISHABH RAWAT", "onpost method");


            placedOrderAdapter = new RecyclerViewPlacedOrderAdapter(NewCartActivity.this, menuListsec, integerListsec);
            placed_order.setLayoutManager(new LinearLayoutManager(NewCartActivity.this));
            placed_order.setAdapter(placedOrderAdapter);

            if (cartitem != null) {
                int size = menuList.size();
                for (int i = 0; i <= size - 1; i++) {
                    PassingCartItem.addmenu(menuList.get(i), 0);
                    menuList.get(i).setTotalcartItem(0);
                }


                totalpricetext.setText("₹" + totalpricesum);
                gstpricetext.setText("₹" + gstprice);
                int total = gstprice + totalpricesum;
                paypricetext.setText("₹" + total);

                if (intent.getStringExtra("flag") == null) {
                    //Now save the order into the Firebase Firestore
                    PlacedOrderCart placedOrderCart = new PlacedOrderCart();
                    placedOrderCart.setDishes(stringHashMap);
                    placedOrderCart.setCart_value(totalpricesum + gstprice);
                    placedOrderCart.setTableNo(PassingData.getTableNo());
                    //placedOrderCart.setGst("18%");
                    SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    String uID = prefs.getString("username", null);
                    placedOrderCart.setUserId(uID);
                    placedOrderCart.setLocation_lat(0.0);
                    placedOrderCart.setLocation_long(0.0);
                    DocumentReference reference = firebaseFirestore.collection("orders/" + PassingData.getResturant_Id() + "/InsideOrder").document();
                    String orderID = "";

                    if (PassingCartItem.getOrderID() == null) {
                        orderID = reference.getId();
                        PassingCartItem.setOrderID(reference.getId());
                        Log.d("RISHABH ORDER if part", orderID);
                        placedOrderCart.setId(orderID);
                    } else {
                        orderID = PassingCartItem.getOrderID();
                        Log.d("RISHABH ORDER else part", orderID);
                        placedOrderCart.setId(orderID);
                    }

                    firebaseFirestore.collection("orders/" + PassingData.getResturant_Id() + "/InsideOrder").document(orderID).set(placedOrderCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(NewCartActivity.this, "Success to store in FireStore 1", Toast.LENGTH_LONG).show();
                            placed_Order_text.setVisibility(View.VISIBLE);
                            placed_order.setVisibility(View.VISIBLE);
                            line1.setVisibility(View.VISIBLE);
                            line2.setVisibility(View.VISIBLE);
                            coupon.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);
                            pending_Item.setVisibility(View.VISIBLE);
                            placed_order_btn.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            add_item_text.setVisibility(View.VISIBLE);
                            payNowbtn.setVisibility(View.VISIBLE);
//                            NewCartActivity newCartActivity=new NewCartActivity();
//                            newCartActivity.showplacedItem();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewCartActivity.this, "Failed to store in FireStore 1", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {

                    HashMap<String, Object> placedOrderHashmap = new HashMap<>();
                    placedOrderHashmap.put("dishes", stringHashMap);
                    placedOrderHashmap.put("cart_value", totalpricesum + gstprice);
                    SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    String uID = prefs.getString("username", null);
                    placedOrderHashmap.put("userId", uID);
                    placedOrderHashmap.put("location_lat", 0.0);
                    placedOrderHashmap.put("location_long", 0.0);
                    placedOrderHashmap.put("status", "PreOder Recived");
                    Log.d("RISHABH", "RESTURANT IS THE " + PassingData.getResturant_Id());
                    final DocumentReference reference = firebaseFirestore.collection("orders/" + PassingData.getResturant_Id() + "/PreOrder").document();
                    placedOrderHashmap.put("id", reference.getId());
                    firebaseFirestore.collection("orders/" + PassingData.getResturant_Id() + "/PreOrder").document(reference.getId()).set(placedOrderHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(NewCartActivity.this, "Success to store in FireStore", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NewCartActivity.this, CartSuccess.class);
                            intent.putExtra("orderId", reference.getId());
                            intent.putExtra("latitude", PassingData.getLatitude());
                            intent.putExtra("longitude", PassingData.getLongitude());
                            intent.putExtra("resturant_id", PassingData.getResturant_Id());
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewCartActivity.this, "Failed to store in FireStore", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        }
    }

}

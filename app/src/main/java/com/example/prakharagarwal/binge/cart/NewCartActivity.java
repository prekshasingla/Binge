package com.example.prakharagarwal.binge.cart;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.model_class.PassingCartItem;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.example.prakharagarwal.binge.model_class.PlacedOrderCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    TextView totalpricetext;
    TextView gstpricetext;
    TextView paypricetext;
    CardView cardView;

    //hide the item
    TextView pendingitem;
    // View line1, line2;
    FirebaseFirestore firebaseFirestore;

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
        menuList = PassingCartItem.getMenuArrayList();
        integerList = PassingCartItem.getIntegerArrayList();
        placed_order_btn = findViewById(R.id.placed_order_btn);
        resturant_name = findViewById(R.id.resturatant_cart_name);
        dish_image = findViewById(R.id.resturant_cart_image);

        firebaseFirestore=FirebaseFirestore.getInstance();

//        if (menuList != null) {
//            resturant_name.setText(menuList.get(0).getRestaurantName());
//            Picasso.with(this).load(menuList.get(0).getPoster_url()).into(dish_image);
//
//        }
        adapter = new RecyclerViewCartAdapter(menuList, integerList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        cardView.setVisibility(View.INVISIBLE);
        pendingitem = findViewById(R.id.Pending_item);
        //   line1 = findViewById(R.id.line_1);
        //   line2 = findViewById(R.id.line_2);

        placed_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute();
            }
        });

        if(!menuList.isEmpty() && !integerList.isEmpty())
            placed_order_btn.setVisibility(View.VISIBLE);


        if (!PassingCartItem.placed_order_hashmap.isEmpty()) {
            placed_order_btn.setVisibility(View.INVISIBLE);
            pendingitem.setVisibility(View.INVISIBLE);

            List<Menu> localmenu = new ArrayList<>();
            List<Integer> localinterger = new ArrayList<>();
            int totalprice = 0;
            int gstpri = 0;

            for (Map.Entry<Menu, Integer> hashmap : PassingCartItem.placed_order_hashmap.entrySet()) {

                localmenu.add(hashmap.getKey());
                localinterger.add(hashmap.getValue());
                totalprice += Integer.parseInt(hashmap.getKey().getPrice()) * hashmap.getValue();
            }
            gstpri = (totalprice * 18) / 100;

            totalpricetext.setText("₹" + totalprice);
            gstpricetext.setText("₹" + gstpri);
            int total = gstpri + totalprice;
            paypricetext.setText("₹" + total);
            cardView.setVisibility(View.VISIBLE);

            placedOrderAdapter = new RecyclerViewPlacedOrderAdapter(NewCartActivity.this, localmenu, localinterger);
            placed_order.setLayoutManager(new LinearLayoutManager(NewCartActivity.this));
            placed_order.setAdapter(placedOrderAdapter);
//
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) pendingitem.getLayoutParams();
//            lp.addRule(RelativeLayout.BELOW, pendingitem.getId());



        }

    }

    class MyTask extends AsyncTask<Void, Void, Void> {
        HashMap<Menu, Integer> cartitem;
        int size;
        List<Menu> menuListsec;
        List<Integer> integerListsec;
        int totalpricesum;
        int gstprice;
        Menu localmenu=new Menu();
        HashMap<String,String> stringHashMap;

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("RISHABH RAWAT", "onbackground method");
            size = menuList.size();
            menuList.clear();
            integerList.clear();

            cartitem = PassingCartItem.getMenuHashmap();
            if (cartitem.isEmpty())
                return null;

            //PassingCartItem.menuArrayList.clear();
            //PassingCartItem.integerArrayList.clear();
            menuListsec = new ArrayList<>();
            integerListsec = new ArrayList<>();
            stringHashMap=new HashMap<>();
           // Menu localmenu=new Menu();
            for (Map.Entry<Menu, Integer> entry : cartitem.entrySet()) {
                if (entry.getValue() != 0) {
                    PassingCartItem.addMenuArrayList(entry.getKey(), entry.getValue());
                    localmenu = entry.getKey();
                    menuListsec.add(entry.getKey());
                    integerListsec.add(entry.getValue());
                    totalpricesum += Integer.parseInt(localmenu.getPrice()) * entry.getValue();
                    PassingCartItem.placed_order_hashmap.put(entry.getKey(), entry.getValue());
                    stringHashMap.put(entry.getKey().getName(),entry.getValue().toString());
                    System.out.println("Rishabh Rawat" + entry.getKey() + " = " + entry.getValue());
                }
            }
            gstprice = (totalpricesum * 18) / 100;

            Log.d("RISHABH RAWAT", "onbackground method complete");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("RISHABH RAWAT", "onpost method");
            if (cartitem != null) {
                int size = menuList.size();
                for (int i = 0; i <= size - 1; i++) {
                    PassingCartItem.addmenu(menuList.get(i), 0);
                    menuList.get(i).setTotalcartItem(0);
                }

                menuList.clear();
                integerList.clear();
                adapter.notifyDataSetChanged();

                placedOrderAdapter = new RecyclerViewPlacedOrderAdapter(NewCartActivity.this, menuListsec, integerListsec);
                placed_order.setLayoutManager(new LinearLayoutManager(NewCartActivity.this));
                placed_order.setAdapter(placedOrderAdapter);

                totalpricetext.setText("₹" + totalpricesum);
                gstpricetext.setText("₹" + gstprice);
                int total = gstprice + totalpricesum;
                paypricetext.setText("₹" + total);
                cardView.setVisibility(View.VISIBLE);
                pendingitem.setVisibility(View.INVISIBLE);
                placed_order_btn.setVisibility(View.INVISIBLE);



                //Now save the order into the Firebase Firestore
                PlacedOrderCart placedOrderCart=new PlacedOrderCart();
                placedOrderCart.setDishes(stringHashMap);
                placedOrderCart.setCart_value(totalpricesum+gstprice);
                //placedOrderCart.setGst("18%");
                placedOrderCart.setUserId("Rishabh");

                //DocumentReference reference=firebaseFirestore.collection("orders/"+PassingData.getResturant_Id()+"/order").document();
                //String orderid=reference.getId();
                placedOrderCart.setId("");
                placedOrderCart.setLocation_lat(0.0);
                placedOrderCart.setLocation_long(0.0);

                //Log.d("RISHABHRAWAT",localmenu.getName()+"\n"+localmenu.getRestaurantName()+"\n"+localmenu.getRestaurant_id());

             //   Log.d("RISHABH RAWAT123456",orderid);
                firebaseFirestore.collection("orders/"+PassingData.getResturant_Id()+"/order").add(placedOrderCart).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(NewCartActivity.this,"Success to store in FireStore",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewCartActivity.this,"Failed to store in FireStore",Toast.LENGTH_LONG).show();
                    }
                });

                Log.d("RISHABH RAWAT", "onpost method finish");
            } else {
                //Toast.makeText(NewCartActivity.this, "Your Pending have no more item", Toast.LENGTH_SHORT).show();
            }

        }
    }
}



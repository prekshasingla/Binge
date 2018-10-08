package com.example.prakharagarwal.binge.cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.BaseApplication;
import com.example.prakharagarwal.binge.LoginActivity;
import com.example.prakharagarwal.binge.MainScreen.DishInfoActivity;
import com.example.prakharagarwal.binge.MainScreen.MySharedPreference;
import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.model_class.PassingCartItem;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.example.prakharagarwal.binge.model_class.PlacedOrderCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
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
    static Button placed_order_btn;
    static TextView totalpricetext;
    static TextView gstpricetext;
    static TextView paypricetext;
    static TextView discountText;
    static TextView discountValue;
    static Button payNowbtn;


    TextView backbutton;
    FirebaseFirestore firebaseFirestore;
    private Intent intent;

    TextView placed_Order_text;
    View line1;
    View line2;
    RelativeLayout coupon;
    CardView cardView;

    TextView pending_Item;
    TextView add_item_text;
    static Boolean preOrderStatus = false;
    long preorder_switch = 0;


    static Float payAmount;


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
        discountText = findViewById(R.id.discount_text);
        discountValue = findViewById(R.id.discount_price);

        placed_order_btn = findViewById(R.id.placed_order_btn);
        resturant_name = findViewById(R.id.resturatant_cart_name);
        dish_image = findViewById(R.id.resturant_cart_image);
        backbutton = findViewById(R.id.back_btn);
        placed_Order_text = findViewById(R.id.placed_order_text);
        line1 = findViewById(R.id.line_1);
        line2 = findViewById(R.id.line_2);
        coupon = findViewById(R.id.cardView_cart);
        pending_Item = findViewById(R.id.Pending_item);
        payNowbtn = findViewById(R.id.pay_now_btn);
        payNowbtn.setText("Pay");
        add_item_text = findViewById(R.id.add_item_text);
        TextView gst_info = findViewById(R.id.gst_info_text);
        gst_info.setText(Html.fromHtml("<sup><small>*</small></sup> 5% on food items | 18% on alcoholic items."));
        firebaseFirestore = FirebaseFirestore.getInstance();


        //now check whether if user placed item in restaurant but not pay the bill
        MySharedPreference sharedPreference = new MySharedPreference(NewCartActivity.this);
        if (sharedPreference.get_insideorderpayment() && ((BaseApplication) getApplication()).isCartFlag()) {
            MySharedPreference savedata = new MySharedPreference(NewCartActivity.this);
            resturant_name.setText(savedata.get_inside_order_restaurant_id());
            Picasso.with(this).load(savedata.get_menu_image()).into(dish_image);

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
            ((BaseApplication) getApplication()).setCartFlag(false);
            showitem();
            PassingData.setResturant_Id(savedata.get_inside_order_restaurant_id());
            PassingCartItem.setOrderID(savedata.get_insideorderID());


        } else {

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

            /* order type can be preorder , orderinside or null
               default functionality will be preorder which will
               act for both preorder and null
               Case would be made only for orderinside*/

            intent = getIntent();
            if (intent != null)
                if (intent.getStringExtra("flag") != null)
                    if (intent.getStringExtra("flag").equals("insideOrder")) {
                        preOrderStatus = false;
                        preorder_switch = 1;
                        placed_order_btn.setVisibility(View.VISIBLE);
                        payNowbtn.setVisibility(View.GONE);
                        cardView.setVisibility(View.GONE);
                    } else {
                        preorder_switch = intent.getLongExtra("preorder_switch", 0);
                        preOrderStatus = true;
                        placed_order_btn.setText("Place Order");
                        placed_order_btn.setVisibility(View.GONE);
                        payNowbtn.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                        RecyclerViewCartAdapter.addingToBill();
                    }
                else {
                    preorder_switch = intent.getLongExtra("preorder_switch", 0);
                    preOrderStatus = true;
                    placed_order_btn.setText("Place Order");
                    placed_order_btn.setVisibility(View.GONE);
                    payNowbtn.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.VISIBLE);
                    RecyclerViewCartAdapter.addingToBill();
                }

            if (menuList.size() == 0) {
                cardView.setVisibility(View.GONE);
                payNowbtn.setVisibility(View.GONE);
            }
            resturant_name.setText(intent != null ? intent.getStringExtra("resturant_name") : null);
            Picasso.with(this).load(intent.getStringExtra("photo_url")).into(dish_image);
            MySharedPreference savedata = new MySharedPreference(NewCartActivity.this);
            savedata.set_inside_order_restaurant_id(PassingData.resturant_Id);
            savedata.set_menu_image(intent.getStringExtra("photo_url"));


            adapter = new RecyclerViewCartAdapter(menuList, integerList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            if (menuList.size() == 0 || integerList.size() == 0) {
                add_item_text.setVisibility(View.VISIBLE);
                placed_order_btn.setVisibility(View.GONE);
            } else {
                add_item_text.setVisibility(View.GONE);
                if (preOrderStatus)
                    placed_order_btn.setVisibility(View.GONE);
                else
                    placed_order_btn.setVisibility(View.VISIBLE);

            }
            adapter.notifyDataSetChanged();

            showplacedItem();
        }
        placed_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String uID = prefs.getString("username", null);
                if (uID != null) {
                    new MyTask(NewCartActivity.this).execute();

                } else {
                    startActivity(new Intent(NewCartActivity.this, LoginActivity.class));
                }
            }
        });

        payNowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preorder_switch == 0) {
                    Toast.makeText(NewCartActivity.this, "This restaurant is currently not serviceable. Please " +
                            "check back later.", Toast.LENGTH_SHORT).show();
                } else {
                    if (!preOrderStatus) {
                        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        String uID = prefs.getString("username", null);
                        if (uID != null) {
//                        new MyTask(NewCartActivity.this).execute();

                            Intent intent = new Intent(NewCartActivity.this, ReviewOrderActivity.class);
                            intent.putExtra("payamount", payAmount);
                            intent.putExtra("restaurant", PassingData.getResturantName());
                            intent.putExtra("restaurantID", PassingData.getResturant_Id());
                            intent.putExtra("orderID", PassingCartItem.getOrderID());


                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        } else {
                            startActivity(new Intent(NewCartActivity.this, LoginActivity.class));
                        }

                    } else {
                        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        String uID = prefs.getString("username", null);
                        if (uID != null) {
//                            new MyTask(NewCartActivity.this).execute();
                            placePreOrder();

                        } else {
                            startActivity(new Intent(NewCartActivity.this, LoginActivity.class));
                        }

                    }
                }
            }

        });

        // RecyclerViewCartAdapter.addingToBill();


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
            float totalprice = 0;
            float total_priceGST5 = 0;
            float total_priceGST18 = 0;
            float gstpri = 0;
            float totalDiscount = 0f;
            float discountedPrice = 0f;

            for (Map.Entry<Menu, Integer> hashmap : PassingCartItem.placed_order_hashmap.entrySet()) {

                localmenu.add(hashmap.getKey());
                if (hashmap.getKey().getDiscount() > 0) {
                    totalDiscount += (Float.parseFloat(hashmap.getKey().getPrice()) * (hashmap.getKey().getDiscount() / 100.0f))
                            * hashmap.getValue();
                    discountedPrice = Float.parseFloat(hashmap.getKey().getPrice()) -
                            (Float.parseFloat(hashmap.getKey().getPrice()) * (hashmap.getKey().getDiscount() / 100.0f));
                }
                if (discountedPrice > 0) {
                    if (hashmap.getKey().getGst() == 5)
                        total_priceGST5 += discountedPrice * hashmap.getValue();
                    if (hashmap.getKey().getGst() == 18)
                        total_priceGST18 += discountedPrice * hashmap.getValue();
                } else {
                    if (hashmap.getKey().getGst() == 5)
                        total_priceGST5 += Float.parseFloat(hashmap.getKey().getPrice()) * hashmap.getValue();
                    if (hashmap.getKey().getGst() == 18)
                        total_priceGST18 += Float.parseFloat(hashmap.getKey().getPrice()) * hashmap.getValue();
                }
                localinterger.add(hashmap.getValue());
                totalprice += Float.parseFloat(hashmap.getKey().getPrice()) * hashmap.getValue();
            }
            if (totalDiscount > 0) {
                discountText.setVisibility(View.VISIBLE);
                discountValue.setVisibility(View.VISIBLE);
                discountValue.setText("₹" + totalDiscount);
            } else {
                discountText.setVisibility(View.INVISIBLE);
                discountValue.setVisibility(View.INVISIBLE);
            }
            gstpri = ((total_priceGST5 * 5) / 100) + ((total_priceGST18 * 18) / 100);

            totalpricetext.setText("₹" + totalprice);
            gstpricetext.setText("₹" + gstpri);
            float total = gstpri + totalprice - totalDiscount;

            paypricetext.setText("₹" + total);
            payAmount = total;

            Picasso.with(this).load(localmenu.get(0).getPoster_url()).into(dish_image);

            placedOrderAdapter = new RecyclerViewPlacedOrderAdapter(NewCartActivity.this, localmenu, localinterger);
            placed_order.setLayoutManager(new LinearLayoutManager(NewCartActivity.this));
            placed_order.setAdapter(placedOrderAdapter);

        } else {

        }
    }

    public void showitem() {
        MySharedPreference sharedPreference = new MySharedPreference(NewCartActivity.this);
        //  if (sharedPreference.get_insideorderpayment()) {

        HashMap<Menu, Integer> savedplacedorder = new HashMap<>();
        String json = sharedPreference.get_saveplacedordermap();
        try {
            JSONObject placedorder = new JSONObject(json);
            JSONArray orderarray = placedorder.getJSONArray("Order");
            for (int i = 0; i <= orderarray.length() - 1; i++) {
                Menu menu = new Menu();
                JSONObject object = orderarray.getJSONObject(i);
                //  menu.setVideo_url(object.getString("video_url"));
                menu.setName(object.getString("name"));
                //   menu.setDesc(object.getString("desc"));
                menu.setPrice(object.getString("price"));
                menu.setVeg(object.getLong("veg"));
                menu.setGst(object.getLong("gst"));
                menu.setDiscount(object.getLong("discount"));
                //   menu.setHas_video(object.getLong("has_video"));
                //   menu.setCart_quantity(object.getInt("cart_quantity"));
                //   menu.setRestaurantName(object.getString("restaurantName"));
                //   menu.setPoster_url(object.getString("poster_url"));
                //    menu.setCategory(object.getString("category"));
                //    menu.setRestaurant_id(object.getString("restaurant_id"));
                //    menu.setCourse_meal(object.getLong("course_meal"));
                //    menu.setTotalcartItem(object.getInt("totalcartItem"));
                //    menu.setLatitude(object.getString("latitude"));
                //    menu.setLongitude(object.getString("longitude"));
                savedplacedorder.put(menu, object.getInt("quantity"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Menu> savedmenulist = new ArrayList<>();
        List<Integer> savedintegerlist = new ArrayList<>();

        for (Map.Entry<Menu, Integer> tempmap : savedplacedorder.entrySet()) {
            savedmenulist.add(tempmap.getKey());
            savedintegerlist.add(tempmap.getValue());
        }
        PassingCartItem.placed_order_hashmap = savedplacedorder;
        placedOrderAdapter = new RecyclerViewPlacedOrderAdapter(NewCartActivity.this, savedmenulist, savedintegerlist);
        placed_order.setLayoutManager(new LinearLayoutManager(NewCartActivity.this));
        placed_order.setAdapter(placedOrderAdapter);
        // }

        RecyclerViewPlacedOrderAdapter.addingToBill();
    }


    class MyTask extends AsyncTask<Void, Void, Void> {
        HashMap<Menu, Integer> cartitem;
        int size;
        List<Menu> menuListsec;
        List<Integer> integerListsec;
        float totalpricesum;
        float totalpricesumGST5;
        float totalpricesumGST18;
        float totalDiscount = 0f;
        float discountedPrice = 0f;

        float gstprice;
        Menu localmenu = new Menu();
        HashMap<String, String> stringHashMap;
        Context context;
        ProgressDialog dialog;

        MyTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Please Wait");
            if (!preOrderStatus)
                dialog.setMessage("Your Items Being Placed ....");
            else
                dialog.setMessage("Opening Payment Gateway....");

            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            size = menuList.size();
//            menuList.clear();
//            integerList.clear();

            cartitem = PassingCartItem.getMenuHashmap();
            if (cartitem.isEmpty())
                return null;


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
                        if (localmenu.getDiscount() > 0) {
                            totalDiscount += (Float.parseFloat(localmenu.getPrice()) * (localmenu.getDiscount() / 100.0f))
                                    * entry.getValue();
                            discountedPrice = Float.parseFloat(localmenu.getPrice()) -
                                    (Float.parseFloat(localmenu.getPrice()) * (localmenu.getDiscount() / 100.0f));
                        }
                        if (discountedPrice > 0) {
                            if (localmenu.getGst() == 5)
                                totalpricesumGST5 += discountedPrice * entry.getValue();
                            if (localmenu.getGst() == 18)
                                totalpricesumGST18 += discountedPrice * entry.getValue();
                        } else {
                            if (localmenu.getGst() == 5)
                                totalpricesumGST5 += Float.parseFloat(localmenu.getPrice()) * entry.getValue();
                            if (localmenu.getGst() == 18)
                                totalpricesumGST18 += Float.parseFloat(localmenu.getPrice()) * entry.getValue();
                        }


                        totalpricesum += Float.parseFloat(localmenu.getPrice()) * entry.getValue();
                        // PassingCartItem.placed_order_hashmap.put(entry.getKey(), entry.getValue());
                        if(!preOrderStatus)
                        PassingCartItem.addplacedorder_item(entry.getKey(), entry.getValue());
                        stringHashMap.put(entry.getKey().getName(), entry.getValue().toString());
                    }
                }

                if (!preOrderStatus) {
                    //saved the hashmap if user kill the app then we directly retrive the data from the sharedpreference
                    JSONArray menuArray = new JSONArray();
                    for (Map.Entry<Menu, Integer> entry : PassingCartItem.placed_order_hashmap.entrySet()) {
                        JSONObject menuObject = new JSONObject();
                        Menu menu = entry.getKey();
                        try {
                            menuObject.put("video_url", menu.getVideo_url());
                            menuObject.put("name", menu.getName());
                            menuObject.put("desc", menu.getDesc());
                            menuObject.put("price", menu.getPrice());
                            menuObject.put("veg", menu.getVeg());
                            menuObject.put("cart_quantity", menu.getCart_quantity());
                            menuObject.put("restaurantName", menu.getRestaurantName());
                            menuObject.put("poster_url", menu.getPoster_url());
                            menuObject.put("category", menu.getCategory());
                            menuObject.put("restaurant_id", menu.getRestaurant_id());
                            menuObject.put("course_meal", menu.getCourse_meal());
                            menuObject.put("totalcartItem", menu.getTotalcartItem());
                            menuObject.put("latitude", menu.getLatitude());
                            menuObject.put("longitude", menu.getLongitude());
                            menuObject.put("gst", menu.getGst());
                            menuObject.put("discount", menu.getDiscount());
                            menuObject.put("quantity", entry.getValue());

                            menuArray.put(menuObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Order", menuArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MySharedPreference sharedPreference = new MySharedPreference(NewCartActivity.this);
                    sharedPreference.set_saveplacedordermap(jsonObject.toString());
                    sharedPreference.set_insideorderpayment(true);
                }

            } else {
                stringHashMap.clear();

                for (Map.Entry<Menu, Integer> entry : PassingCartItem.getMenuHashmap().entrySet()) {
                    if (entry.getValue() != 0) {
                        PassingCartItem.addplacedorder_item(entry.getKey(), entry.getValue());
                        stringHashMap.put(entry.getKey().getName(), entry.getValue().toString());

                    }
                }

                //saved the hashmap if user kill the app then we directly retrive the data from the sharedpreference
                JSONArray menuArray = new JSONArray();
                for (Map.Entry<Menu, Integer> entry : PassingCartItem.placed_order_hashmap.entrySet()) {
                    JSONObject menuObject = new JSONObject();
                    Menu menu = entry.getKey();
                    try {
                        menuObject.put("video_url", menu.getVideo_url());
                        menuObject.put("name", menu.getName());
                        menuObject.put("desc", menu.getDesc());
                        menuObject.put("price", menu.getPrice());
                        menuObject.put("veg", menu.getVeg());
                        menuObject.put("cart_quantity", menu.getCart_quantity());
                        menuObject.put("restaurantName", menu.getRestaurantName());
                        menuObject.put("poster_url", menu.getPoster_url());
                        menuObject.put("category", menu.getCategory());
                        menuObject.put("restaurant_id", menu.getRestaurant_id());
                        menuObject.put("course_meal", menu.getCourse_meal());
                        menuObject.put("totalcartItem", menu.getTotalcartItem());
                        menuObject.put("latitude", menu.getLatitude());
                        menuObject.put("longitude", menu.getLongitude());
                        menuObject.put("gst", menu.getGst());
                        menuObject.put("discount", menu.getDiscount());
                        menuObject.put("quantity", entry.getValue());
                        menuArray.put(menuObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Order", menuArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MySharedPreference sharedPreference = new MySharedPreference(NewCartActivity.this);
                sharedPreference.set_saveplacedordermap(jsonObject.toString());
                sharedPreference.set_insideorderpayment(true);
                for (Map.Entry<Menu, Integer> entry : PassingCartItem.placed_order_hashmap.entrySet()) {
                    if (entry.getValue() != 0) {
                        PassingCartItem.addMenuArrayList(entry.getKey(), entry.getValue());
                        localmenu = entry.getKey();
                        menuListsec.add(entry.getKey());
                        integerListsec.add(entry.getValue());
                        if (localmenu.getDiscount() > 0) {
                            totalDiscount += (Float.parseFloat(localmenu.getPrice()) * (localmenu.getDiscount() / 100.0f))
                                    * entry.getValue();
                            discountedPrice = Float.parseFloat(localmenu.getPrice()) -
                                    (Float.parseFloat(localmenu.getPrice()) * (localmenu.getDiscount() / 100.0f));
                        }
                        if (discountedPrice > 0) {
                            if (localmenu.getGst() == 5)
                                totalpricesumGST5 += discountedPrice * entry.getValue();
                            if (localmenu.getGst() == 18)
                                totalpricesumGST18 += discountedPrice * entry.getValue();
                        } else {
                            if (localmenu.getGst() == 5)
                                totalpricesumGST5 += Float.parseFloat(localmenu.getPrice()) * entry.getValue();
                            if (localmenu.getGst() == 18)
                                totalpricesumGST18 += Float.parseFloat(localmenu.getPrice()) * entry.getValue();
                        }


                        totalpricesum += Float.parseFloat(localmenu.getPrice()) * entry.getValue();
                    }
                }


            }

            gstprice = ((totalpricesumGST5 * 5) / 100) + ((totalpricesumGST18 * 18) / 100);
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

                if (totalDiscount > 0) {
                    discountText.setVisibility(View.VISIBLE);
                    discountValue.setVisibility(View.VISIBLE);
                    discountValue.setText("₹" + totalDiscount);
                } else {
                    discountText.setVisibility(View.INVISIBLE);
                    discountValue.setVisibility(View.INVISIBLE);
                }
                totalpricetext.setText("₹" + totalpricesum);
                gstpricetext.setText("₹" + gstprice);
                float total = gstprice + totalpricesum - totalDiscount;
                paypricetext.setText("₹" + total);
                NewCartActivity.payAmount = total;

                if (!preOrderStatus) {
                    //Now save the order into the Firebase Firestore
                    PlacedOrderCart placedOrderCart = new PlacedOrderCart();
                    placedOrderCart.setDishes(stringHashMap);
                    placedOrderCart.setCart_value(total);
                    placedOrderCart.setTableNo(PassingData.getTableNo());
                    //placedOrderCart.setGst("18%");
                    SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    String uID = prefs.getString("username", null);
                    placedOrderCart.setUserId(uID);
                    placedOrderCart.setLocation_lat(0.0);
                    placedOrderCart.setLocation_long(0.0);
                    placedOrderCart.setTimestamp(Calendar.getInstance().getTimeInMillis() + "");
                    placedOrderCart.setOrdertype("insideOrder");
                    DocumentReference reference = firebaseFirestore.collection("orders/" + PassingData.getResturant_Id() + "/InsideOrder").document();
                    String orderID = "";

                    if (PassingCartItem.getOrderID() == null) {
                        orderID = reference.getId();
                        PassingCartItem.setOrderID(reference.getId());
                        Log.d("RISHABH ORDER if part", orderID);
                        placedOrderCart.setId(orderID);
                        new MySharedPreference(NewCartActivity.this).set_insideorderID(orderID);
                        firebaseFirestore.collection("orders/" + PassingData.getResturant_Id() + "/InsideOrder").document(orderID).set(placedOrderCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
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
                                dialog.dismiss();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewCartActivity.this, "Some Error Occurred", Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        orderID = PassingCartItem.getOrderID();
                        Log.d("RISHABH ORDER else part", orderID);
                        placedOrderCart.setId(orderID);

                        firebaseFirestore.collection("orders/" + PassingData.getResturant_Id() + "/InsideOrder").document(orderID).update("dishes", FieldValue.arrayUnion(stringHashMap)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
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
                                dialog.dismiss();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewCartActivity.this, "Some Error Occurred", Toast.LENGTH_LONG).show();
                            }
                        });
                    }


                } else {



                }

            }
        }
    }
    private void placePreOrder() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Please wait ...");

        dialog.setCancelable(false);
        dialog.show();
        HashMap<Menu, Integer> cartitem;
        int size;
        List<Menu> menuListsec;
        List<Integer> integerListsec;
        float totalpricesum=0;
        float totalpricesumGST5 = 0;
        float totalpricesumGST18=0;
        float totalDiscount = 0f;
        float discountedPrice = 0f;

        float gstprice;
        Menu localmenu = new Menu();
        HashMap<String, String> stringHashMap;
        menuListsec = new ArrayList<>();
        integerListsec = new ArrayList<>();
        stringHashMap = new HashMap<>();
        for (Map.Entry<Menu, Integer> entry : PassingCartItem.getMenuHashmap().entrySet()) {
            if (entry.getValue() != 0) {
                localmenu = entry.getKey();
                menuListsec.add(entry.getKey());
                integerListsec.add(entry.getValue());
                if (localmenu.getDiscount() > 0) {
                    totalDiscount += (Float.parseFloat(localmenu.getPrice()) * (localmenu.getDiscount() / 100.0f))
                            * entry.getValue();
                    discountedPrice = Float.parseFloat(localmenu.getPrice()) -
                            (Float.parseFloat(localmenu.getPrice()) * (localmenu.getDiscount() / 100.0f));
                }
                if (discountedPrice > 0) {
                    if (localmenu.getGst() == 5)
                        totalpricesumGST5 += discountedPrice * entry.getValue();
                    if (localmenu.getGst() == 18)
                        totalpricesumGST18 += discountedPrice * entry.getValue();
                } else {
                    if (localmenu.getGst() == 5)
                        totalpricesumGST5 += Float.parseFloat(localmenu.getPrice()) * entry.getValue();
                    if (localmenu.getGst() == 18)
                        totalpricesumGST18 += Float.parseFloat(localmenu.getPrice()) * entry.getValue();
                }


                totalpricesum += Float.parseFloat(localmenu.getPrice()) * entry.getValue();
                // PassingCartItem.placed_order_hashmap.put(entry.getKey(), entry.getValue());
                if(!preOrderStatus)
                    PassingCartItem.addplacedorder_item(entry.getKey(), entry.getValue());
                stringHashMap.put(entry.getKey().getName(), entry.getValue().toString());
            }
        }
        gstprice = ((totalpricesumGST5 * 5) / 100) + ((totalpricesumGST18 * 18) / 100);
        float total = gstprice + totalpricesum - totalDiscount;

        final HashMap<String, Object> placedOrderHashmap = new HashMap<>();
        List<HashMap<String, String>> dishes = new ArrayList<>();
        dishes.add(stringHashMap);
        placedOrderHashmap.put("dishes", dishes);
        placedOrderHashmap.put("cart_value", total);
        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String uID = prefs.getString("username", null);
        placedOrderHashmap.put("userId", uID);
        placedOrderHashmap.put("location_lat", 0.0);
        placedOrderHashmap.put("location_long", 0.0);
        placedOrderHashmap.put("status", "PreOder Recived");
        placedOrderHashmap.put("time_to_reach", "pending");
        placedOrderHashmap.put("timestamp", Calendar.getInstance().getTimeInMillis() + "");
        placedOrderHashmap.put("ordertype", "preOrder");
        final DocumentReference reference = firebaseFirestore.collection("orders/" + PassingData.getResturant_Id() + "/PreOrder").document();
        placedOrderHashmap.put("id", reference.getId());
//
        Intent intent = new Intent(NewCartActivity.this, ReviewOrderActivity.class);
        intent.putExtra("payamount", total);
        intent.putExtra("restaurant", PassingData.getResturantName());
        intent.putExtra("restaurantID", PassingData.getResturant_Id());
        intent.putExtra("preorderFlag", 1);
        intent.putExtra("preoderHashMap", placedOrderHashmap);
        intent.putExtra("orderID", reference.getId());

        startActivity(intent);
        dialog.dismiss();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


}

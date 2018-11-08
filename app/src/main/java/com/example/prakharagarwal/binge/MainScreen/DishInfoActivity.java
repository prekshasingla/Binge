package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.BaseApplication;
import com.example.prakharagarwal.binge.Config;
import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;

import com.example.prakharagarwal.binge.cart.NewCartActivity;
import com.example.prakharagarwal.binge.model_class.PassingCartItem;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.example.prakharagarwal.binge.rishabhcutomview.CartNumberButton;
import com.example.prakharagarwal.binge.rishabhcutomview.CustomScrollView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishInfoActivity extends AppCompatActivity implements
        YouTubePlayer.OnInitializedListener {


    String orderType = null;
    Animation animationUtils;
    Animation animation;

    static SlidingUpPanelLayout slidingUpPanelLayout;
    static TextView dish_name, dish_price, dish_restaurant, dish_discription, offer;
    static YouTubePlayerSupportFragment youTubePlayerFragment;
    static YouTubePlayer youTubePlayer1;
    Menu menu1;
    Menu menu3;
    ViewPager pager;
    ViewPager pager2;
    static List<Menu> menuList;
    //  ProgressBar progressBar;
    RestaurantPagerAdapter pagerAdapter;
    RestaurantPagerAdapter pagerAdapter1;
    Context context;
    static String youtube_video_url;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    static FrameLayout frameLayout;
    static TextView pending_item;
    static TextView placed_item;
    static TextView timing_text;
    TextView mostPopularText;
    TextView dish_address;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    ImageView backbtn;
    CustomScrollView customScrollView;
    // static CartNumberButton cartNumberButton;

    String resturant_name;
    static List<Menu> course_meal1;
    static List<Menu> course_meal2;
    static String timing;

    RelativeLayout main_relative_layout;

    final static List<String> foodcategory = new ArrayList<>();
    final static List<List<Menu>> foodcategorywithoutvideo = new ArrayList<>();
    static HashMap<String, List<Menu>> menuHashMap = new HashMap<>();

    long preorder_switch = 0;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_info);

        MySharedPreference sharedPreference = new MySharedPreference(this);
        if (sharedPreference.get_insideorderpayment() && ((BaseApplication) getApplication()).isCartFlag()) {
            Intent intent = new Intent(DishInfoActivity.this, NewCartActivity.class);
            startActivity(intent);
        }
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        progress.setIndeterminate(true);
        context = this;
        main_relative_layout = findViewById(R.id.main_relative_layout);
        main_relative_layout.setVisibility(View.GONE);
        slidingUpPanelLayout = findViewById(R.id.slide_panel);
        dish_name = findViewById(R.id.dish_name);
        dish_price = findViewById(R.id.dish_price);
        dish_restaurant = findViewById(R.id.dish_restaurant);
        offer = findViewById(R.id.dish_offer);
        dish_discription = findViewById(R.id.dish_discription);
        frameLayout = findViewById(R.id.cart_layout);
        //   cartNumberButton = findViewById(R.id.elegantNumberButton);
        pending_item = findViewById(R.id.pending_item_textview);
        placed_item = findViewById(R.id.placed_item);
        timing_text = findViewById(R.id.dish_time);
        mostPopularText = findViewById(R.id.dishes_meal2);
        dish_address = findViewById(R.id.dish_location);
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_player_fragment);
        pager = findViewById(R.id.dish_viewpager);
        pager2 = findViewById(R.id.dish_viewpager_second);
        customScrollView = findViewById(R.id.customScrollView);
        expandableListView = findViewById(R.id.lvExp);
        //   progressBar = findViewById(R.id.progressBar);
        pager.setPageMargin(20);
        pager2.setPageMargin(20);

        dish_name.setVisibility(View.INVISIBLE);
        dish_price.setVisibility(View.INVISIBLE);
        dish_restaurant.setVisibility(View.INVISIBLE);
        dish_discription.setVisibility(View.INVISIBLE);
        //  cartNumberButton.setVisibility(View.INVISIBLE);
        youTubePlayerFragment.setMenuVisibility(true);
        pager.setVisibility(View.INVISIBLE);
        pager2.setVisibility(View.INVISIBLE);
        timing_text.setVisibility(View.INVISIBLE);
        customScrollView.setEnableScrolling(false);

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    customScrollView.setEnableScrolling(true);

                }
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    customScrollView.setEnableScrolling(false);
                    customScrollView.smoothScrollTo(0, -5000);
                }

            }
        });


        animationUtils = AnimationUtils.loadAnimation(DishInfoActivity.this, R.anim.fade_in);
        animation = AnimationUtils.loadAnimation(DishInfoActivity.this, R.anim.fade_out);
        menuList = new ArrayList<>();


        Intent intent = getIntent();
        String resturant_id = intent.getStringExtra("rest");
        String dish = intent.getStringExtra("dish");
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Menu, Integer> cartitem = PassingCartItem.getMenuHashmap();
                PassingCartItem.menuArrayList.clear();
                PassingCartItem.integerArrayList.clear();
                String photo_url = "Image URL";
                for (Map.Entry<Menu, Integer> entry : cartitem.entrySet()) {
                    if (entry.getValue() != 0) {
                        PassingCartItem.addMenuArrayList(entry.getKey(), entry.getValue());
                        photo_url = entry.getKey().getPoster_url();
                        System.out.println("Rishabh" + entry.getKey() + " = " + entry.getValue());
                    }
                }
                if (orderType == null) {
                    Intent intent = new Intent(DishInfoActivity.this, NewCartActivity.class);
                    intent.putExtra("resturant_name", resturant_name);
                    intent.putExtra("photo_url", photo_url);
                    intent.putExtra("preorder_switch", preorder_switch);
                    startActivity(intent);
                } else if (orderType.equals("preOrder")) {
                    Intent intent = new Intent(DishInfoActivity.this, NewCartActivity.class);
                    intent.putExtra("flag", "preOrder");
                    intent.putExtra("resturant_name", resturant_name);
                    intent.putExtra("preorder_switch", preorder_switch);
                    intent.putExtra("photo_url", photo_url);

                    startActivity(intent);
                } else if (orderType.equals("insideOrder")) {
                    Intent intent = new Intent(DishInfoActivity.this, NewCartActivity.class);
                    intent.putExtra("flag", "insideOrder");
                    intent.putExtra("resturant_name", resturant_name);
                    intent.putExtra("photo_url", photo_url);

                    startActivity(intent);
                }
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }


        });


        orderType = intent.getStringExtra("flag");
        if (intent.getLongExtra("whole_discount", 0) != 0) {
            offer.setText(intent.getLongExtra("whole_discount", 0) + " % on Food Bill");
            offer.setVisibility(View.VISIBLE);
        } else
            offer.setVisibility(View.GONE);
        final String finalDish;
        if (dish == null) {
            finalDish = "Wrong";
        } else {
            PassingData.setResturant_Id(resturant_id);
            finalDish = dish;
        }

        timing = intent.getStringExtra("time");
        if (timing != null && !timing.equals("")) {
            timing_text.setVisibility(View.VISIBLE);
            timing_text.setText(timing);
        } else {
            timing_text.setVisibility(View.GONE);
        }


        //seperate the data for the two view pager from the menulist and pass the half data to menu 1 and menu  2
        course_meal1 = new ArrayList<>();
        course_meal2 = new ArrayList<>();
//        final List<String> foodcategory = new ArrayList<>();
//        final List<List<Menu>> foodcategorywithoutvideo = new ArrayList<>();


        menuHashMap.clear();
        foodcategory.clear();
        foodcategorywithoutvideo.clear();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("menu/" + resturant_id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child1 : dataSnapshot.getChildren()) {
                    if (child1.getKey().equals("latitude")) {
                        PassingData.setLatitude((Double) child1.getValue());
                    }
                    if (child1.getKey().equals("longitude"))
                        PassingData.setLongitude((Double) child1.getValue());
                    if (child1.getKey().equals("restaurant_name")) {
                        resturant_name = String.valueOf(child1.getValue());
                        PassingData.setResturantName(String.valueOf(child1.getValue()));
                    }
                    if (child1.getKey().equals("discount")) {
                        if ((Long) child1.getValue() != 0) {
                            offer.setText(child1.getValue() + " % on Food Bill");
                            offer.setVisibility(View.VISIBLE);
                        } else
                            offer.setVisibility(View.GONE);


                    }
                    if (child1.getKey().equals("preorder_switch")) {
                        preorder_switch = (long) child1.getValue();

                    }
                    if (child1.getKey().equals("restaurant_id"))
                        PassingData.setResturant_Id(String.valueOf(child1.getValue()));
                    if (!child1.getKey().equals("latitude") && !child1.getKey().equals("longitude") && !child1.getKey().equals("restaurant_name") && !child1.getKey().equals("restaurant_id")) {

                        foodcategory.add(child1.getKey());
                    }

                    List<Menu> localmenu = new ArrayList<>();
                    for (DataSnapshot child2 : child1.getChildren()) {
                        Menu menu2 = child2.getValue(Menu.class);
                        if (finalDish.equals(menu2.getName())) {
                            if (menu2.getCourse_meal() == 1) {
                                if (orderType != null && orderType.equals("preOrder")) {
                                    menu2.setTotalcartItem(1);
                                    course_meal1.add(0, menu2);
                                } else
                                    course_meal1.add(0, menu2);
                            } else {
                                if (orderType != null && orderType.equals("preOrder")) {
                                    menu2.setTotalcartItem(1);
                                    course_meal2.add(0, menu2);
                                } else
                                    course_meal2.add(0, menu2);
                            }
                            menu1 = menu2;
                            //menuList.add(menu2);
                        } else {
                            Log.d("RISHABH", "DATA IS DISH " + menu2.getRestaurantName() + " " + menu2.getPrice() + " " + menu2.getDesc());
                            if (menu2.getHas_video() == 0) {
                                if (menu3 == null) {
                                    menu3 = menu2;
                                    menuList.add(menu3);

                                } else {
                                    menuList.add(menu2);
                                }
                            } else {
                                localmenu.add(menu2);
                            }
                        }
                    }

                    foodcategorywithoutvideo.add(localmenu);
                }
//                HashMap<String, List<Menu>> menuHashMap = new HashMap<>();
                for (int i = 0; i <= foodcategory.size() - 1; i++) {
                    //     if (!foodcategorywithoutvideo.get(i).isEmpty())
                    menuHashMap.put(foodcategory.get(i), foodcategorywithoutvideo.get(i));
                    // else {
                    //this code state that
                    //      foodcategory.remove(i);
                    //      foodcategorywithoutvideo.remove(i);
                    //  }
                }
                List<String> foodcategory2 = new ArrayList<>();
                HashMap<String, List<Menu>> menuHashMap2 = new HashMap<>();
                for (Map.Entry<String, List<Menu>> entry : menuHashMap.entrySet()) {
                    List<Menu> menu = entry.getValue();
                    if (menu.size() > 0) {
                        foodcategory2.add(entry.getKey());
                        menuHashMap2.put(entry.getKey(), menu);
                    }
                }
                progress.setVisibility(View.GONE);
                expandableListAdapter = new ExpandableListAdapter(DishInfoActivity.this, foodcategory2, menuHashMap2);
                expandableListView.setAdapter(expandableListAdapter);
                setListViewHeight(expandableListView, 0);
                expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                        setListViewHeight(parent, groupPosition);
                        return false;
                    }
                });

                if (finalDish.equals("Wrong")) {
                    menu3.getRestaurantName();
                    youtube_video_url = menu3.getVideo_url();
                    dish_name.setText(menu3.getName());
                    dish_price.setText("₹" + menu3.getPrice());
                    dish_discription.setText(menu3.getDesc());
                    dish_restaurant.setText(resturant_name);
                    play_youtube_video(menu3.getVideo_url());
                    visibleView();
                } else {
                    menu1.getRestaurantName();
                    youtube_video_url = menu1.getVideo_url();
                    dish_name.setText(menu1.getName());
                    dish_price.setText("₹ " + menu1.getPrice());
                    dish_discription.setText(menu1.getDesc());
                    dish_restaurant.setText(resturant_name);
                    play_youtube_video(menu1.getVideo_url());
                    visibleView();
                }

                if (orderType != null && orderType.equals("preOrder")) {
                    add_item_to_cart(menu1, 1);
                }


                for (int i = 0; i <= menuList.size() - 1; i++) {
                    if (menuList.get(i).getCourse_meal() == 1)
                        course_meal1.add(menuList.get(i));
                    else if (menuList.get(i).getCourse_meal() == 2)
                        course_meal2.add(menuList.get(i));
                }
                Log.d("RISHABH", "GOING TO THE ADAPTER ");
                pagerAdapter = new RestaurantPagerAdapter(getSupportFragmentManager(), course_meal1, getApplicationContext(), 1);
                pager.setOffscreenPageLimit(10);
                pager.setAdapter(pagerAdapter);
                pagerAdapter1 = new RestaurantPagerAdapter(getSupportFragmentManager(), course_meal2, getApplicationContext(), 2);
                pager.setOffscreenPageLimit(10);
                pager2.setAdapter(pagerAdapter1);
                if (course_meal2.size() == 0) {
                    pager2.setVisibility(View.GONE);
                    mostPopularText.setVisibility(View.GONE);
                } else {
                    pager2.setVisibility(View.VISIBLE);
                    mostPopularText.setVisibility(View.VISIBLE);

                }
                main_relative_layout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getAddress(resturant_id);

    }

    private void getAddress(String resturant_id) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("table/" + resturant_id + "/h_address");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dish_address.setText(dataSnapshot.getValue() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void visibleView() {
        dish_name.setVisibility(View.VISIBLE);
        dish_price.setVisibility(View.VISIBLE);
        dish_restaurant.setVisibility(View.VISIBLE);
        dish_discription.setVisibility(View.VISIBLE);
        // cartNumberButton.setVisibility(View.VISIBLE);
        youTubePlayerFragment.setMenuVisibility(false);
        pager.setVisibility(View.VISIBLE);
        pager2.setVisibility(View.VISIBLE);
        // progressBar.setVisibility(View.INVISIBLE);
    }


    public static void play_youtube_video(final String url) {
        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer1 = player;
                    youTubePlayer1.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

                    youTubePlayer1.cueVideo(url);
                    youTubePlayer1.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e("Rishabh", "Youtube Player View initialization failed");
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = "error";//String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            if (menu1.getVideo_url() != null)
                player.loadVideo(menu1.getVideo_url());
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            youTubePlayer1 = player;

        }
    }


    public void add_item_to_cart(Menu menu1, int newValue) {
        frameLayout.setAnimation(animationUtils);
        frameLayout.setVisibility(View.VISIBLE);

        PassingCartItem.addmenu(menu1, newValue);
        updatehasmap();

    }


    private void updatehasmap() {
        HashMap<Menu, Integer> cartitem = PassingCartItem.getMenuHashmap();

        int temp[] = new int[100];
        byte inc = 0;
        byte totalitem = 0;
        for (Map.Entry<Menu, Integer> map : cartitem.entrySet()) {
            ++inc;
            temp[inc] = map.getValue();
            totalitem += map.getValue();
            Log.d("RISHABH", "VALUE IS " + map.getValue());
        }

        float totalprice = 0;
        inc = 0;
        for (Menu menu : cartitem.keySet()) {
            ++inc;
            totalprice += Float.parseFloat(menu.getPrice()) * temp[inc];
        }
        pending_item.setText("Pending " + totalitem + " Item | ₹" + totalprice);

        if (totalitem == 0 && PassingCartItem.placed_order_hashmap.isEmpty()) {

            frameLayout.setAnimation(animation);
            frameLayout.setVisibility(View.INVISIBLE);
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class RestaurantPagerAdapter extends FragmentStatePagerAdapter {
        FragmentManager fragmentManager;
        private List<Menu> mFood;
        Context context;
        int coursemeal;


        public RestaurantPagerAdapter(FragmentManager fm, List<Menu> mFood, Context context, int coursemeal) {
            super(fm);
            this.fragmentManager = fm;
            this.mFood = mFood;
            this.context = context;
            this.coursemeal = coursemeal;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ResturantMenuFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt("dishPosition", i);
            args.putString("videoId", "1fwwqY9293s");
            args.putInt("coursemeal", coursemeal);
            Menu dish = mFood.get(i);
            args.putSerializable("dish", dish);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);

        }

        @Override
        public float getPageWidth(int position) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return (float) 700 / size.x;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

        }

        @Override
        public int getCount() {
            return mFood.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class ResturantMenuFragment extends Fragment {
        public static final String ARG_OBJECT = "object";
        private static final String TAG = DishInfoActivity.ResturantMenuFragment.class.getSimpleName();
        private Menu dishes;

        private Uri uri;
        public int position;
        public ImageView thumnail;
        public TextView textView;
        public TextView price_rest;
        public static CartNumberButton numberButton;
        public int coursemeal;
        TextView offer;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.restaurant_menu_viewpager, container, false);

            thumnail = view.findViewById(R.id.thumbnail);
            textView = view.findViewById(R.id.dish_name_menu);
            price_rest = view.findViewById(R.id.price_rest);
            numberButton = view.findViewById(R.id.element_btn_viewpager);
            offer = view.findViewById(R.id.restaurant_offer);

            Bundle args = getArguments();
            position = (int) args.getInt("dishPosition");
            dishes = (Menu) args.getSerializable("dish");
            coursemeal = args.getInt("coursemeal");
            textView.setText(dishes.getName());
            Picasso.with(getActivity().getApplicationContext()).load(dishes.getPoster_url()).into(thumnail);
            price_rest.setText("₹" + dishes.getPrice());
            if (dishes.getDiscount() != 0) {
                offer.setText(dishes.getDiscount() + " %");
                offer.setVisibility(View.VISIBLE);
            } else
                offer.setVisibility(View.GONE);


            thumnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DishInfoActivity.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    DishInfoActivity.youTubePlayer1.release();
                    DishInfoActivity.play_youtube_video(dishes.getVideo_url());

                    dish_name.setText(dishes.getName());
                    dish_price.setText("₹ " + dishes.getPrice());
                    dish_discription.setText(dishes.getDesc());
//                    cartNumberButton.setNumber(dishes.getTotalcartItem() + "");
                    if (DishInfoActivity.timing != null && !DishInfoActivity.timing.equals("")) {
                        DishInfoActivity.timing_text.setText(DishInfoActivity.timing);
                        DishInfoActivity.timing_text.setVisibility(View.VISIBLE);
                    } else
                        DishInfoActivity.timing_text.setVisibility(View.GONE);

                }
            });

            numberButton.setNumber(dishes.getTotalcartItem() + "");

            numberButton.setOnValueChangeListener(new CartNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(CartNumberButton view, int oldValue, int newValue) {

                    if (coursemeal == 1) {
                        course_meal1.get(position).setTotalcartItem(newValue);
                    } else {
                        course_meal2.get(position).setTotalcartItem(newValue);
                    }


                    if (newValue != 0)
                        numberButton.updateColors(getResources().getColor(R.color.lime_green), Color.WHITE);
                    else
                        numberButton.updateColors(getResources().getColor(R.color.yellow), Color.WHITE);

                    DishInfoActivity dishInfoActivity = new DishInfoActivity();
                    dishInfoActivity.add_item_to_cart(dishes, newValue);
                }
            });

            prepare();
            return view;
        }


        public void prepare() {
            if (!TextUtils.isEmpty(dishes.getPoster_url()) && uri == null) {
                try {
                    uri = Uri.parse(dishes.getPoster_url());
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        progress.setVisibility(View.VISIBLE);
        if (course_meal1 != null && course_meal2 != null) {

            pagerAdapter = new RestaurantPagerAdapter(getSupportFragmentManager(), course_meal1, getApplicationContext(), 1);
            pager.setOffscreenPageLimit(10);
            pager.setAdapter(pagerAdapter);
            pagerAdapter1 = new RestaurantPagerAdapter(getSupportFragmentManager(), course_meal2, getApplicationContext(), 2);
            pager.setOffscreenPageLimit(10);
            pager2.setAdapter(pagerAdapter1);
            updatehasmap();
        }
        //  if (!PassingCartItem.menuIntegerHashMap.isEmpty()) {
        frameLayout.setVisibility(View.VISIBLE);
        int totalitems = 0, totalprices = 0;
        List<Menu> placedordermenu = new ArrayList<>();
        List<Integer> placedordermenuItem = new ArrayList<>();
        for (Map.Entry<Menu, Integer> entry : PassingCartItem.placed_order_hashmap.entrySet()) {
            totalprices += Integer.parseInt(entry.getKey().getPrice()) * entry.getValue();
            placedordermenu.add(entry.getKey());
            placedordermenuItem.add(entry.getValue());
            totalitems += entry.getValue();
        }
        placed_item.setText("Placed " + totalitems + " Item | ₹" + totalprices);

        Log.d("RISHABH FOOD ITEMS", "The loop has been started");
        for (int i = 0; i <= foodcategorywithoutvideo.size() - 1; i++) {
            Log.d("RISHABH FOOD ITEMS", foodcategorywithoutvideo.size() + "this is the size");
            for (int j = 0; j <= foodcategorywithoutvideo.get(i).size() - 1; j++) {
                Log.d("RISHABH FOOD ITEMS", foodcategorywithoutvideo.get(i).size() + "this is the size of second");
                if (placedordermenu.contains(foodcategorywithoutvideo.get(i).get(j))) {
                    foodcategorywithoutvideo.get(i).get(j).setTotalcartItem(placedordermenuItem.get(j));
                } else {
                    Log.d("RISHABH FOOD ITEMS", "NO item there");
                }

            }
        }
        List<String> foodcategory2 = new ArrayList<>();
        HashMap<String, List<Menu>> menuHashMap2 = new HashMap<>();
        for (Map.Entry<String, List<Menu>> entry : menuHashMap.entrySet()) {
            List<Menu> menu = entry.getValue();
            if (menu.size() > 0) {
                foodcategory2.add(entry.getKey());
                menuHashMap2.put(entry.getKey(), menu);
            }
        }
        progress.setVisibility(View.GONE);
        expandableListAdapter = new ExpandableListAdapter(DishInfoActivity.this, foodcategory2, menuHashMap2);
        expandableListView.setAdapter(expandableListAdapter);
        // }
    }

    @Override
    public void onBackPressed() {
        if(new MySharedPreference(this).get_insideorderpayment())
            Toast.makeText(this, "There are placed items in your cart. Kindly pay the bill.", Toast.LENGTH_LONG).show();
        else {
        PassingCartItem.menuIntegerHashMap.clear();
        PassingCartItem.placed_order_hashmap.clear();
        super.onBackPressed();
//
        }
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }


}




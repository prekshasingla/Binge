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
import android.support.v4.app.FragmentPagerAdapter;
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
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.cart.NewCartActivity;
import com.example.prakharagarwal.binge.model_class.PassingCartItem;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.example.prakharagarwal.binge.rishabhcutomview.CartNumberButton;
import com.facebook.imagepipeline.request.ImageRequest;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishInfoActivity extends AppCompatActivity implements
        YouTubePlayer.OnInitializedListener {


    Animation animationUtils;
    Animation animation;

    static SlidingUpPanelLayout slidingUpPanelLayout;
    static TextView dish_name, dish_price, dish_trending, dish_rating, dish_time, dish_discription;
    static YouTubePlayerSupportFragment youTubePlayerFragment;
    static YouTubePlayer youTubePlayer1;
    Menu menu1;
    Menu menu3;
    ViewPager pager;
    ViewPager pager2;
    static List<Menu> menuList;
    ProgressBar progressBar;
    RestaurantPagerAdapter pagerAdapter;
    RestaurantPagerAdapter pagerAdapter1;
    Context context;
    static String youtube_video_url;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    static FrameLayout frameLayout;
    static TextView pending_item;
    static CartNumberButton cartNumberButton;

    static List<Menu> course_meal1;
    static List<Menu> course_meal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_info);

        context = this;
        slidingUpPanelLayout = findViewById(R.id.slide_panel);
        dish_name = findViewById(R.id.dish_name);
        dish_price = findViewById(R.id.dish_price);
        dish_trending = findViewById(R.id.dish_trending);
        dish_rating = findViewById(R.id.dish_rating);
        dish_time = findViewById(R.id.dish_time);
        dish_discription = findViewById(R.id.dish_discription);
        frameLayout = findViewById(R.id.cart_layout);
        cartNumberButton = findViewById(R.id.elegantNumberButton);
        pending_item = findViewById(R.id.pending_item_textview);
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_player_fragment);
        pager = findViewById(R.id.dish_viewpager);
        pager2 = findViewById(R.id.dish_viewpager_second);
        progressBar=findViewById(R.id.progressBar);
        pager.setPageMargin(20);
        pager2.setPageMargin(20);

        dish_name.setVisibility(View.INVISIBLE);
        dish_price.setVisibility(View.INVISIBLE);
        dish_trending.setVisibility(View.INVISIBLE);
        dish_discription.setVisibility(View.INVISIBLE);
        dish_rating.setVisibility(View.INVISIBLE);
        dish_time.setVisibility(View.INVISIBLE);
        cartNumberButton.setVisibility(View.INVISIBLE);
        youTubePlayerFragment.setMenuVisibility(true);
        pager.setVisibility(View.INVISIBLE);
        pager2.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        animationUtils = AnimationUtils.loadAnimation(DishInfoActivity.this, R.anim.fade_in);
        animation = AnimationUtils.loadAnimation(DishInfoActivity.this, R.anim.fade_out);
        menuList = new ArrayList<>();

        cartNumberButton.setViewVisibility(View.GONE);
        cartNumberButton.setOnValueChangeListener(new CartNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(CartNumberButton view, int oldValue, int newValue) {

                if (newValue != 0)
                    cartNumberButton.updateColors(getResources().getColor(R.color.lime_green), Color.WHITE);
                else
                    cartNumberButton.updateColors(getResources().getColor(R.color.sky_color), Color.WHITE);
                add_item_to_cart(menu1, newValue);

            }
        });



        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Menu, Integer> cartitem = PassingCartItem.getMenuHashmap();
                PassingCartItem.menuArrayList.clear();
                PassingCartItem.integerArrayList.clear();
                for (Map.Entry<Menu, Integer> entry : cartitem.entrySet()) {
                    if (entry.getValue() != 0) {
                        PassingCartItem.addMenuArrayList(entry.getKey(), entry.getValue());
                        System.out.println("Rishabh" + entry.getKey() + " = " + entry.getValue());
                    }
                }
                startActivity(new Intent(DishInfoActivity.this, NewCartActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        Intent intent = getIntent();
        String resturant_id = intent.getStringExtra("rest");
        String dish = intent.getStringExtra("dish");
        final String finalDish;
        if (dish == null) {
            finalDish = "Wrong";
        } else {
            finalDish = dish;
        }


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("menu/" + resturant_id);
        Log.d("RISHABH", "Calling listener DISH ");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot child2 : child1.getChildren()) {
                        Menu menu2 = child2.getValue(Menu.class);
                        if (finalDish.equals(menu2.getName())) {
                            menu1 = menu2;
                        } else {
                            Log.d("RISHABH", "DATA IS DISH " + menu2.getRestaurantName() + " " + menu2.getPrice() + " " + menu2.getDesc());
                            if (menu2.getHas_video() == 0)
                                if (menu3 == null) {
                                    menu3 = menu2;
                                } else {
                                    menuList.add(menu2);
                                }
                        }
                    }
                }

                if (finalDish.equals("Wrong")) {
                    menu3.getRestaurantName();
                    youtube_video_url = menu3.getVideo_url();
                    dish_name.setText(menu3.getName());
                    dish_price.setText("₹ " + menu3.getPrice());
                    dish_discription.setText(menu3.getDesc());
                    play_youtube_video(menu3.getVideo_url());
                    visibleView();
                } else {
                    menu1.getRestaurantName();
                    youtube_video_url = menu1.getVideo_url();
                    dish_name.setText(menu1.getName());
                    dish_price.setText("₹ " + menu1.getPrice());
                    dish_discription.setText(menu1.getDesc());
                    play_youtube_video(menu1.getVideo_url());
                    visibleView();
                }




                //seperate the data for the two view pager from the menulist and pass the half data to menu 1 and menu  2
                 course_meal1 = new ArrayList<>();
                 course_meal2 = new ArrayList<>();
                for (int i = 0; i <= menuList.size() - 1; i++) {
                    if (menuList.get(i).getCourse_meal() == 1)
                        course_meal1.add(menuList.get(i));
                    else
                        course_meal2.add(menuList.get(i));
                }
                Log.d("RISHABH", "GOING TO THE ADAPTER ");
                pagerAdapter = new RestaurantPagerAdapter(getSupportFragmentManager(), course_meal1, getApplicationContext(),1);
                pager.setOffscreenPageLimit(10);
                pager.setAdapter(pagerAdapter);
                pagerAdapter1 = new RestaurantPagerAdapter(getSupportFragmentManager(), course_meal2, getApplicationContext(),2);
                pager.setOffscreenPageLimit(10);
                pager2.setAdapter(pagerAdapter1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void visibleView()
    {
        dish_name.setVisibility(View.VISIBLE);
        dish_price.setVisibility(View.VISIBLE);
        dish_trending.setVisibility(View.VISIBLE);
        dish_discription.setVisibility(View.VISIBLE);
        dish_rating.setVisibility(View.VISIBLE);
        dish_time.setVisibility(View.VISIBLE);
        cartNumberButton.setVisibility(View.VISIBLE);
        youTubePlayerFragment.setMenuVisibility(false);
        pager.setVisibility(View.VISIBLE);
        pager2.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }



    public static void play_youtube_video(final String url) {
        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer1 = player;
                    youTubePlayer1.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

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


    private void updatehasmap()
    {
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

        int totalprice = 0;
        inc = 0;
        for (Menu menu : cartitem.keySet()) {
            ++inc;
            totalprice += Integer.parseInt(menu.getPrice()) * temp[inc];
            Log.d("RISHABH", "VALUE IS THE " + menu.getPrice() + " " + menu.getName());
        }
        pending_item.setText("Pending " + totalitem + " Item | ₹" + totalprice);

        if (totalitem == 0) {
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


        public RestaurantPagerAdapter(FragmentManager fm, List<Menu> mFood, Context context,int coursemeal) {
            super(fm);
            this.fragmentManager = fm;
            this.mFood = mFood;
            this.context = context;
            this.coursemeal=coursemeal;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ResturantMenuFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt("dishPosition", i );
            args.putString("videoId", "1fwwqY9293s");
            args.putInt("coursemeal",coursemeal);
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
            return (float) 900 / size.x;
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
        public TextView min_rest;
        public static CartNumberButton numberButton;
        public int coursemeal;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.restaurant_menu_viewpager, container, false);

            thumnail = view.findViewById(R.id.thumbnail);
            textView = view.findViewById(R.id.dish_name_menu);
            price_rest = view.findViewById(R.id.price_rest);
            min_rest = view.findViewById(R.id.rating_rest_viewpager);
            numberButton = view.findViewById(R.id.element_btn_viewpager);

            Bundle args = getArguments();
            position = (int) args.getInt("dishPosition");
            dishes = (Menu) args.getSerializable("dish");
            coursemeal=args.getInt("coursemeal");
            textView.setText(dishes.getName());
            Picasso.with(getActivity().getApplicationContext()).load(dishes.getPoster_url()).into(thumnail);
            price_rest.setText(dishes.getPrice());
            min_rest.setText("5 Star");

            thumnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DishInfoActivity.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    DishInfoActivity.youTubePlayer1.release();
                    DishInfoActivity.play_youtube_video(dishes.getVideo_url());

                    dish_name.setText(dishes.getName());
                    dish_price.setText("₹ " + dishes.getPrice());
                    dish_discription.setText(dishes.getDesc());
                    cartNumberButton.setNumber(dishes.getTotalcartItem()+"");

                }
            });

            numberButton.setNumber(dishes.getTotalcartItem()+"");

            numberButton.setOnValueChangeListener(new CartNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(CartNumberButton view, int oldValue, int newValue) {

                        if(coursemeal==1)
                        {
                            course_meal1.get(position).setTotalcartItem(newValue);
                        }
                        else
                        {
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
        if(course_meal1!=null && course_meal2!=null) {

            pagerAdapter = new RestaurantPagerAdapter(getSupportFragmentManager(), course_meal1, getApplicationContext(), 1);
            pager.setOffscreenPageLimit(10);
            pager.setAdapter(pagerAdapter);
            pagerAdapter1 = new RestaurantPagerAdapter(getSupportFragmentManager(), course_meal2, getApplicationContext(), 2);
            pager.setOffscreenPageLimit(10);
            pager2.setAdapter(pagerAdapter1);
            updatehasmap();
        }
    }
}



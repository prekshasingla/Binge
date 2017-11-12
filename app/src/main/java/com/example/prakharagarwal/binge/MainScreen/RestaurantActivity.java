package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.StoriesMenu.Menu;
import com.example.prakharagarwal.binge.StoriesMenu.MenuAdapter;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RestaurantActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, MenuAdapter.Callback, RestaurantBottomAdapter.CallCategory {

    private RecyclerView bottomRecycler;
    List<Menu> menus;
    List<String> categories;
    List<Category> categoryList;
    private RecyclerView menuRecyler;
    private MenuAdapter menuAdapter;
    Restaurant restaurant;
    RestaurantBottomAdapter restaurantBottomAdapter;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    Menu selectedItem;

    private YouTubePlayerView youTubeView;
    YouTubePlayer youTubePlayer;

    String ID;
    TextView resName;
    TextView desc;
    TextView dishName;
    TextView price;
    ImageView veg;
    ImageView share;
    TextView location;
    TextView call;

    TextView heading;
    TextView reviews;

    TextView restaurantCategory;
    TextView restaurantLocation;
    TextView restaurantCostForTwo;
    TextView restaurantOpenClosed;

    private String compareStringOne = "9:45";
    private String compareStringTwo = "1:45";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);


        SlidingUpPanelLayout panel=(SlidingUpPanelLayout)findViewById(R.id.stories_sliding_up);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics());

        panel.setPanelHeight(height-(int)px);

        bottomRecycler=(RecyclerView)findViewById(R.id.restaurant_bottom_recycler);
        bottomRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        restaurantBottomAdapter= new RestaurantBottomAdapter(categories,this,bottomRecycler.getLayoutManager());
        bottomRecycler.setAdapter(restaurantBottomAdapter);

        menus = new ArrayList<Menu>();
        categories=new ArrayList<String>();
        categoryList=new ArrayList<Category>();
        selectedItem=new Menu();
        restaurant = new Restaurant();

        resName = (TextView) findViewById(R.id.restaurant_name);
        resName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        desc = (TextView) findViewById(R.id.desc_selected);
        dishName = (TextView) findViewById(R.id.dish_name_selected);
        price = (TextView) findViewById(R.id.price_selected);
        veg = (ImageView) findViewById(R.id.veg_image_selected);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Nunito-SemiBold.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Nunito-Light.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Nunito-Regular.ttf");

        resName.setTypeface(typeface);
        desc.setTypeface(typeface);
        dishName.setTypeface(typeface);
        price.setTypeface(typeface);

        heading = (TextView)findViewById(R.id.heading);
        String htmlString="<u>MENU</u>";
        heading.setText(Html.fromHtml(htmlString));
        heading.setTypeface(typeface);
        reviews = (TextView)findViewById(R.id.item_reviews_textview);
        reviews.setTypeface(typeface2);



        call=(TextView)findViewById(R.id.call_item_textview);
        location=(TextView)findViewById(R.id.locate_item_textview);
        restaurantCategory=(TextView)findViewById(R.id.category);
        restaurantLocation=(TextView)findViewById(R.id.address);
        restaurantCostForTwo=(TextView)findViewById(R.id.resCostForTwo);
        restaurantOpenClosed=(TextView)findViewById(R.id.openClosed);

        call.setTypeface(typeface);
        location.setTypeface(typeface);
        restaurantCategory.setTypeface(typeface1);
        restaurantLocation.setTypeface(typeface1);
        restaurantCostForTwo.setTypeface(typeface1);
        restaurantOpenClosed.setTypeface(typeface1);

        ID = getIntent().getStringExtra("restaurantID");
        final String res_Name = getIntent().getStringExtra("restaurantName");
        selectedItem.setName(getIntent().getStringExtra("dishName"));

        resName.setText(res_Name);
        resName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dishName.setText(selectedItem.getName());


        menuRecyler = (RecyclerView) findViewById(R.id.menu_recycler);
        menuAdapter = new MenuAdapter(this,menus);
        menuRecyler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        try {
            menuRecyler.setAdapter(menuAdapter);
        } catch (NoClassDefFoundError e) {

        }
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        share = (ImageView) findViewById(R.id.share_button_stories);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Indulge in this exotic " + selectedItem.getName() + " at Restaurant- " + res_Name + " on Binge. https://play.google.com/store/apps/details?id=in.binge.android ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Binge");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }

    public void getData(DataSnapshot dataSnapshot) {
        for(DataSnapshot child0 : dataSnapshot.getChildren()) {
            if (child0.getKey().equals("menu")) {
                for (DataSnapshot child1 : child0.getChildren()) {
                    if (child1.getKey().equals(ID))
                        for (DataSnapshot child2 : child1.getChildren()) {
                            if (child2.getKey() != null) {
                                Category category = new Category();
                                category.setCategory(child2.getKey());
                                categories.add(child2.getKey());
                                for (DataSnapshot child3 : child2.getChildren()) {
                                    Menu menu = child3.getValue(Menu.class);
                                    category.addCategoryMenu(menu);
                                }
                                categoryList.add(category);
                            }
                        }
                }
            }
            if (child0.getKey().equals("table")) {
                for (DataSnapshot child1 : child0.getChildren()) {
                    if (child1.getKey().equals(ID)) {
                        for (DataSnapshot child2 : child1.getChildren()) {
                            if (child2.getKey().equals("hname")) {
                                restaurant.setName("" + child2.getValue());
                            }

                            if (child2.getKey().equals("h_address")) {
                                restaurant.setAddress("" + child2.getValue());
                            }
                            if (child2.getKey().equals("h_lat")) {
                                restaurant.setLattitude("" + child2.getValue());
                            }

                            if (child2.getKey().equals("h_lng")) {
                                restaurant.setLongitude("" + child2.getValue());
                            }

                            if (child2.getKey().equals("h_type_of_restaurant")) {
                                restaurant.setTypeOfRestaurant("" + child2.getValue());
                            }

                            if (child2.getKey().equals("hambience_etime")) {
                                restaurant.setAmbienceEndTime(Integer.parseInt("" + child2.getValue()));
                            }
                            if (child2.getKey().equals("hambience_stime")) {
                                restaurant.setAmbienceStartTime(Integer.parseInt("" + child2.getValue()));
                            }
                            if (child2.getKey().equals("hclosing_time")) {
                                restaurant.setClosingTime("" + child2.getValue());
                            }
                            if (child2.getKey().equals("hopening_time")) {
                                restaurant.setOpeningTime("" + child2.getValue());
                            }
                            if (child2.getKey().equals("hcuisine_type")) {
                                restaurant.setCuisineType("" + child2.getValue());
                            }
                            if (child2.getKey().equals("hsignature_etime")) {
                                restaurant.setSignatureEndTime(Integer.parseInt("" + child2.getValue()));
                            }
                            if (child2.getKey().equals("hsignature_stime")) {
                                restaurant.setSignatureStartTime(Integer.parseInt("" + child2.getValue()));
                            }
                            if (child2.getKey().equals("hid")) {
                                restaurant.setId("" + child2.getValue());
                            }
                            if (child2.getKey().equals("h_cost_for_two")) {
                                restaurant.setCostForTwo(Integer.parseInt("" + child2.getValue()));
                            }
                            if (child2.getKey().equals("h_phone")) {
                                restaurant.setPhone("" + child2.getValue());
                            }
                        }
                    }
                }
            }
        }

        if(categoryList.size()>0)
         addAllMenus(categoryList.get(0).getCategoryMenu());
        addAllCategories(categories);

        if(selectedItem.getName()!=null){
            for(int i=0;i<categoryList.size();i++){
                for(int j=0;j<categoryList.get(i).getCategoryMenu().size();j++) {
                    if (selectedItem.getName().equalsIgnoreCase(categoryList.get(i).getCategoryMenu().get(j).getName())) {

                        selectedItem.setDesc(categoryList.get(i).getCategoryMenu().get(j).getDesc());
                        selectedItem.setHas_video(categoryList.get(i).getCategoryMenu().get(j).getHas_video());
                        selectedItem.setPrice(categoryList.get(i).getCategoryMenu().get(j).getPrice());
                        selectedItem.setVeg(categoryList.get(i).getCategoryMenu().get(j).getVeg());
                        selectedItem.setVideo_url(categoryList.get(i).getCategoryMenu().get(j).getVideo_url());

                        youTubePlayer.loadVideo(selectedItem.getVideo_url());
                       // youTubePlayer.play();

                        dishName.setText(selectedItem.getName());
                        desc.setText(selectedItem.getDesc());
                        price.setText(selectedItem.getPrice());
                        if (selectedItem.getVeg() != null) {
                            if (selectedItem.getVeg() == 0) {
                                veg.setImageResource(R.mipmap.veg);
                                dishName.setTextColor(getResources().getColor(R.color.veg_active));
                            } else {
                                veg.setImageResource(R.mipmap.nonveg);
                                dishName.setTextColor(getResources().getColor(R.color.nonveg_active));
                            }
                        }
                       // youTubePlayer.play();
                    }
                }
            }
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?daddr=" + restaurant.getLattitude() + "," + restaurant.getLongitude();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = restaurant.getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +number));
                startActivity(intent);
            }
        });

        restaurantCategory.setText(restaurant.getTypeOfRestaurant());
        restaurantLocation.setText(restaurant.getAddress());
        restaurantCostForTwo.setText("Cost for two: "+restaurant.getCostForTwo());


        if(compareDates()){
            restaurantOpenClosed.setText(""+ Html.fromHtml(compareStringOne + "-" + compareStringTwo + " hrs"));
        }else{
            restaurantOpenClosed.setText(""+Html.fromHtml(compareStringOne + "-" + compareStringTwo + " hrs"));
        }
        restaurantOpenClosed=(TextView)findViewById(R.id.openClosed);
    }


    public void addAllCategories(List<String> categories) {
        restaurantBottomAdapter.addAll(categories);
        restaurantBottomAdapter.notifyDataSetChanged();
//        checkIfEmpty();

    }

    private boolean compareDates() {

        try {
            Date time1 = new SimpleDateFormat("HH:mm").parse(compareStringOne);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);

            Date time2 = new SimpleDateFormat("HH:mm").parse(compareStringTwo);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);

            calendar2.add(Calendar.DATE, 1);

            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);
            String a =hour+":"+minute;
            Date d = new SimpleDateFormat("HH:mm").parse(a);
            now.setTime(d);
            now.add(Calendar.DATE, 1);

            Date x = now.getTime();

            if(calendar1.get(Calendar.HOUR_OF_DAY)>calendar2.get(Calendar.HOUR_OF_DAY)){
                if(now.get(Calendar.HOUR_OF_DAY)<calendar2.get(Calendar.HOUR_OF_DAY)){
                    return true;
                }
                int hr =calendar2.get(Calendar.HOUR_OF_DAY)+24;
                int min = calendar2.get(Calendar.MINUTE);
                String b = hr+":"+min;
                Date d1 = new SimpleDateFormat("HH:mm").parse(b);
                calendar2.setTime(d1);
                calendar2.add(Calendar.DATE, 1);
            }
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                return true;
            }
            return false;

        } catch (ParseException e){
            e.printStackTrace();

        }
        return true;
    }
    public void addAllMenus(List<com.example.prakharagarwal.binge.StoriesMenu.Menu> menus) {
        menuAdapter.addAll(menus);
        menuAdapter.notifyDataSetChanged();
//        checkIfEmpty();


    }

    @Override
    public void selectCategory(String categoryName) {
        for(int i=0;i<categoryList.size();i++) {
            if(categoryList.get(i).getCategory().equals(categoryName)) {
                menuAdapter.addAll(categoryList.get(i).getCategoryMenu());
                menuAdapter.notifyDataSetChanged();
            }
        }
//        checkIfEmpty();
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
            if(selectedItem.getVideo_url()!=null)
             player.loadVideo(selectedItem.getVideo_url());
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            youTubePlayer=player;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void showStory(Context context, Menu menu) {
        selectedItem.setName(menu.getName());
        selectedItem.setDesc(menu.getDesc());
        selectedItem.setHas_video(menu.getHas_video());
        selectedItem.setPrice(menu.getPrice());
        selectedItem.setVeg(menu.getVeg());
        selectedItem.setVideo_url(menu.getVideo_url());

        youTubePlayer.loadVideo(selectedItem.getVideo_url());
//        youTubePlayer.cueVideo(selectedItem.getVideo_url());
//        youTubePlayer.play();

        dishName.setText(selectedItem.getName());
        desc.setText(selectedItem.getDesc());
        price.setText(selectedItem.getPrice());
        if (selectedItem.getVeg() != null) {
            if (selectedItem.getVeg() == 0) {
                veg.setImageResource(R.mipmap.veg);
                dishName.setTextColor(getResources().getColor(R.color.veg_active));
            } else {
                veg.setImageResource(R.mipmap.nonveg);
                dishName.setTextColor(getResources().getColor(R.color.nonveg_active));
            }
        }
    }

}



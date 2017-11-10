package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, MenuAdapter.Callback, RestaurantBottomAdapter.CallCategory {

    private RecyclerView bottomRecycler;
    List<Menu> menus;
    List<String> categories;
    List<Category> categoryList;
    private RecyclerView menuRecyler;
    private MenuAdapter menuAdapter;
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
    String dish_Name="";
    int posit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        bottomRecycler=(RecyclerView)findViewById(R.id.restaurant_bottom_recycler);
        restaurantBottomAdapter= new RestaurantBottomAdapter(categories,this);
        bottomRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        bottomRecycler.setAdapter(restaurantBottomAdapter);

        menus = new ArrayList<Menu>();
        categories=new ArrayList<String>();
        categoryList=new ArrayList<Category>();
        selectedItem=new Menu();

        resName = (TextView) findViewById(R.id.restaurant_name);
        desc = (TextView) findViewById(R.id.desc_selected);
        dishName = (TextView) findViewById(R.id.dish_name_selected);
        price = (TextView) findViewById(R.id.price_selected);
        veg = (ImageView) findViewById(R.id.veg_image_selected);

        ID = getIntent().getStringExtra("restaurantID");

        final String res_Name = getIntent().getStringExtra("restaurantName");
        // posit = getIntent().getIntExtra("posi", 0);

        //final String res_Name="Too Indian";

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
        menuAdapter = new MenuAdapter(this, menus);
        menuRecyler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        try {
            menuRecyler.setAdapter(menuAdapter);
        } catch (NoClassDefFoundError e) {

        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("menu");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

//                Log.e("cancelled","cancel");
            }
        });


        if(selectedItem.getName()!=null){
            for(int i=0;i<categoryList.size();i++){
                for(int j=0;j<categoryList.get(i).getCategoryMenu().size();j++) {
                    if (dish_Name.equals(categoryList.get(i).getCategoryMenu().get(j))) {

                        selectedItem.setDesc(categoryList.get(i).getCategoryMenu().get(j).getDesc());
                        selectedItem.setHas_video(categoryList.get(i).getCategoryMenu().get(j).getHas_video());
                        selectedItem.setPrice(categoryList.get(i).getCategoryMenu().get(j).getPrice());
                        selectedItem.setVeg(categoryList.get(i).getCategoryMenu().get(j).getVeg());
                        selectedItem.setVideo_url(categoryList.get(i).getCategoryMenu().get(j).getVideo_url());
                        youTubePlayer.cueVideo(categoryList.get(i).getCategoryMenu().get(j).getVideo_url());
                        youTubePlayer.play();

                        dishName.setText(selectedItem.getName());
                        desc.setText(selectedItem.getDesc());
                        price.setText(selectedItem.getPrice());
                        if (selectedItem.getVeg() != null) {
                            if (selectedItem.getVeg() == 0) {
                                veg.setImageResource(R.mipmap.veg);
                            } else {
                                veg.setImageResource(R.mipmap.nonveg);
                            }
                        }


                    }
                }
            }
        }

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

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

       // Log.e("data",dataSnapshot+"");
        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
            if (child1.getKey().equals(ID))
                for (DataSnapshot child2 : child1.getChildren()) {
                    if (child2.getKey() != null) {
                        Category category=new Category();
                        category.setCategory(child2.getKey());
                        categories.add(child2.getKey());
                        for (DataSnapshot child3 : child2.getChildren()) {
                            Menu menu = child3.getValue(Menu.class);
                            category.addCategoryMenu(menu);
                            //menus.add(menu);
//                            Log.e("me",menu.getName());
                        }
                        //category.setCategoryMenu(menus);
                        categoryList.add(category);
//                        addAllMenus(category.getCategoryMenu());
                    }
                }
        }
        //addAllMenus(menus);
        if(categoryList.size()>0)
         addAllMenus(categoryList.get(0).getCategoryMenu());
        addAllCategories(categories);

        if(selectedItem.getName()!=null){
            for(int i=0;i<categoryList.size();i++){
                for(int j=0;j<categoryList.get(i).getCategoryMenu().size();j++) {
                    if (dish_Name.equals(categoryList.get(i).getCategoryMenu().get(j))) {

                        selectedItem.setDesc(categoryList.get(i).getCategoryMenu().get(j).getDesc());
                        selectedItem.setHas_video(categoryList.get(i).getCategoryMenu().get(j).getHas_video());
                        selectedItem.setPrice(categoryList.get(i).getCategoryMenu().get(j).getPrice());
                        selectedItem.setVeg(categoryList.get(i).getCategoryMenu().get(j).getVeg());
                        selectedItem.setVideo_url(categoryList.get(i).getCategoryMenu().get(j).getVideo_url());
                        youTubePlayer.cueVideo(categoryList.get(i).getCategoryMenu().get(j).getVideo_url());
                        youTubePlayer.play();

                        dishName.setText(selectedItem.getName());
                        desc.setText(selectedItem.getDesc());
                        price.setText(selectedItem.getPrice());
                        if (selectedItem.getVeg() != null) {
                            if (selectedItem.getVeg() == 0) {
                                veg.setImageResource(R.mipmap.veg);
                            } else {
                                veg.setImageResource(R.mipmap.nonveg);
                            }
                        }


                    }
                }
            }
        }

    }

    public void addAllCategories(List<String> categories) {
        restaurantBottomAdapter.addAll(categories);
        restaurantBottomAdapter.notifyDataSetChanged();
//        checkIfEmpty();

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

        youTubePlayer.cueVideo(selectedItem.getVideo_url());
        youTubePlayer.play();

        selectedItem.setName(menu.getName());
        selectedItem.setDesc(menu.getDesc());
        selectedItem.setHas_video(menu.getHas_video());
        selectedItem.setPrice(menu.getPrice());
        selectedItem.setVeg(menu.getVeg());
        selectedItem.setVideo_url(menu.getVideo_url());

        dishName.setText(selectedItem.getName());
        desc.setText(selectedItem.getDesc());
        price.setText(selectedItem.getPrice());
        if (selectedItem.getVeg() != null) {
            if (selectedItem.getVeg() == 0) {
                veg.setImageResource(R.mipmap.veg);
            } else {
                veg.setImageResource(R.mipmap.nonveg);
            }
        }
        //curr = 0;
    }

}



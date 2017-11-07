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
        YouTubePlayer.OnInitializedListener, MenuAdapter.Callback {

    private RecyclerView bottomRecycler;
    List<com.example.prakharagarwal.binge.StoriesMenu.Menu> menus;
    private RecyclerView menuRecyler;
    private MenuAdapter menuAdapter;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    Menu selectedItem;

    private YouTubePlayerView youTubeView;
    YouTubePlayer youTubePlayer;



    String ID;
    TextView resName;
    TextView desc;
    TextView dishName;
    ImageView veg;
    ImageView share;
    String dish_Name="";
    int posit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);


        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);


        bottomRecycler=(RecyclerView)findViewById(R.id.restaurant_bottom_recycler);
        RestaurantBottomAdapter restaurantBottomAdapter= new RestaurantBottomAdapter(null,this);
        bottomRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        bottomRecycler.setAdapter(restaurantBottomAdapter);

        menus = new ArrayList<>();
        selectedItem=new Menu();


        resName = (TextView) findViewById(R.id.restaurant_name);
        //desc = (TextView) findViewById(R.id.frag_stories_desc);
        //dishName = (TextView) findViewById(R.id.frag_stories_dish);
        //veg = (ImageView) findViewById(R.id.frag_stories_veg_image);


        ID = getIntent().getStringExtra("restaurantID");

        String res_Name = getIntent().getStringExtra("restaurantName");
       // selectedItem.setName(getIntent().getStringExtra("dishName"));
//        Log.i("TAG", dish_Name);
       // posit = getIntent().getIntExtra("posi", 0);

        res_Name="Too Indian";
        selectedItem.setName("Chit Chat");

        resName.setText(res_Name);

        resName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


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

            }
        });

        if(selectedItem.getName()!=null){
            for(int i=0;i<menus.size();i++){
                if(dish_Name.equals(menus.get(i).getName())){

                    selectedItem.setDesc(menus.get(i).getDesc());
                    selectedItem.setHas_video(menus.get(i).getHas_video());
                    selectedItem.setPrice(menus.get(i).getPrice());
                    selectedItem.setVeg(menus.get(i).getVeg());
                    selectedItem.setVideo_url(menus.get(i).getVideo_url());
//                    youTubePlayer.cueVideo(selectedItem.getVideo_url());
//                    youTubePlayer.play();

                    //curr=i;
                }
            }
        }

        share = (ImageView) findViewById(R.id.share_button_stories);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //String shareBody = "Indulge in this exotic " + menus.get(curr).getName() + " at Restaurant- " + res_Name + " on Binge. https://play.google.com/store/apps/details?id=in.binge.android ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Binge");
                //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

    }

    public void getData(DataSnapshot dataSnapshot) {

        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
            if (child1.getKey().equals("too_indian_delhi"))
                for (DataSnapshot child2 : child1.getChildren()) {
                    if (child2.getKey() != null) {
                        for (DataSnapshot child3 : child2.getChildren()) {
                            com.example.prakharagarwal.binge.StoriesMenu.Menu menu = child3.getValue(com.example.prakharagarwal.binge.StoriesMenu.Menu.class);
                            menus.add(menu);
                        }
                    }
                }
        }
        addAllMenus(menus);
    }
    public void addAllMenus(List<com.example.prakharagarwal.binge.StoriesMenu.Menu> menus) {
        menuAdapter.addAll(menus);
        menuAdapter.notifyDataSetChanged();
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

            //youTubePlayer=player;
            if(selectedItem.getVideo_url()!=null)
             player.loadVideo(selectedItem.getVideo_url());
            else
                player.loadVideo("ruHMqQJ6PNE");
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
       // slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        //webView.loadDataWithBaseURL("", getYoutubeURL(menu.getVideo_url()), "text/html", "UTF-8", "");
       // youTubePlayer.cueVideo(selectedItem.getVideo_url());

        selectedItem.setName(menu.getName());
        selectedItem.setDesc(menu.getDesc());
        selectedItem.setHas_video(menu.getHas_video());
        selectedItem.setPrice(menu.getPrice());
        selectedItem.setVeg(menu.getVeg());
        selectedItem.setVideo_url(menu.getVideo_url());

        youTubePlayer.cueVideo(selectedItem.getVideo_url());
        youTubePlayer.play();
//        dishName.setText(menu.getName());
//        desc.setText(menu.getDesc());
        if (menu.getVeg() != null) {
            if (menu.getVeg() == 0) {
 //               veg.setImageResource(R.mipmap.veg);
            } else {
  //              veg.setImageResource(R.mipmap.nonveg);
            }
        }
        //curr = 0;
    }

}



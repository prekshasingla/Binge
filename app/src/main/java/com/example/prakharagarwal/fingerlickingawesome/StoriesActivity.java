package com.example.prakharagarwal.fingerlickingawesome;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.sothree.slidinguppanel.ScrollableViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;


public class StoriesActivity extends FragmentActivity {


    RecyclerView mRecyclerView;
    MenuAdapter menuAdapter;
    List<Menu> menus;
    SlidingUpPanelLayout slidingUpPanelLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        menus=new ArrayList<Menu>();

        slidingUpPanelLayout=(SlidingUpPanelLayout)findViewById(R.id.stories_sliding_up);

       slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
           @Override
           public void onPanelSlide(View panel, float slideOffset) {

           }

           @Override
           public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            if(newState== SlidingUpPanelLayout.PanelState.EXPANDED)
                ((ImageView)findViewById(R.id.stories_heading_image)).setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);


               if(newState== SlidingUpPanelLayout.PanelState.COLLAPSED)
                   ((ImageView)findViewById(R.id.stories_heading_image)).setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);

           }
       });

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);

        mRecyclerView=(RecyclerView)findViewById(R.id.menu_fragment_recycler_view);
        menuAdapter= new MenuAdapter(this,menus);
        try{
            mRecyclerView.setAdapter(menuAdapter);
        }catch (NoClassDefFoundError e){

        }
        mRecyclerView.setLayoutManager(linearLayoutManager);

        for(int i=0;i<10;i++) {
            Menu menu = new Menu("Name", "Description", "Price", true);
            menus.add(menu);
        }

        menuAdapter.addAll(menus);
        menuAdapter.notifyDataSetChanged();
    }

    public SlidingUpPanelLayout getSlidingUpPanelLayout(){
        return slidingUpPanelLayout;
    }

}

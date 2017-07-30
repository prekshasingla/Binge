package com.example.prakharagarwal.fingerlickingawesome;

import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        menus=new ArrayList<Menu>();

        SlidingUpPanelLayout slidingUpPanelLayout=(SlidingUpPanelLayout)findViewById(R.id.stories_sliding_up);
        //slidingUpPanelLayout.setDragView(R.id.stories_heading);
        //slidingUpPanelLayout.setAnchorPoint(0.75f);


        //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);


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
}

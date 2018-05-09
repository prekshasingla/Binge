package com.example.prakharagarwal.binge.MainScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.prakharagarwal.binge.DishRecommend;
import com.example.prakharagarwal.binge.LoginActivity;
import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FoodMainScreenAdapter extends RecyclerView.Adapter<FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 1;
    private static final int ITEM_VIEW_TYPE_ITEM = 2;
    private List<Menu> mFood;
    final private Activity mContext;

    FoodMainScreenAdapterViewHolder holder;
    RecyclerView recyclerView;
    private boolean headerEnabled;

    public FoodMainScreenAdapter(List<Menu> mFood, Activity mContext, RecyclerView recyclerView) {
        this.mFood = mFood;
        this.mContext = mContext;
        this.recyclerView = recyclerView;

    }



    @Override
    public FoodMainScreenAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_main_screen, parent, false);
        holder = new FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(FoodMainScreenAdapterViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {

        return mFood.size();
    }


    public class FoodMainScreenAdapterViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView image;
        public TextView title;
        public TextView description;
        public FrameLayout videoContainer;

        public FoodMainScreenAdapterViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            videoContainer = (FrameLayout) itemView.findViewById(R.id.video_container);
        }
    }


    public void addAll(List<Menu> food) {
        mFood = food;
    }
}

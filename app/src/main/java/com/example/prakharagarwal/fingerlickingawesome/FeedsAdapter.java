package com.example.prakharagarwal.fingerlickingawesome;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

/**
 * Created by prakharagarwal on 25/06/17.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedsAdapterViewHolder> {
    private static final int RECOVERY_REQUEST = 1;


    private List<String> mVideos;
    final private Context mContext;
    FeedsAdapterViewHolder holder;
    FragmentManager fragmentManager;
    int currId = 0;


    public FeedsAdapter(Context context, List<String> videos, FragmentManager fragmentManager) {
        mContext = context;
        mVideos = videos;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public FeedsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_adapter_card_item, parent, false);
        //view.setFocusable(true);
        holder = new FeedsAdapterViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final FeedsAdapterViewHolder holder, int position) {


        final String video = mVideos.get(position);
        final int id = 100 + position;
        holder.youTubeView.setId(id);

        fragmentManager.beginTransaction().replace(id,new BlankFragment()).commit();


    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }


    public class FeedsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final FrameLayout youTubeView;
        //public final FrameLayout container;


        public FeedsAdapterViewHolder(View view) {
            super(view);


            youTubeView = (FrameLayout) view.findViewById(R.id.youtube_thumbnail);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                    if (!b) {
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                        youTubePlayer.cueVideo("jzbMxWvnlGw"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo

                    }

                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
                    if (errorReason.isUserRecoverableError()) {
                        //errorReason.getErrorDialog(mContext, RECOVERY_REQUEST).show();
                    } else {
                        String error = String.format(mContext.getString(R.string.player_error), errorReason.toString());
                        Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                    }
                }

            });
            if (currId == 0) {

            } else {

                fragmentManager.beginTransaction().replace(currId,new BlankFragment()).commit();
            }
            int id = getAdapterPosition() + 100;
            currId=id;
            fragmentManager.beginTransaction().replace(id, youTubePlayerFragment).commit();

        }
    }

    public void addAll(List<String> videos) {
        mVideos = videos;

    }


}

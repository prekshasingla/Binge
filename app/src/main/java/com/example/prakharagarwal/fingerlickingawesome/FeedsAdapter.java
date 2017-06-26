package com.example.prakharagarwal.fingerlickingawesome;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

/**
 * Created by prakharagarwal on 25/06/17.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedsAdapterViewHolder> {

    private static final int RECOVERY_REQUEST = 1;


    private List<String> mVideos;
    final private Context mContext;
    FeedsAdapterViewHolder holder;


    public FeedsAdapter(Context context, List<String> videos) {
        mContext = context;
        mVideos = videos;
    }


    @Override
    public FeedsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_adapter_card_item, parent,false);
            //view.setFocusable(true);
            holder = new FeedsAdapterViewHolder(view);
            return holder;

    }

    @Override
    public void onBindViewHolder(FeedsAdapterViewHolder holder, int position) {


        String video= mVideos.get(position);
        /*holder.youTubeView.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                if (!b) {
                    //player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

                    youTubePlayer.cueVideo("tOoOI0KdUw4"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo

                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
                if (errorReason.isUserRecoverableError()) {
                    //errorReason.getErrorDialog(mContext, RECOVERY_REQUEST).show();
                } else {
                    String error = String.format(mContext.getString(R.string.player_error), errorReason.toString());
                    Toast.makeText(mContext,"Error", Toast.LENGTH_LONG).show();
                }
            }

        });*/


        holder.t2.setText(video);

    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }


    public class FeedsAdapterViewHolder extends RecyclerView.ViewHolder {
       // public final YouTubePlayerView youTubeView;
        public final TextView t2;



        public FeedsAdapterViewHolder(View view) {
            super(view);


//            youTubeView = (YouTubePlayerView) view.findViewById(R.id.feeds_adapter_item_youtube_view);
            t2 = (TextView) view.findViewById(R.id.article_text);

        }


    }

    public void addAll(List<String> videos){
        mVideos=videos;

    }
}

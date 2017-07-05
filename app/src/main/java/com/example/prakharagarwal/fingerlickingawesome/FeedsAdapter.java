package com.example.prakharagarwal.fingerlickingawesome;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    RecyclerView recyclerView;
    int currId = -1;


    public FeedsAdapter(Context context, RecyclerView recyclerView, List<String> videos, FragmentManager fragmentManager) {
        mContext = context;
        mVideos = videos;
        this.recyclerView = recyclerView;
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
    public void onBindViewHolder(final FeedsAdapterViewHolder holder, final int position) {


        String video = mVideos.get(position);
        //video="ZZS4dd5VeXY";
        final int id = 100 + position;
      //  holder.youTubeView.setId(id);

        Bundle bundle = new Bundle();
        bundle.putString("video", video);
        BlankFragment blankFragment = new BlankFragment();
        blankFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(id, blankFragment).commit();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisible = linearLayoutManager.findFirstVisibleItemPosition() + 100;
                int lastVisible = linearLayoutManager.findLastVisibleItemPosition() + 100;
                final int visiblePos = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (visiblePos != -1) {
                    int id1 = 100 + visiblePos;
                    final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                        if (!b) {
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                            youTubePlayer.cueVideo(mVideos.get(visiblePos)); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo

                        }
                        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                            @Override
                            public void onLoading() {

                            }

                            @Override
                            public void onLoaded(String s) {

                                Log.e("Play","Done");
                                youTubePlayer.play();
                            }

                            @Override
                            public void onAdStarted() {

                            }

                            @Override
                            public void onVideoStarted() {

                            }

                            @Override
                            public void onVideoEnded() {

                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {

                            }
                        });

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


                 if (currId == -1) {

                    } else {
                        if (currId >= firstVisible && currId <= lastVisible) {
                            Bundle bundle = new Bundle();
                            bundle.putString("video", mVideos.get(currId - 100));
                            BlankFragment blankFragment = new BlankFragment();
                            blankFragment.setArguments(bundle);
                            fragmentManager.beginTransaction().replace(currId, blankFragment).commit();
                        }
                    }

                    fragmentManager.beginTransaction().replace(id1, youTubePlayerFragment).commit();
                    currId = id1;


                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });


    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }


    public class FeedsAdapterViewHolder extends RecyclerView.ViewHolder {

        //public final FrameLayout youTubeView;
        //public final FrameLayout container;


        public FeedsAdapterViewHolder(View view) {
            super(view);


       //     youTubeView = (FrameLayout) view.findViewById(R.id.youtube_thumbnail);
//            view.setOnClickListener(this);

        }

//        @Override
//        public void onClick(View view) {
//                final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//                youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
//                    @Override
//                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//
//                        if (!b) {
//                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
//                       /**/
//                            youTubePlayer.cueVideo(mVideos.get(getAdapterPosition())); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
//                        if (errorReason.isUserRecoverableError()) {
//                            //errorReason.getErrorDialog(mContext, RECOVERY_REQUEST).show();
//                        } else {
//                            String error = String.format(mContext.getString(R.string.player_error), errorReason.toString());
//                            Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                });
//                if (currId == 0) {
//
//                } else {
//
//                    //if (view.findViewById(currId)!= null) {
//                        String video = mVideos.get(currId - 100);
//                        // video="ZZS4dd5VeXY";
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("video", video);
//                        BlankFragment blankFragment = new BlankFragment();
//                        blankFragment.setArguments(bundle);
//                        fragmentManager.beginTransaction().replace(currId, blankFragment).commit();
//                    //}
//                    //else{
//                      //  Log.e("view", "null");
//                    //}
//                    }
//                    int id = getAdapterPosition() + 100;
//                    currId = id;
//                    fragmentManager.beginTransaction().replace(id, youTubePlayerFragment).commit();
//
//
//        }
    }

    public void addAll(List<String> videos) {
        mVideos = videos;

    }

    public void notifyItemSetChanged(int position, boolean hasDownloaded) {
        if (currId == position) {
            return;
        }
        currId = position;
        Log.d("notifyItemSetChanged","" + position);
        notifyItemChanged(position,"done");
    }


}
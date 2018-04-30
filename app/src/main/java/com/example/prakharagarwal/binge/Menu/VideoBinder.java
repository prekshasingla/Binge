package com.example.prakharagarwal.binge.Menu;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.prakharagarwal.binge.MainScreen.Config;
import com.example.prakharagarwal.binge.MainScreen.FoodMainScreenAdapter;
import com.example.prakharagarwal.binge.MainScreen.IFragmentManager;
import com.example.prakharagarwal.binge.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by prakharagarwal on 28/04/18.
 */

public class VideoBinder {
    private static final String TAG = VideoBinder.class.getSimpleName();
    private static final int HACK_ID_PREFIX = 12331293; //some random number
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private static YouTubePlayerSupportFragment youTubePlayerFragment;
    private static YouTubePlayer youTubePlayer;
    private static boolean isFullScreen = false;
    private Menu dish;

    private ImageRequest imageRequest;
    private Uri uri;

    VideoBinder(Menu dish) {
        this.dish = dish;
    }

    public void prepare() {
        if (!TextUtils.isEmpty(dish.imageUrl) && uri == null) {
            try {
                uri = Uri.parse(dish.imageUrl);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }

    public void bind(final FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder videoViewHolder, final IFragmentManager fragmentManager) {
        videoViewHolder.image.setAspectRatio(16f / 9f);
        if (imageRequest == null) {
            videoViewHolder.image.post(new Runnable() {
                @Override
                public void run() {
                    ImageRequestBuilder builder;
                    if (uri == null) {
                        builder = ImageRequestBuilder.newBuilderWithResourceId(android.R.color.darker_gray);
                    } else {
                        builder = ImageRequestBuilder.newBuilderWithSource(uri);
                    }
                    imageRequest = builder.setResizeOptions(new ResizeOptions(
                            videoViewHolder.image.getWidth(), videoViewHolder.image.getHeight()
                    )).build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(imageRequest)
                            .setOldController(videoViewHolder.image.getController())
                            .build();
                    videoViewHolder.image.setController(controller);
                }
            });
        } else {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setOldController(videoViewHolder.image.getController())
                    .build();
            videoViewHolder.image.setController(controller);
        }
        bindVideo(videoViewHolder, fragmentManager);
        bindTitle(videoViewHolder);
        bindDescription(videoViewHolder);
    }

    private void bindVideo(final FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder viewHolder,
                           final IFragmentManager fragmentManager) {
        View view = viewHolder.itemView.findViewWithTag(viewHolder.itemView.getContext()
                .getString(R.string.video_component_tag));
        if (view != null) {
            view.setId(HACK_ID_PREFIX + viewHolder.getAdapterPosition());
        }
        handleClick(viewHolder, fragmentManager);
    }

    private void bindTitle(final FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder videoViewHolder) {
        videoViewHolder.title.setText(dish.getVideo_url());
    }

    private void bindDescription(final FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder videoViewHolder) {
        videoViewHolder.description.setText(dish.getVideo_url());
    }

    private void handleClick(final FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder viewHolder,
                             final IFragmentManager fragmentManager) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(dish.getVideo_url())) {
                    return;
                }
                if (!YouTubeIntents.isYouTubeInstalled(view.getContext()) ||
                        YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(view.getContext()) != YouTubeInitializationResult.SUCCESS) {
                    if (YouTubeIntents.canResolvePlayVideoIntent(view.getContext())) {
                        fragmentManager.getSupportFragment().
                                startActivity(YouTubeIntents.createPlayVideoIntent(view.getContext(), dish.getVideo_url()));
                        return;
                    }
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + dish.getVideo_url()));
                    fragmentManager.getSupportFragment().startActivity(viewIntent);
                    return;
                }
                if (viewHolder.videoContainer.getChildCount() == 0) {
                    if (youTubePlayerFragment == null) {
                        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                    }
                    if (youTubePlayerFragment.isAdded()) {
                        if (VideoBinder.youTubePlayer != null) {
                            try {
                                VideoBinder.youTubePlayer.pause();
                                VideoBinder.youTubePlayer.release();
                            } catch (Exception e) {
                                if (VideoBinder.youTubePlayer != null) {
                                    try {
                                        VideoBinder.youTubePlayer.release();
                                    } catch (Exception ignore) {
                                    }

                                }
                            }
                            VideoBinder.youTubePlayer = null;
                        }

                        fragmentManager.getSupportFragmentManager()
                                .beginTransaction()
                                .remove(youTubePlayerFragment)
                                .commit();
                        fragmentManager.getSupportFragmentManager()
                                .executePendingTransactions();
                        youTubePlayerFragment = null;
                    }
                    if (youTubePlayerFragment == null) {
                        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                    }
                    fragmentManager.getSupportFragmentManager()
                            .beginTransaction()
                            .add(HACK_ID_PREFIX + viewHolder.getAdapterPosition(), youTubePlayerFragment)
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .commit();
                    youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY,
                            new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                    YouTubePlayer youTubePlayer, boolean b) {
                                    VideoBinder.youTubePlayer = youTubePlayer;
                                    VideoBinder.youTubePlayer.loadVideo(dish.getVideo_url());
                                    VideoBinder.youTubePlayer.setFullscreenControlFlags(0);
                                    VideoBinder.youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                        @Override
                                        public void onFullscreen(boolean b) {
                                            isFullScreen = b;
                                        }
                                    });
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {
                                    Log.e(VideoBinder.class.getSimpleName(), youTubeInitializationResult.name());
                                    if (YouTubeIntents.canResolvePlayVideoIntent(
                                            fragmentManager.getSupportFragment().getContext())) {
                                        fragmentManager.getSupportFragment()
                                                .startActivity(YouTubeIntents.createPlayVideoIntent(
                                                        fragmentManager.getSupportFragment().getContext(),
                                                        dish.getVideo_url()));
                                        return;
                                    }
                                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + dish.getVideo_url()));
                                    fragmentManager.getSupportFragment().startActivity(viewIntent);
                                }
                            });
                }
            }
        });
    }

    public void unBind(final FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder videoViewHolder, IFragmentManager fragmentManager) {
        if (videoViewHolder.videoContainer.getChildCount() > 0) {
            if (youTubePlayerFragment != null && youTubePlayerFragment.isAdded()) {
                if (VideoBinder.youTubePlayer != null) {
                    try {
                        VideoBinder.youTubePlayer.pause();
                        VideoBinder.youTubePlayer.release();
                    } catch (Exception e) {
                        if (VideoBinder.youTubePlayer != null) {
                            try {
                                VideoBinder.youTubePlayer.release();
                            } catch (Exception ignore) {}
                        }
                    }
                    VideoBinder.youTubePlayer = null;
                }

                fragmentManager.getSupportFragmentManager()
                        .beginTransaction()
                        .remove(youTubePlayerFragment)
                        .commit();
                fragmentManager.getSupportFragmentManager()
                        .executePendingTransactions();
                youTubePlayerFragment = null;
            }
        }
    }
}


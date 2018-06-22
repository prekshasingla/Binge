package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class DishInfoActivity extends AppCompatActivity implements
        YouTubePlayer.OnInitializedListener {


    SlidingUpPanelLayout slidingUpPanelLayout;
    TextView dish_name, dish_price, dish_trending, dish_rating, dish_time, dish_discription;
    static YouTubePlayerSupportFragment youTubePlayerFragment;
    static YouTubePlayer youTubePlayer1;
    Menu menu1;
    ViewPager pager;
    List<Menu> menuList;
    RestaurantPagerAdapter pagerAdapter;
    Context context;
    static String youtube_video_url;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_info);

        context = this;
        slidingUpPanelLayout = findViewById(R.id.slide_panel);
        dish_name = findViewById(R.id.dish_name);
        dish_price = findViewById(R.id.dish_price);
        dish_trending = findViewById(R.id.dish_trending);
        dish_rating = findViewById(R.id.dish_rating);
        dish_time = findViewById(R.id.dish_time);
        dish_discription = findViewById(R.id.dish_discription);
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_player_fragment);

        pager = findViewById(R.id.dish_viewpager);
        pager.setClipToPadding(false);
        pager.setPadding(60, 0, 60, 0);
        pager.setPageMargin(20);

        //get the menu data
        menu1 = PassingData.getMenu();
        menu1.getRestaurantName();
        youtube_video_url=menu1.getVideo_url();
        dish_name.setText(menu1.getName());
        dish_price.setText("₹ " + menu1.getPrice());
        dish_discription.setText(menu1.getDesc());
        menuList=new ArrayList<>();



        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer1 = player;
                    youTubePlayer1.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                     youTubePlayer1.cueVideo(menu1.getVideo_url());
//                    youTubePlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e("Rishabh", "Youtube Player View initialization failed");
            }
        });

//        if (youTubePlayer != null) {
//            youTubePlayer.loadVideo(menu1.getVideo_url());
//            youTubePlayer.play();
//        }

        Log.d("Rishabh",menu1.getRestaurant_id());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("menu/" + menu1.getRestaurant_id());
        Log.d("RISHABH","Calling listener");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot child2 : child1.getChildren()) {
                        Menu menu1 = child2.getValue(Menu.class);
                        Log.d("RISHABH" ,"DATA IS "+menu1.getRestaurantName()+" "+menu1.getPrice()+" "+menu1.getDesc());
                        if (menu1.getHas_video() == 0)
                            menuList.add(menu1);
                        }
                    }


                pagerAdapter = new RestaurantPagerAdapter(getSupportFragmentManager(), menuList, getApplicationContext());
                pager.setAdapter(pagerAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
            if (menu1.getVideo_url() != null)
                player.loadVideo(menu1.getVideo_url());
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            youTubePlayer1 = player;

        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RECOVERY_DIALOG_REQUEST) {
//            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
//        }
//    }
//
//    private YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return (YouTubePlayerView) findViewById(R.id.youtube_view);
//    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class RestaurantPagerAdapter extends FragmentStatePagerAdapter {
        FragmentManager fragmentManager;
        private List<Menu> mFood;
        Context context;


        public RestaurantPagerAdapter(FragmentManager fm, List<Menu> mFood, Context context) {
            super(fm);
            this.fragmentManager = fm;
            this.mFood = mFood;
            this.context = context;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ResturantMenuFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt(DishInfoActivity.ResturantMenuFragment.ARG_OBJECT, i + 1);
            args.putString("videoId", "1fwwqY9293s");
            Menu dish = mFood.get(i);
            args.putSerializable("dish", dish);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);

        }

        @Override
        public float getPageWidth(int position) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return (float) 900 / size.x;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

        }

        @Override
        public int getCount() {
            return mFood.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class ResturantMenuFragment extends Fragment {
        public static final String ARG_OBJECT = "object";
        private static final String TAG = DishInfoActivity.ResturantMenuFragment.class.getSimpleName();
        private static final int HACK_ID_PREFIX = 12331293; //some random number
        private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
        private static YouTubePlayerSupportFragment youTubePlayerSupportFragment;
        private static YouTubePlayer youTubePlayer;
        private static boolean isFullScreen = false;
        private Menu dishes;

        private ImageRequest imageRequest;
        private Uri uri;
        public int position;
        public ImageView imageView_menu;
        public FrameLayout frameLayout;
        public SimpleDraweeView draweeView;
        public TextView textView;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.restaurant_menu_viewpager, container, false);

            imageView_menu = view.findViewById(R.id.play_button_menu);
            frameLayout = view.findViewById(R.id.video_container_menu);
            draweeView = view.findViewById(R.id.drawer_menu);
            textView=view.findViewById(R.id.dish_name_menu);

            Bundle args = getArguments();
            position = (int) args.get(ARG_OBJECT);
            dishes = (Menu) args.getSerializable("dish");
            textView.setText(dishes.getName());

            prepare();
            return view;
        }


        public void prepare() {
            if (!TextUtils.isEmpty(dishes.getPoster_url()) && uri == null) {
                try {
                    uri = Uri.parse(dishes.getPoster_url());
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
            bind();
        }

        public void bind() {
            draweeView.setAspectRatio(16f / 9f);
            if (imageRequest == null) {
                draweeView.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageRequestBuilder builder;
                        if (uri == null) {
                            builder = ImageRequestBuilder.newBuilderWithResourceId(android.R.color.darker_gray);
                        } else {
                            builder = ImageRequestBuilder.newBuilderWithSource(uri);
                        }
                        imageRequest = builder.setResizeOptions(new ResizeOptions(
                                draweeView.getWidth(), draweeView.getHeight()
                        )).build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(imageRequest)
                                .setOldController(draweeView.getController())
                                .build();
                        draweeView.setController(controller);
                    }
                });
            } else {
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(imageRequest)
                        .setOldController(draweeView.getController())
                        .build();
                draweeView.setController(controller);
            }
            bindVideo();
//            bindDescription(videoViewHolder);
        }

        private void bindVideo() {
            View view = frameLayout;
            if (view != null) {
                view.setId(HACK_ID_PREFIX + position);
            }
            handleClick();
        }

        private void handleClick() {
            draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(DishInfoActivity.youTubePlayer1!=null)
                    {
                        Log.d("RISHABH","YouTube player is not the null");
                        DishInfoActivity.youTubePlayer1.pause();
                        DishInfoActivity.youTubePlayer1.release();

                        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                                boolean wasRestored) {
                                if (!wasRestored) {
                                    youTubePlayer1 = player;
                                    youTubePlayer1.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                                    youTubePlayer1.cueVideo(youtube_video_url);
//                    youTubePlayer.play();
                                }
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                                //print or show error if initialization failed
                                Log.e("Rishabh", "Youtube Player View initialization failed");
                            }
                        });







                    }
                    if (TextUtils.isEmpty(dishes.getVideo_url())) {
                        return;
                    }
                    if (!YouTubeIntents.isYouTubeInstalled(view.getContext()) ||
                            YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(view.getContext()) != YouTubeInitializationResult.SUCCESS) {
                        if (YouTubeIntents.canResolvePlayVideoIntent(view.getContext())) {
                            getActivity().
                                    startActivity(YouTubeIntents.createPlayVideoIntent(view.getContext(), dishes.getVideo_url()));
                            return;
                        }
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + dishes.getVideo_url()));
                        getActivity().startActivity(viewIntent);
                        return;
                    }
                    if (frameLayout.getChildCount() == 0) {
                        if (youTubePlayerSupportFragment == null) {
                            youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
                        }
                        if (youTubePlayerSupportFragment.isAdded()) {
                            if (DishInfoActivity.ResturantMenuFragment.youTubePlayer != null) {
                                try {
                                    DishInfoActivity.ResturantMenuFragment.youTubePlayer.pause();
                                    DishInfoActivity.ResturantMenuFragment.youTubePlayer.release();
                                } catch (Exception e) {
                                    if (DishInfoActivity.ResturantMenuFragment.youTubePlayer != null) {
                                        try {
                                            DishInfoActivity.ResturantMenuFragment.youTubePlayer.release();
                                        } catch (Exception ignore) {
                                        }

                                    }
                                }
                                DishInfoActivity.ResturantMenuFragment.youTubePlayer = null;
                            }

                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .remove(youTubePlayerSupportFragment)
                                    .commit();
                            getActivity().getSupportFragmentManager()
                                    .executePendingTransactions();
                            youTubePlayerSupportFragment = null;
                        }
                        if (youTubePlayerSupportFragment == null) {
                            youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
                        }
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .add(HACK_ID_PREFIX + position, youTubePlayerSupportFragment)
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .commit();
                        youTubePlayerSupportFragment.initialize(Config.YOUTUBE_API_KEY,
                                new YouTubePlayer.OnInitializedListener() {
                                    @Override
                                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                        YouTubePlayer youTubePlayer, boolean b) {
                                        DishInfoActivity.ResturantMenuFragment.youTubePlayer = youTubePlayer;
                                        DishInfoActivity.ResturantMenuFragment.youTubePlayer.loadVideo(dishes.getVideo_url());
                                        DishInfoActivity.ResturantMenuFragment.youTubePlayer.setFullscreenControlFlags(0);
                                        DishInfoActivity.ResturantMenuFragment.youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                            @Override
                                            public void onFullscreen(boolean b) {
                                                isFullScreen = b;
                                            }
                                        });
                                    }

                                    @Override
                                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                        YouTubeInitializationResult youTubeInitializationResult) {
                                        Log.e(MainActivityFragment.DemoObjectFragment.class.getSimpleName(), youTubeInitializationResult.name());
                                        if (YouTubeIntents.canResolvePlayVideoIntent(
                                                getActivity())) {
                                            getActivity()
                                                    .startActivity(YouTubeIntents.createPlayVideoIntent(
                                                            getActivity(),
                                                            dishes.getVideo_url()));
                                            return;
                                        }
                                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + dishes.getVideo_url()));
                                        getActivity().startActivity(viewIntent);
                                    }
                                });
                    }
                }
            });
        }


    }


}



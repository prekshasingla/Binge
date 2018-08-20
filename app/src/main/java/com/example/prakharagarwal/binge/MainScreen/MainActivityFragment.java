package com.example.prakharagarwal.binge.MainScreen;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prakharagarwal.binge.BaseApplication;
import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.cart.CartSuccess;
import com.example.prakharagarwal.binge.cart.NewCartActivity;
import com.example.prakharagarwal.binge.model_class.PassingCartItem;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {


    RecyclerView mBrandRecyclerView;
    BrandsAdapter mBrandsAdapter;
    RecyclerView mCategoryRecyclerView;
    CategoriesAdapter mCategoriesAdapter;
    static private List<Menu> mFood;
    static private List<Menu> mFood2;
    List<Brand> brands;
    TextView emptyView;
    ProgressBar progressBar;
    static boolean locationFlag;
    boolean flag = false;
    // LinearLayout nearbyEmptyLayout;
    private ViewPager trendingViewpager;
    private DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    private int mFood2Counter = 0;
    private List<Category1> categories;
    Button inside_order_button;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 98;


    public static MainActivityFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFood = new ArrayList<>();
        mFood2 = new ArrayList<>();
        brands = new ArrayList<>();
        categories = new ArrayList<>();

        PassingCartItem.placed_order_hashmap.clear();
        PassingCartItem.menuIntegerHashMap.clear();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main_fragment, container, false);
        mBrandRecyclerView = (RecyclerView) rootView.findViewById(R.id.brands_recycler);
        mCategoryRecyclerView = rootView.findViewById(R.id.category_recycler);
        emptyView = (TextView) rootView.findViewById(R.id.text_menu_empty);
        progressBar = (ProgressBar) rootView.findViewById(R.id.main_activity_progress);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        inside_order_button=rootView.findViewById(R.id.Order_Inside_button);
        //   nearbyEmptyLayout = rootView.findViewById(R.id.nearby_empty_layout_lin);
        flag = false;
        checkLocationPermission();
        trendingViewpager = rootView.findViewById(R.id.trending_viewpager);

        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(getActivity().getSupportFragmentManager(), mFood);


        Log.v("RishabhSharedPreference","Starting shared prefernce");
        //get data from the shared preference because if user already placed the preorder then we directly switch to the map activity
        MySharedPreference sharedPreference=new MySharedPreference(getActivity());
        if(sharedPreference.savedmapactivity_get_flag()==true)
        {
            Log.v("RishabhSharedPreference","Inside shared prefernce "+sharedPreference.savedmapactivity_get_flag());
            Intent intent = new Intent(getActivity(), CartSuccess.class);
            intent.putExtra("orderId", sharedPreference.savedmapactivity_get_orderID());
            intent.putExtra("latitude", Double.parseDouble(new Float(sharedPreference.savedmapactivity_get_latitude()).toString()));
            intent.putExtra("longitude", Double.parseDouble(new Float(sharedPreference.savedmapactivity_get_longitude()).toString()));
            intent.putExtra("resturant_id",sharedPreference.savedmapactivity_restaurantID());
            startActivity(intent);
        }
        Log.v("RishabhSharedPreference","OutSide shared prefernce "+sharedPreference.savedmapactivity_get_flag());

        if(sharedPreference.get_insideorderpayment() && ((BaseApplication) getActivity().getApplication()).isCartFlag())
        {
            Intent intent = new Intent(getActivity(), DishInfoActivity.class);
            intent.putExtra("rest", sharedPreference.get_inside_order_restaurant_id());
            startActivity(intent);
        }


        inside_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PostOrderQRActivity.class));
            }
        });



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("menu");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (((MainActivity) getActivity()).getLatitude() != null && ((MainActivity) getActivity()).getLongitude() != null)
                    locationFlag = true;
                else
                    locationFlag = false;

                new TrendingAsync().execute(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference ref1 = database.getReference("brands");

        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                new BrandsAsync().execute(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mBrandsAdapter = new BrandsAdapter(brands, getActivity());
        //  final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBrandRecyclerView.setLayoutManager(gridLayoutManager);
        mBrandRecyclerView.setAdapter(mBrandsAdapter);

        return rootView;
    }

    public void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity().getApplicationContext())
                        .setTitle("Location Permission Required")
                        .setMessage("Please give permission to access your location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {

        }
    }


    private class TrendingAsync extends AsyncTask<DataSnapshot, Void, Void> {

        HashMap<String, Integer> totalcategory = new HashMap<>();

        protected Void doInBackground(DataSnapshot... dataSnapshot) {
//            mFood.clear();
//            mFood2.clear();
            for (DataSnapshot child : dataSnapshot[0].getChildren()) { //38_barakks
                Double latitude = 0.0;
                Double longitude = 0.0;
                String restuarant_name = null;
                String restuarant_id = null;

                for (DataSnapshot child1 : child.getChildren()) { //starter, lat, long
                    if (child1.getKey().equals("latitude"))
                        latitude = (Double) child1.getValue();
                    if (child1.getKey().equals("longitude"))
                        longitude = (Double) child1.getValue();
                    if (child1.getKey().equals("restaurant_name"))
                        restuarant_name = (String) child1.getValue();
                    if (child1.getKey().equals("restaurant_id"))
                        restuarant_id = (String) child1.getValue();
                }
                if (locationFlag && calRadius(latitude, longitude)) {
                    flag = true;
                    for (DataSnapshot child1 : child.getChildren()) {
                        if (!child1.getKey().equals("latitude") && !child1.getKey().equals("longitude")) {
                            for (DataSnapshot child2 : child1.getChildren()) {
                                Menu menu = child2.getValue(Menu.class);
                                menu.setRestaurantName(restuarant_name);
                                menu.setRestaurant_id(restuarant_id);
                                menu.setLatitude(String.valueOf(latitude));
                                menu.setLongitude(String.valueOf(longitude));
                                if (menu.getHas_video() == 0) {
                                    mFood.add(menu);

                                    if (totalcategory.containsKey(menu.getCategory())) {
                                        int total = totalcategory.get(menu.getCategory());
                                        totalcategory.put(menu.getCategory(), total + 1);
                                        Log.v("RISHABH", "ToTal Category " + menu.getCategory() + " " + total);
                                    } else {
                                        totalcategory.put(menu.getCategory(), 1);
                                        Log.v("RISHABH", "ToTal Category " + menu.getCategory() + "1");
                                    }
                                }
                            }
                        }

                    }
                    //pass the data for the category
                    PassingData.setMenuList(mFood);
                    PassingData.setTotalcategoryitem(totalcategory);
                } else if (!flag && mFood2Counter < 50) {
                    for (DataSnapshot child1 : child.getChildren()) {
                        if (!child1.getKey().equals("latitude") && !child1.getKey().equals("longitude")) {
                            for (DataSnapshot child2 : child1.getChildren()) {
                                Menu menu = child2.getValue(Menu.class);
                                menu.setRestaurantName(restuarant_name);
                                menu.setRestaurant_id(restuarant_id);
                                menu.setLatitude(String.valueOf(latitude));
                                menu.setLongitude(String.valueOf(longitude));
                                if (menu.getHas_video() == 0) {
                                    mFood2.add(menu);
                                    mFood2Counter++;
                                    if (totalcategory.containsKey(menu.getCategory())) {
                                        int total = totalcategory.get(menu.getCategory());
                                        totalcategory.put(menu.getCategory(), total+1);
                                        Log.v("RISHABH","ToTal Category "+menu.getCategory()+" "+total);
                                    } else {
                                        totalcategory.put(menu.getCategory(), 1);
                                        Log.v("RISHABH","ToTal Category "+menu.getCategory()+ "1 no");
                                    }
                                }

                            }
                        }

                    }
                    PassingData.setMenuList(mFood2);
                    PassingData.setTotalcategoryitem(totalcategory);
                }

            }
            return null;
        }


        protected void onPostExecute(Void d) {

            UpdateTrendingView();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref2 = database.getReference("categories");

            ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    new CategoriesAsync().execute(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void UpdateTrendingView() {

//        Collections.shuffle(mFood);
//        Collections.shuffle(mFood2);
        progressBar.setVisibility(View.GONE);
        if (mFood.size() == 0 && mFood2.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else if (mFood.size() == 0) {
            emptyView.setVisibility(View.GONE);
            //   nearbyEmptyLayout.setVisibility(View.VISIBLE);
            mDemoCollectionPagerAdapter =
                    new DemoCollectionPagerAdapter(getActivity().getSupportFragmentManager(), mFood2);
            trendingViewpager.setAdapter(mDemoCollectionPagerAdapter);
        } else {
            emptyView.setVisibility(View.GONE);
            //   nearbyEmptyLayout.setVisibility(View.GONE);


            mDemoCollectionPagerAdapter.addAll(mFood);
            mDemoCollectionPagerAdapter.notifyDataSetChanged();
             trendingViewpager.removeAllViews();
            trendingViewpager.setAdapter(mDemoCollectionPagerAdapter);

        }

    }

    private class BrandsAsync extends AsyncTask<DataSnapshot, Void, Void> {

        protected Void doInBackground(DataSnapshot... dataSnapshot) {

            for (DataSnapshot child : dataSnapshot[0].getChildren()) { //38_barakks

                Brand brand = child.getValue(Brand.class);
                brands.add(brand);
            }
            return null;
        }


        protected void onPostExecute(Void d) {
            UpdateBrandsView();
        }
    }

    private void UpdateBrandsView() {
        mBrandsAdapter.addAll(brands);
        mBrandsAdapter.notifyDataSetChanged();
    }

    private class CategoriesAsync extends AsyncTask<DataSnapshot, Void, Void> {

        protected Void doInBackground(DataSnapshot... dataSnapshot) {

            for (DataSnapshot child : dataSnapshot[0].getChildren()) { //38_barakks

                Category1 category1 = child.getValue(Category1.class);
                categories.add(category1);
            }
            return null;
        }


        protected void onPostExecute(Void d) {
            UpdateCategoriesView();
        }
    }

    private void UpdateCategoriesView() {
        List<String> category_string=new ArrayList<>();
        List<Integer> category_integer=new ArrayList<>();
        HashMap<String,Integer> categoryhashmap=PassingData.getTotalcategoryitem();
        for(Map.Entry<String,Integer> category:categoryhashmap.entrySet())
        {
            category_string.add(category.getKey());
            category_integer.add(category.getValue());

            Log.v("RISHABHRAWAT","category is the "+category.getKey()+" "+category.getValue());
        }

        mCategoriesAdapter = new CategoriesAdapter(categories, getActivity(),category_string,category_integer);
        final LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mCategoryRecyclerView.setLayoutManager(mLayoutManager1);
        mCategoryRecyclerView.setAdapter(mCategoriesAdapter);
    }


    private boolean calRadius(double lat2, double lon2) {
        double lat1 = Double.parseDouble(((MainActivity) getActivity()).getLatitude());
        double lon1 = Double.parseDouble(((MainActivity) getActivity()).getLongitude());
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (dist <= 6)
            return true;
        else
            return false;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public class DemoCollectionPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
        FragmentManager fragmentManager;
        private List<Menu> mFood;


        public DemoCollectionPagerAdapter(FragmentManager fm, List<Menu> mFood) {
            super(fm);
            this.fragmentManager = fm;
            this.mFood = mFood;
        }

        public void addAll(List<Menu> mFood){
            this.mFood=mFood;
        }
        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
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
            Display display = getActivity().getWindowManager().getDefaultDisplay();
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

    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";
        private static final String TAG = DemoObjectFragment.class.getSimpleName();
        private static final int HACK_ID_PREFIX = 12331293; //some random number
        private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
        private static YouTubePlayerSupportFragment youTubePlayerFragment;
        private static YouTubePlayer youTubePlayer;
        private static boolean isFullScreen = false;
        private Menu dish;

        private ImageRequest imageRequest;
        private Uri uri;
        public SimpleDraweeView image;
        public TextView title, time;
        public TextView restaurantTrend;
        public FrameLayout videoContainer;
        public int position;
        private Button preOrder;
        private Button postOrder;

        private String timing;


        @Override
        public View onCreateView(final LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(R.layout.fragment_trending, container, false);
            Bundle args = getArguments();
            position = (int) args.get(ARG_OBJECT);
            dish = (Menu) args.getSerializable("dish");
            image = (SimpleDraweeView) rootView.findViewById(R.id.image);
            title = (TextView) rootView.findViewById(R.id.title);
            time = rootView.findViewById(R.id.restaurant_time);
            videoContainer = (FrameLayout) rootView.findViewById(R.id.video_container);
            restaurantTrend = rootView.findViewById(R.id.restaurant_trent);
            preOrder = rootView.findViewById(R.id.pre_order);
            postOrder = rootView.findViewById(R.id.post_order);
            title.setText(dish.getName());
            restaurantTrend.setText(dish.getRestaurantName());
            if(MainActivityFragment.locationFlag) {
                double timebetweentwolatlong = caldistance(Double.parseDouble(dish.getLatitude()), Double.parseDouble(dish.getLongitude()));
                time.setText(String.valueOf(timebetweentwolatlong).substring(0, 4) + " km");
                timing = String.valueOf(timebetweentwolatlong).substring(0, 4) + " km";
            }
            else
            {
                time.setText("");
                timing="";
            }
            preOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DishInfoActivity.class);
                    intent.putExtra("rest", dish.getRestaurant_id());
                    intent.putExtra("dish", dish.getName());
                    intent.putExtra("flag", "preOrder");
                    intent.putExtra("time", timing);
                    // PassingData.setLatitude(dish.getLatitude());
                    // PassingData.setLongitude(dish.getLongitude());
                    startActivity(intent);
                }
            });

            postOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PostOrderQRActivity.class);
                    startActivity(intent);
                }
            });


            prepare();
            return rootView;
        }

        public void prepare() {
            if (!TextUtils.isEmpty(dish.getPoster_url()) && uri == null) {
                try {
                    uri = Uri.parse(dish.getPoster_url());
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
            bind();
        }


        private double caldistance(double lat2, double lon2) {
            double lat1 = Double.parseDouble(((MainActivity) getActivity()).getLatitude());
            double lon1 = Double.parseDouble(((MainActivity) getActivity()).getLongitude());
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1))
                    * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            return dist;
        }

        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }

        public void bind() {
            image.setAspectRatio(16f / 9f);
            if (imageRequest == null) {
                image.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageRequestBuilder builder;
                        if (uri == null) {
                            builder = ImageRequestBuilder.newBuilderWithResourceId(android.R.color.darker_gray);
                        } else {
                            builder = ImageRequestBuilder.newBuilderWithSource(uri);
                        }
                        try {
                            imageRequest = builder.setResizeOptions(new ResizeOptions(
                                    image.getWidth(), image.getHeight()
                            )).build();
                        } catch (Exception e) {
                            Log.e("Rishabh", "Error is coming");
                        }
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(imageRequest)
                                .setOldController(image.getController())
                                .build();
                        image.setController(controller);
                    }
                });
            } else {
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(imageRequest)
                        .setOldController(image.getController())
                        .build();
                image.setController(controller);
            }
            bindVideo();
//            bindDescription(videoViewHolder);
        }

        private void bindVideo() {
            View view = videoContainer;
            if (view != null) {
                view.setId(HACK_ID_PREFIX + position);
            }
            handleClick();
        }

        private void handleClick() {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(dish.getVideo_url())) {
                        return;
                    }
                    if (!YouTubeIntents.isYouTubeInstalled(view.getContext()) ||
                            YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(view.getContext()) != YouTubeInitializationResult.SUCCESS) {
                        if (YouTubeIntents.canResolvePlayVideoIntent(view.getContext())) {
                            getActivity().
                                    startActivity(YouTubeIntents.createPlayVideoIntent(view.getContext(), dish.getVideo_url()));
                            return;
                        }
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + dish.getVideo_url()));
                        getActivity().startActivity(viewIntent);
                        return;
                    }
                    if (videoContainer.getChildCount() == 0) {
                        if (youTubePlayerFragment == null) {
                            youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                        }
                        if (youTubePlayerFragment.isAdded()) {
                            if (DemoObjectFragment.youTubePlayer != null) {
                                try {
                                    DemoObjectFragment.youTubePlayer.pause();
                                    DemoObjectFragment.youTubePlayer.release();
                                } catch (Exception e) {
                                    if (DemoObjectFragment.youTubePlayer != null) {
                                        try {
                                            DemoObjectFragment.youTubePlayer.release();
                                        } catch (Exception ignore) {
                                        }

                                    }
                                }
                                DemoObjectFragment.youTubePlayer = null;
                            }

                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .remove(youTubePlayerFragment)
                                    .commit();
                            getActivity().getSupportFragmentManager()
                                    .executePendingTransactions();
                            youTubePlayerFragment = null;
                        }
                        if (youTubePlayerFragment == null) {
                            youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                        }
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .add(HACK_ID_PREFIX + position, youTubePlayerFragment)
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .commit();
                        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY,
                                new YouTubePlayer.OnInitializedListener() {
                                    @Override
                                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                        YouTubePlayer youTubePlayer, boolean b) {
                                        DemoObjectFragment.youTubePlayer = youTubePlayer;
                                        DemoObjectFragment.youTubePlayer.loadVideo(dish.getVideo_url());
                                        DemoObjectFragment.youTubePlayer.setFullscreenControlFlags(0);
                                        DemoObjectFragment.youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                            @Override
                                            public void onFullscreen(boolean b) {
                                                isFullScreen = b;
                                            }
                                        });
                                    }

                                    @Override
                                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                        YouTubeInitializationResult youTubeInitializationResult) {
                                        Log.e(DemoObjectFragment.class.getSimpleName(), youTubeInitializationResult.name());
                                        if (YouTubeIntents.canResolvePlayVideoIntent(
                                                getActivity())) {
                                            getActivity()
                                                    .startActivity(YouTubeIntents.createPlayVideoIntent(
                                                            getActivity(),
                                                            dish.getVideo_url()));
                                            return;
                                        }
                                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + dish.getVideo_url()));
                                        getActivity().startActivity(viewIntent);
                                    }
                                });
                    }
                }
            });
        }
    }

}


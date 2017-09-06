package com.example.prakharagarwal.binge.StoriesMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class StoriesActivity extends FragmentActivity implements MenuAdapter.Callback{


    RecyclerView mRecyclerView;
    MenuAdapter menuAdapter;
    SlidingUpPanelLayout slidingUpPanelLayout;
    ArrayList<String> Videos;
    String ID;
    List<Menu> menus;

    RelativeLayout relativeLayoutData;

    static TextView emptyView;
    static TextView emptyViewMenu;



    WebView webView;
    ArrayList<MenuVideoPojo> mVideos;
    int end = -1, curr = -1;
    TextView resName;
    TextView desc;
    TextView dishName;
    ImageView veg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        mVideos = new ArrayList<>();
        menus = new ArrayList<Menu>();

        emptyView = (TextView)findViewById(R.id.menu_story_empty);
        emptyViewMenu = (TextView)findViewById(R.id.stories_text_menu_empty);

        relativeLayoutData=(RelativeLayout)findViewById(R.id.relative_layout_data);

        resName=(TextView) findViewById(R.id.restaurant_name_stories);
        desc=(TextView)findViewById(R.id.frag_stories_desc);
        dishName=(TextView)findViewById(R.id.frag_stories_dish);
        veg=(ImageView)findViewById(R.id.frag_stories_veg_image);
//        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/segoeui.ttf");
//        resName.setTypeface(typeface);

        ID = getIntent().getStringExtra("restaurantID");
        String res_Name=getIntent().getStringExtra("restaurantName");
        resName.setText(res_Name);
        resName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("menu");
        //ref.keepSynced(true);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("error", "error");
            }
        });

//        mVideos.add("B34rGH1GX4w");//Portrait Video
//        mVideos.add("xmYg3GqWlaQ");//potrait
//        mVideos.add("c2EY0KnAGZc");//Landscape Video
//        mVideos.add("xsFQN64WmF4");
//        mVideos.add("yabDCV4ccQs");
//        mVideos.add("FoMlSB6ftQg");
//        mVideos.add("5723ieP5VAQ");


        //mVideos=new ArrayList<String>();
        //  mVideos.addAll(((StoriesActivity) getActivity()).getVideos());
        curr = 0;
        end = mVideos.size();

        LinearLayout linearLayoutCenter=(LinearLayout) findViewById(R.id.center);
        linearLayoutCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });

        webView = (WebView) findViewById(R.id.webview_stories);

        hideSystemUi();

        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient());

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);


        webView.setScrollContainer(false);

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        } else {
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        }


        LinearLayout linearLayoutleft=(LinearLayout)findViewById(R.id.click_left);
        linearLayoutleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curr != 0 && mVideos.size()>0) {
                    curr--;
                    webView.loadDataWithBaseURL("", getYoutubeURL(mVideos.get(curr).getUrl()), "text/html", "UTF-8", "");
                    dishName.setText(mVideos.get(curr).getName());
                    desc.setText(mVideos.get(curr).getDesc());
                    if(mVideos.get(curr).getVeg()!=null)
                    {if(mVideos.get(curr).getVeg()==0 ){
                        veg.setImageResource(R.mipmap.veg);
                    }else{
                        veg.setImageResource(R.mipmap.nonveg);
                    }}
                }
            }
        });

        LinearLayout linearLayoutright=(LinearLayout)findViewById(R.id.click_right);
        linearLayoutright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curr !=end-1 && mVideos.size()>0) {

                    curr++;
                    webView.loadDataWithBaseURL("", getYoutubeURL(mVideos.get(curr).getUrl()), "text/html", "UTF-8", "");
                    dishName.setText(mVideos.get(curr).getName());
                    desc.setText(mVideos.get(curr).getDesc());
                    if(mVideos.get(curr).getVeg()!=null)
                    {if(mVideos.get(curr).getVeg()==0 ){
                        veg.setImageResource(R.mipmap.veg);
                    }else{
                        veg.setImageResource(R.mipmap.nonveg);
                    }}
                }
            }
        });


//---------------------------------------SWIPE-----------------------------------------
//        final GestureDetector gesture = new GestureDetector(getActivity(),
//                new GestureDetector.SimpleOnGestureListener() {
//
//                    @Override
//                    public boolean onDown(MotionEvent e) {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                        boolean result = false;
//                        final int SWIPE_THRESHOLD = 100;
//                        final int SWIPE_VELOCITY_THRESHOLD = 100;
//                        try {
//                            float diffY = e2.getY() - e1.getY();
//                            float diffX = e2.getX() - e1.getX();
//                            if (Math.abs(diffX) > Math.abs(diffY)) {
//                                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                                    if (diffX > 0) {
//                                        if (curr != 0) {
//                                            curr--;
//                                            webView.loadDataWithBaseURL("", getYoutubeURL(mVideos.get(curr).getUrl()), "text/html", "UTF-8", "");
//                                        }
//                                        //Toast.makeText(getActivity(), "right", Toast.LENGTH_LONG).show();
//                                    } else {
//
//                                        if (curr != end - 1) {
//                                            curr++;
//                                            webView.loadDataWithBaseURL("", getYoutubeURL(mVideos.get(curr).getUrl()), "text/html", "UTF-8", "");
//                                        }
//                                        //Toast.makeText(getActivity(), "left", Toast.LENGTH_LONG).show();
//                                    }
//                                    result = true;
//                                }
//                            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                                if (diffY > 0) {
//                                    ((StoriesActivity) getActivity()).getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//                                    //Toast.makeText(getActivity(), "down", Toast.LENGTH_LONG).show();
//                                } else {
//                                    ((StoriesActivity) getActivity()).getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                                    //Toast.makeText(getActivity(), "up", Toast.LENGTH_LONG).show();
//                                }
//                                result = true;
//                            }
//                        } catch (Exception exception) {
//                            exception.printStackTrace();
//                        }
//                        return result;
//                    }
//
//                });



//        rootView.findViewById(R.id.relative_layout_stories).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return gesture.onTouchEvent(event);
//            }
//        });
//----------------------------------------END------------------------------------------

        menus = new ArrayList<Menu>();
        Videos = new ArrayList<String>();

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.stories_sliding_up);

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                    ((ImageView) findViewById(R.id.stories_heading_image)).setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);


                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                    ((ImageView) findViewById(R.id.stories_heading_image)).setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.menu_fragment_recycler_view);
        menuAdapter = new MenuAdapter(this, menus);
        try {
            mRecyclerView.setAdapter(menuAdapter);
        } catch (NoClassDefFoundError e) {

        }
        mRecyclerView.setLayoutManager(linearLayoutManager);
        checkIfEmpty();


    }
    public void getData(DataSnapshot dataSnapshot) {

        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
            if (child1.getKey().equals(ID))

                for (DataSnapshot child2 : child1.getChildren()) {

                    if (child2.getKey() != null) {
                        for (DataSnapshot child3 : child2.getChildren()) {
                            Menu menu = child3.getValue(Menu.class);
                            getVideoStoryURL(menu);
                            menus.add(menu);

                        }

                    }

                }


        }
        playStories();
        addAllMenus(menus);
    }
    private void getVideoStoryURL(Menu menu) {
        if (menu.getHas_video() == 0) {
            MenuVideoPojo pojo=new MenuVideoPojo();
            pojo.setUrl(menu.getVideo_url());
            pojo.setName(menu.getName());
            pojo.setDesc(menu.getDesc());
            pojo.setVeg(menu.getVeg());
            mVideos.add(pojo);
        }
        end = mVideos.size();
    }

    public void playStories() {
        if (mVideos.size() > 0) {
            emptyView.setVisibility(View.INVISIBLE);
            relativeLayoutData.setVisibility(View.VISIBLE);
            webView.loadDataWithBaseURL("", getYoutubeURL(mVideos.get(0).getUrl()), "text/html", "UTF-8", "");
            dishName.setText(mVideos.get(0).getName());
            desc.setText(mVideos.get(0).getDesc());
            if (mVideos.get(0).getVeg() != null) {
                if (mVideos.get(0).getVeg() == 0) {
                    veg.setImageResource(R.mipmap.veg);
                } else {
                    veg.setImageResource(R.mipmap.nonveg);
                }
            }
        }


    }

    void checkIfEmpty() {
        if (emptyViewMenu != null && menuAdapter != null) {
            final boolean emptyViewVisible =
                    menuAdapter.getItemCount() == 0;
            emptyViewMenu.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            mRecyclerView.setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    String getYoutubeURL(String videoID) {

        String url = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <body style=\"margin:0; padding:0\">\n" +
                "    <div id=\"player\"></div>\n" +
                "\n" +
                "    <script>\n" +
                "      var tag = document.createElement('script');\n" +
                "\n" +
                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "\n" +
                "      var player;\n" +
                "      function onYouTubeIframeAPIReady() {\n" +
                "        player = new YT.Player('player', {\n" +
                "          height: '640',\n" +
                "          width: '360',\n" +
                "          videoId: '" + videoID + "',\n" +
                " playerVars: { \n" +
                "         'autoplay': 1,\n" +
                "          'autohide': 1,\n" +
                "         'controls': 0, \n" +
                "         'showinfo': 0,\n" +
                "          'playlist': '" + videoID + "',\n" +
                "         'loop': 1,\n" +
                "         'rel' : 0\n" +
                "  }," +
                "          events: {\n" +
                "            'onReady': onPlayerReady,\n" +
                "            'onStateChange': onPlayerStateChange\n" +
                "          }\n" +


                "        });\n" +
                "      }\n" +
                "\n" +
                "      function onPlayerReady(event) {\n" +
                "        event.target.playVideo();\n" +
                "      }\n" +
                "\n" +
                "      var done = false;\n" +
                "      function onPlayerStateChange(event) {\n" +
                "      }\n" +
                "      function stopVideo() {\n" +
                "        player.stopVideo();\n" +
                "      }\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";

        return url;
    }

    public void addAllMenus(List<Menu> menus) {
        menuAdapter.addAll(menus);
        menuAdapter.notifyDataSetChanged();
        checkIfEmpty();


    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    public void changeSlidingUpPanelLayoutState() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

    }

    @Override
    public void showStory(Context context, Menu menu) {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        webView.loadDataWithBaseURL("", getYoutubeURL(menu.getVideo_url()), "text/html", "UTF-8", "");
        dishName.setText(menu.getName());
        desc.setText(menu.getDesc());
        if(menu.getVeg()!=null)
        {if(menu.getVeg()==0 ){
            veg.setImageResource(R.mipmap.veg);
        }else{
            veg.setImageResource(R.mipmap.nonveg);
        }}
        curr=0;
    }
}
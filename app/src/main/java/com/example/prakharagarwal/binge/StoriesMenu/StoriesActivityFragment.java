package com.example.prakharagarwal.binge.StoriesMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class StoriesActivityFragment extends Fragment {

String ID;
    List<Menu> menus;

    WebView webView;
    ArrayList<String> mVideos;
    int end = -1, curr = -1;


    public StoriesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_stories, container, false);
        mVideos=new ArrayList<String>();
        menus=new ArrayList<Menu>();

        ID=getActivity().getIntent().getStringExtra("restaurantID");
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

                Log.e("error","error");
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

//        RelativeLayout relativeLayoutStories=(RelativeLayout) rootView.findViewById(R.id.relative_layout_stories);
//        relativeLayoutStories.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
//            }
//        });

        webView = (WebView) rootView.findViewById(R.id.webview_stories);

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

//        if(mVideos.size()>0)
//         webView.loadDataWithBaseURL("", getYoutubeURL(mVideos.get(0)), "text/html", "UTF-8", "");


        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        boolean result = false;
                        final int SWIPE_THRESHOLD = 100;
                        final int SWIPE_VELOCITY_THRESHOLD = 100;
                        try {
                            float diffY = e2.getY() - e1.getY();
                            float diffX = e2.getX() - e1.getX();
                            if (Math.abs(diffX) > Math.abs(diffY)) {
                                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                    if (diffX > 0) {
                                        if (curr != 0) {
                                            curr--;
                                            webView.loadDataWithBaseURL("", getYoutubeURL(mVideos.get(curr)), "text/html", "UTF-8", "");
                                        }
                                        //Toast.makeText(getActivity(), "right", Toast.LENGTH_LONG).show();
                                    } else {

                                        if (curr != end - 1) {
                                            curr++;
                                            webView.loadDataWithBaseURL("", getYoutubeURL(mVideos.get(curr)), "text/html", "UTF-8", "");
                                        }
                                        //Toast.makeText(getActivity(), "left", Toast.LENGTH_LONG).show();
                                    }
                                    result = true;
                                }
                            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffY > 0) {
                                    ((StoriesActivity) getActivity()).getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                    //Toast.makeText(getActivity(), "down", Toast.LENGTH_LONG).show();
                                } else {
                                    ((StoriesActivity) getActivity()).getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                                    //Toast.makeText(getActivity(), "up", Toast.LENGTH_LONG).show();
                                }
                                result = true;
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        return result;
                    }

                });

        rootView.findViewById(R.id.relative_layout_stories).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });


        return rootView;
    }
public void addAllVideos(){

}
    public void getData(DataSnapshot dataSnapshot) {

        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
            if(child1.getKey().equals(ID))

                for (DataSnapshot child2 : child1.getChildren()) {

                    if (child2.getKey()!=null){
                        for (DataSnapshot child3 : child2.getChildren()) {
                            Menu menu=child3.getValue(Menu.class);
                            getVideoStoryURL(menu);
                            menus.add(menu);

                        }

                    }

                }


        }
playStories();
        ((StoriesActivity)getActivity()).addAllMenus(menus);
    }
    private void getVideoStoryURL(Menu menu) {
        if(menu.getHas_video()==0) {
            mVideos.add(menu.getVideo_url());
        }
    }
    public void playStories(){
        if(mVideos.size()>0)
            webView.loadDataWithBaseURL("", getYoutubeURL(mVideos.get(0)), "text/html", "UTF-8", "");
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

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    @Override
    public void onPause() {
        super.onPause();
        webView.destroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        webView.destroy();
    }
}

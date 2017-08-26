package com.example.prakharagarwal.fingerlickingawesome.StoriesMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.prakharagarwal.fingerlickingawesome.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class StoriesActivityFragment extends Fragment{


    WebView webView;
    ArrayList<String> Videos;
    int end=-1, curr=-1;


    public StoriesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= (View)inflater.inflate(R.layout.fragment_stories, container, false);

        Videos=new ArrayList<String>();
        Videos.add("B34rGH1GX4w");//Portrait Video
        Videos.add("xmYg3GqWlaQ");//potrait
        Videos.add("c2EY0KnAGZc");//Landscape Video
        Videos.add("xsFQN64WmF4");
        Videos.add("yabDCV4ccQs");
        Videos.add("FoMlSB6ftQg");
        Videos.add("5723ieP5VAQ");



        curr=0;
        end=Videos.size();

//        RelativeLayout relativeLayoutStories=(RelativeLayout) rootView.findViewById(R.id.relative_layout_stories);
//        relativeLayoutStories.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
//            }
//        });

        webView= (WebView)rootView.findViewById(R.id.webview_stories);

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
        }
        else {
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        }

        webView.loadDataWithBaseURL("",getYoutubeURL(Videos.get(0)), "text/html", "UTF-8", "");


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
                                        if(curr!=0) {
                                           curr--;
                                           webView.loadDataWithBaseURL("", getYoutubeURL(Videos.get(curr)), "text/html", "UTF-8", "");
                                        }
                                        //Toast.makeText(getActivity(), "right", Toast.LENGTH_LONG).show();
                                    } else {

                                        if(curr!=end-1) {
                                           curr++;
                                           webView.loadDataWithBaseURL("", getYoutubeURL(Videos.get(curr)), "text/html", "UTF-8", "");
                                        }
                                        //Toast.makeText(getActivity(), "left", Toast.LENGTH_LONG).show();
                                    }
                                    result = true;
                                }
                            }
                            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffY > 0) {
                                    ((StoriesActivity)getActivity()).getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                    //Toast.makeText(getActivity(), "down", Toast.LENGTH_LONG).show();
                                } else {
                                    ((StoriesActivity)getActivity()).getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
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


    String getYoutubeURL(String videoID){

         String url ="<!DOCTYPE html>\n" +
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
                "          videoId: '"+videoID+"',\n" +
                " playerVars: { \n" +
                "         'autoplay': 1,\n" +
                "          'autohide': 1,\n"+
                "         'controls': 0, \n" +
                "         'showinfo': 0,\n"+
                "          'playlist': '"+videoID+"',\n" +
                "         'loop': 1,\n"+
                "         'rel' : 0\n" +
                "  },"+
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

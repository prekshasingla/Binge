package com.example.prakharagarwal.fingerlickingawesome;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.media.MediaCodec;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.Util;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class StoriesActivityFragment extends Fragment {


    private static final String TAG = "VideoPlayer";



    WebView webView;
    ArrayList<String> Videos;
    int end=-1, curr=-1;

    String url;


    public StoriesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= (View)inflater.inflate(R.layout.fragment_stories, container, false);



        Videos=new ArrayList<String>();
        Videos.add("B34rGH1GX4w");//Portrait Video
        Videos.add("V_BEOsCvKqI");
        Videos.add("c2EY0KnAGZc");//Landscape Video
        Videos.add("xsFQN64WmF4");
        Videos.add("yabDCV4ccQs");
        Videos.add("FoMlSB6ftQg");
        Videos.add("5723ieP5VAQ");



        curr=0;
        end=Videos.size();

        RelativeLayout relativeLayoutStories=(RelativeLayout) rootView.findViewById(R.id.relative_layout_stories);
        relativeLayoutStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });



        webView= (WebView)rootView.findViewById(R.id.webview_stories);

        hideSystemUi();


        // final  String url="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+Videos.get(0)+"?rel=0&ecver=1&modestbranding=1&showinfo=0&autohide=1&controls=0&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";


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


        LinearLayout linearLayoutLeft=(LinearLayout)rootView.findViewById(R.id.click_left);
        linearLayoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curr!=0) {
                    curr--;
                    webView.loadDataWithBaseURL("", getYoutubeURL(Videos.get(curr)), "text/html", "UTF-8", "");

                }


            }
        });

        LinearLayout linearLayoutRight=(LinearLayout)rootView.findViewById(R.id.click_right);
        linearLayoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(curr!=end-1) {
                    curr++;
                    webView.loadDataWithBaseURL("", getYoutubeURL(Videos.get(curr)), "text/html", "UTF-8", "");
                }

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

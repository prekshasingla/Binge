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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= (View)inflater.inflate(R.layout.fragment_stories, container, false);



        Videos=new ArrayList<String>();
        //Videos.add("3Gwnyt5-Iq8"); //Landscape Video
        Videos.add("V_BEOsCvKqI");
        Videos.add("xsFQN64WmF4");
        Videos.add("yabDCV4ccQs");
        Videos.add("FoMlSB6ftQg");
        Videos.add("5723ieP5VAQ");

        end=Videos.size();

        RelativeLayout relativeLayoutStories=(RelativeLayout) rootView.findViewById(R.id.relative_layout_stories);
        relativeLayoutStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });



//        final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//                youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
//                    @Override
//                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//
//                        if (!b) {
//                            player=youTubePlayer;
//                            player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
//                       /**/
//                            player.cueVideo(Videos.get(0));
//                            curr=0;
//
//                            player.play();
//                        }
//
//                    }
//
//                    @Override
//                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
//                        if (errorReason.isUserRecoverableError()) {
//                            //errorReason.getErrorDialog(mContext, RECOVERY_REQUEST).show();
//                        } else {
//                            String error = String.format(getActivity().getString(R.string.player_error), errorReason.toString());
//                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                });


        webView= (WebView)rootView.findViewById(R.id.webview_stories);

        hideSystemUi();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        float density = displayMetrics.density;


        // final  String url="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+Videos.get(0)+"?rel=0&ecver=1&modestbranding=1&showinfo=0&autohide=1&controls=0&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";

        url ="<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <body>\n" +
                "    <!-- 1. The <iframe> (and video player) will replace this <div> tag. -->\n" +
                "    <div id=\"player\" onClick=\" {} \"></div>\n" +
                "\n" +
                "    <script>\n" +
                "      // 2. This code loads the IFrame Player API code asynchronously.\n" +
                "      var tag = document.createElement('script');\n" +
                "\n" +
                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "\n" +
                "      // 3. This function creates an <iframe> (and YouTube player)\n" +
                "      //    after the API code downloads.\n" +
                "      var player;\n" +
                "      function onYouTubeIframeAPIReady() {\n" +
                "        player = new YT.Player('player', {\n" +
                "          height: '626',\n" +
                "          width: '345',\n" +
                "          videoId: 'xsFQN64WmF4',\n" +
                " playerVars: { \n" +
                "         'autoplay': 1,\n" +
                "         'controls': 0, \n" +
                "         'showinfo': 0,\n"+
                "         'rel' : 0\n" +
                "  },"+
                "          events: {\n" +
                "            'onReady': onPlayerReady,\n" +
                "            'onStateChange': onPlayerStateChange\n" +
                "          }\n" +


                "        });\n" +
                "      }\n" +
                "\n" +
                "      // 4. The API will call this function when the video player is ready.\n" +
                "      function onPlayerReady(event) {\n" +
                "        event.target.playVideo();\n" +
                "      }\n" +
                "\n" +
                "      // 5. The API calls this function when the player's state changes.\n" +
                "      //    The function indicates that when playing a video (state=1),\n" +
                "      //    the player should play for six seconds and then stop.\n" +
                "      var done = false;\n" +
                "      function onPlayerStateChange(event) {\n" +
                "      }\n" +
                "      function stopVideo() {\n" +
                "        player.stopVideo();\n" +
                "      }\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";

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

        webView.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");


        LinearLayout linearLayoutLeft=(LinearLayout)rootView.findViewById(R.id.click_left);
//        linearLayoutLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(curr!=0) {
//                    curr--;
//                    final  String url="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+Videos.get(curr)+"?rel=0?ecver=1&modestbranding=1&showinfo=0&autohide=1&controls=0&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";
//
//                    webView.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");
//
//                    //player.cueVideo(Videos.get(curr));
//                    //player.play();
//                }
//
//
//            }
//        });

        LinearLayout linearLayoutRight=(LinearLayout)rootView.findViewById(R.id.click_right);
        linearLayoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(curr!=end) {
                    curr++;
                    //final  String url="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+Videos.get(curr)+"?rel=0?ecver=1&modestbranding=1&showinfo=0&autohide=1&controls=0&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";

                    url ="<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "  <body>\n" +
                            "    <!-- 1. The <iframe> (and video player) will replace this <div> tag. -->\n" +
                            "    <div id=\"player\" onClick=\" {} \"></div>\n" +
                            "\n" +
                            "    <script>\n" +
                            "      // 2. This code loads the IFrame Player API code asynchronously.\n" +
                            "      var tag = document.createElement('script');\n" +
                            "\n" +
                            "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                            "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                            "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                            "\n" +
                            "      // 3. This function creates an <iframe> (and YouTube player)\n" +
                            "      //    after the API code downloads.\n" +
                            "      var player;\n" +
                            "      function onYouTubeIframeAPIReady() {\n" +
                            "        player = new YT.Player('player', {\n" +
                            "          height: '626',\n" +
                            "          width: '345',\n" +
                            "          videoId: 'V_BEOsCvKqI',\n" +
                            " playerVars: { \n" +
                            "         'autoplay': 1,\n" +
                            "         'controls': 0, \n" +
                            "         'showinfo': 0,\n"+
                            "         'rel' : 0\n" +
                            "  },"+
                            "          events: {\n" +
                            "            'onReady': onPlayerReady,\n" +
                            "            'onStateChange': onPlayerStateChange\n" +
                            "          }\n" +


                            "        });\n" +
                            "      }\n" +
                            "\n" +
                            "      // 4. The API will call this function when the video player is ready.\n" +
                            "      function onPlayerReady(event) {\n" +
                            "        event.target.playVideo();\n" +
                            "      }\n" +
                            "\n" +
                            "      // 5. The API calls this function when the player's state changes.\n" +
                            "      //    The function indicates that when playing a video (state=1),\n" +
                            "      //    the player should play for six seconds and then stop.\n" +
                            "      var done = false;\n" +
                            "      function onPlayerStateChange(event) {\n" +
                            "      }\n" +
                            "      function stopVideo() {\n" +
                            "        player.stopVideo();\n" +
                            "      }\n" +
                            "    </script>\n" +
                            "  </body>\n" +
                            "</html>";


                    webView.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");


                    //player.cueVideo(Videos.get(curr));
                    //player.play();
                }

            }
        });


        //getFragmentManager().beginTransaction().replace(R.id.blank_fragment_stories,youTubePlayerFragment).commit();




        return rootView;
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


}

package com.example.prakharagarwal.fingerlickingawesome;

import android.app.FragmentTransaction;
import android.media.MediaCodec;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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


    YouTubePlayer player;

    ArrayList<String> Videos;
    int end=-1, curr=-1;


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


        final WebView webView= (WebView)rootView.findViewById(R.id.webview_stories);

        final  String url="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+Videos.get(0)+"?rel=0&ecver=1&modestbranding=1&showinfo=0&autohide=1&controls=0&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient());

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        else {
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        }

        webView.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");



        LinearLayout linearLayoutLeft=(LinearLayout)rootView.findViewById(R.id.click_left);
        linearLayoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(curr!=0) {
                    curr--;
                    final  String url="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+Videos.get(curr)+"?rel=0?ecver=1&modestbranding=1&showinfo=0&autohide=1&controls=0&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";

                    webView.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");

                    //player.cueVideo(Videos.get(curr));
                    //player.play();
                }


            }
        });

        LinearLayout linearLayoutRight=(LinearLayout)rootView.findViewById(R.id.click_right);
        linearLayoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(curr!=end) {
                    curr++;
                    final  String url="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+Videos.get(curr)+"?rel=0?ecver=1&modestbranding=1&showinfo=0&autohide=1&controls=0&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";

                    webView.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");


                    //player.cueVideo(Videos.get(curr));
                    //player.play();
                }

            }
        });


        //getFragmentManager().beginTransaction().replace(R.id.blank_fragment_stories,youTubePlayerFragment).commit();




        return rootView;
    }

}

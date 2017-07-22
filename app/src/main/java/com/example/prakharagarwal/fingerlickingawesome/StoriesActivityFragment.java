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


/**
 * A placeholder fragment containing a simple view.
 */
public class StoriesActivityFragment extends Fragment {


    private static final String TAG = "VideoPlayer";

//    public static final int RENDERER_COUNT = 2;
//    public static final int TYPE_AUDIO = 1;
//    private ExoPlayer player;
//    private SurfaceView surface;
//    private String video_url, video_type="hls", video_title;
//    private Handler mainHandler;
//
//    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
//    private static final int BUFFER_SEGMENT_COUNT = 256;




    public StoriesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= (View)inflater.inflate(R.layout.fragment_stories, container, false);



        final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                        if (!b) {
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                       /**/
                            youTubePlayer.cueVideo("u6p6dubzHAc");

                            youTubePlayer.play();
                        }

                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
                        if (errorReason.isUserRecoverableError()) {
                            //errorReason.getErrorDialog(mContext, RECOVERY_REQUEST).show();
                        } else {
                            String error = String.format(getActivity().getString(R.string.player_error), errorReason.toString());
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }

                });

        getFragmentManager().beginTransaction().replace(R.id.blank_fragment_stories,youTubePlayerFragment).commit();

//        VideoView videoView =(VideoView)rootView.findViewById(R.id.videoView);
//        Uri uri=Uri.parse("rtsp://r2---sn-a5m7zu76.c.youtube.com/Ck0LENy73wIaRAnTmlo5oUgpQhMYESARFEgGUg5yZWNvbW1lbmRhdGlvbnIhAWL2kyn64K6aQtkZVJdTxRoO88HsQjpE1a8d1GxQnGDmDA==/0/0/0/video.3gp");
//        videoView.setVideoURI(uri);
//        videoView.requestFocus();
//
//        videoView.start();



//        String youTubeVideoID = "HoXDsTIu_10";
//
//        YouTubeVideoInfoRetriever retriever = new YouTubeVideoInfoRetriever();
//
//        try
//        {
//            retriever.retrieve(youTubeVideoID);
//            video_url=retriever.getInfo(YouTubeVideoInfoRetriever.KEY_DASH_VIDEO);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }

//        surface=(SurfaceView)rootView.findViewById(R.id.surface_view);
//        video_type="others";
//        video_url="http://player.hungama.com/mp3/91508493.mp4";
//        video_title = "Big Buck Bunny";
//        mainHandler=new Handler();
//        execute();




        return rootView;
    }

//    private void execute() {
//        player=ExoPlayer.Factory.newInstance(RENDERER_COUNT);
//        if(player!=null) {
//
//            Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
//            String userAgent = Util.getUserAgent(getActivity(), "HpLib");
//            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(mainHandler, null);
//            DataSource dataSource = new DefaultUriDataSource(getActivity(), bandwidthMeter, userAgent);
//            ExtractorSampleSource sampleSource = new ExtractorSampleSource(Uri.parse(video_url), dataSource, allocator,
//                    BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);
//            MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(getActivity(),
//                    sampleSource, MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5000);
//            MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT,null,true);
//            player.prepare(videoRenderer,audioRenderer);
//
//
//
//        }
//    }


}

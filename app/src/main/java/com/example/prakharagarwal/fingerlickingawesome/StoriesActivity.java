package com.example.prakharagarwal.fingerlickingawesome;

import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;


public class StoriesActivity extends FragmentActivity {

    private static final String TAG = "VideoPlayer";



    WebView webView;
    ArrayList<String> Videos;
    int end=-1, curr=-1;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

    }


}

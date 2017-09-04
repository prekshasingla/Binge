package com.example.prakharagarwal.binge.Review;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.prakharagarwal.binge.LoginActivity;
import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewActivityFragment extends Fragment {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    static TextView emptyView;

    TextView videoBtn;
    View rootView;
    List<Review> reviews;

    File videoFile;
    Uri videoUri;
    String video = null;

    WebView webView;

    String id;
    String restaurantName;


    VideoView videoView;
    ArrayList<String> Videos;
    int end = -1, curr = -1;
    final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 100;


    public ReviewActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_review, container, false);

        emptyView = (TextView)rootView.findViewById(R.id.review_story_empty);

        reviews = new ArrayList<Review>();

        Intent intent = getActivity().getIntent();
        id = intent.getStringExtra("restaurantID");
        restaurantName = intent.getStringExtra("restaurantName");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("reviews");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviews.clear();
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("error", "error");
            }
        });
        DatabaseReference ref1 = database.getReference("story_reviews");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getVideoReviews(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Videos = new ArrayList<String>();

        curr = 0;
        end = Videos.size();


        webView = (WebView) rootView.findViewById(R.id.webview_reviews);

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


        LinearLayout linearLayoutLeft = (LinearLayout) rootView.findViewById(R.id.click_left_review);
        linearLayoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr != 0) {
                    curr--;
                    webView.loadDataWithBaseURL("", getYoutubeURL(Videos.get(curr)), "text/html", "UTF-8", "");
                }

            }
        });

        LinearLayout linearLayoutCentre = (LinearLayout) rootView.findViewById(R.id.centre_review);
        linearLayoutCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        LinearLayout linearLayoutRight = (LinearLayout) rootView.findViewById(R.id.click_left_review);
        linearLayoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (curr != end - 1) {
                    curr++;
                    webView.loadDataWithBaseURL("", getYoutubeURL(Videos.get(curr)), "text/html", "UTF-8", "");
                }

            }
        });


        videoBtn = (TextView) rootView.findViewById(R.id.btnSelectVideo);
        videoBtn.performClick();
        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
//            prefs.edit().clear();
                String uID = prefs.getString("username", null);
//                Log.e("Uid", uID);
                if (uID == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                } else {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(getActivity(), "Please grant storage permission in Settings/Apps/Binge/Permissions", Toast.LENGTH_SHORT).show();

                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    } else {
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                } else {
                    Toast.makeText(getActivity(), "Please Grant Storage Permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
    }

    public void getVideoReviews(DataSnapshot dataSnapshot) {
        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
            if (child1.getKey().equals(id)) {
                for (DataSnapshot child2 : child1.getChildren()) {
                    for (DataSnapshot child3 : child2.getChildren()) {
                        if (child3.getKey().equals("youtube_id")) {
                            Videos.add("" + child3.getValue());
                        }
                    }
                }

            }

        }
        if (Videos.size() != 0) {
            webView.loadDataWithBaseURL("", getYoutubeURL(Videos.get(0)), "text/html", "UTF-8", "");
            emptyView.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
        }
    }

    public void getData(DataSnapshot dataSnapshot) {
        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
            if (child1.getKey().equals(id)) {
                for (DataSnapshot child2 : child1.getChildren()) {
                    Review review = new Review();
                    review.setUserid("" + child2.getKey());
                    Log.e("userid", child2.getKey());

                    for (DataSnapshot child3 : child2.getChildren()) {

                        if (child3.getKey().equals("review")) {
                            review.setReview("" + child3.getValue());
                        }
                        if (child3.getKey().equals("rating")) {
                            review.setRating(Float.parseFloat("" + child3.getValue()));
                        }
                        if (child3.getKey().equals("title")) {
                            review.setTitle("" + child3.getValue());
                        }
                        if (child3.getKey().equals("epoch")) {
                            review.setEpoch((long) child3.getValue());
                        }
                    }
                    reviews.add(review);
                }
            }
        }

        ((ReviewActivity) getActivity()).addAllReviews(reviews, id, restaurantName);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            videoUri = data.getData();
            videoFile = new File(getRealPathFromUri(videoUri));
            video = getRealPathFromUri(videoUri);
            long epoch = System.currentTimeMillis();

            Intent intent = new Intent(getActivity(), UploadReviewStoryActivity.class);
            intent.putExtra("epoch", epoch);
            intent.putExtra("video", video);
            intent.putExtra("restaurantID", id);
            intent.putExtra("restaurantName", restaurantName);
            intent.putExtra("user", "user1");
            getActivity().startActivity(intent);
        }
    }

    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(tempUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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
                "         'autohide': 1,\n" +
                "         'controls': 0, \n" +
                "         'showinfo': 0,\n" +
                "         'playlist': '" + videoID + "',\n" +
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


}
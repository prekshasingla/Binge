package com.example.prakharagarwal.binge.Review;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

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

    TextView videoBtn;
    View rootView;
    List<Review> reviews;

    File videoFile;
    Uri videoUri;
    String video = null;

    VideoView videoView;
    ArrayList<String> Videos;
    int end = -1, curr = -1;


    public ReviewActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_review, container, false);
        reviews = new ArrayList<Review>();

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

        videoView = (VideoView) rootView.findViewById(R.id.videoview_reviews);

        hideSystemUi();

        videoView.setVerticalScrollBarEnabled(false);
        videoView.setHorizontalScrollBarEnabled(false);


        videoView.setScrollContainer(false);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        String video = "https://firebasestorage.googleapis.com/v0/b/finalfingerlickingawesome.appspot.com/o/videos%2Friver4.mp4?alt=media&token=8f72b197-f211-405a-870d-09ec444149c9";

//        videoView.setVideoURI(Uri.parse(video));

        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        videoBtn = (TextView) rootView.findViewById(R.id.btnSelectVideo);
        videoBtn.performClick();
        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

            }
        });

        return rootView;
    }

    public void getVideoReviews(DataSnapshot dataSnapshot) {
        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
            for(DataSnapshot child2: child1.getChildren()) {
                if (child2.getKey().equals("uri")) {
                    Videos.add("" + child2.getValue());
                }
            }

        }
        videoView.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/finalfingerlickingawesome.appspot.com/o/videos%2Friver4.mp4?alt=media&token=8f72b197-f211-405a-870d-09ec444149c9"));

    }

    public void getData(DataSnapshot dataSnapshot) {

        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
            Review review = new Review();

            for (DataSnapshot child2 : child1.getChildren()) {

                if (child2.getKey().equals("userid")) {
                    // Log.e("Restaurant",""+child2.getValue());
                    review.setUserid("" + child2.getValue());
                }
                if (child2.getKey().equals("review")) {
                    // Log.e("Restaurant",""+child2.getValue());
                    review.setReview("" + child2.getValue());
                }
                if (child2.getKey().equals("rating")) {
                    // Log.e("Restaurant",""+child2.getValue());
                    review.setRating(Float.parseFloat("" + child2.getValue()));
                }
                if (child2.getKey().equals("restaurant")) {
                    // Log.e("Restaurant",""+child2.getValue());
                    review.setRestaurant("" + child2.getValue());
                }

            }
            reviews.add(review);

        }

        ((ReviewActivity) getActivity()).addAllReviews(reviews);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            videoUri = data.getData();
            videoFile = new File(getRealPathFromUri(videoUri));
            video = getRealPathFromUri(videoUri);

            Intent intent = new Intent(getActivity(), UploadReviewStoryActivity.class);
            intent.putExtra("video", video);
            getActivity().startActivity(intent);

            Log.d("data video", "" + videoFile);
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


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

}

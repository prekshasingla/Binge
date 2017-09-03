package com.example.prakharagarwal.binge.Review;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;

import com.example.prakharagarwal.binge.R;

public class UploadReviewStoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_review_story);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //    finish();

    }
}

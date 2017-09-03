package com.example.prakharagarwal.binge.Review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends FragmentActivity {

    RecyclerView mRecyclerView;
    ReviewTextAdapter reviewTextAdapter;
    List<Review> reviews;
    SlidingUpPanelLayout slidingUpPanelLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        reviews=new ArrayList<Review>();


        slidingUpPanelLayout=(SlidingUpPanelLayout)findViewById(R.id.reviews_sliding_up);

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState== SlidingUpPanelLayout.PanelState.EXPANDED)
                    ((ImageView)findViewById(R.id.review_text_heading_image)).setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);


                if(newState== SlidingUpPanelLayout.PanelState.COLLAPSED)
                    ((ImageView)findViewById(R.id.review_text_heading_image)).setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);

            }
        });


        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);

        mRecyclerView=(RecyclerView)findViewById(R.id.review_text_fragment_recycler_view);
        reviewTextAdapter= new ReviewTextAdapter(this,reviews);
        try{
            mRecyclerView.setAdapter(reviewTextAdapter);
        }catch (NoClassDefFoundError e){

        }
        mRecyclerView.setLayoutManager(linearLayoutManager);

        TextView buttonWriteReview=(TextView) findViewById(R.id.button_write_review);
        buttonWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReviewActivity.this,WriteReviewActivity.class);
                intent.putExtra("restaurant","cafe_hangouts_faridabad");
                intent.putExtra("user","user1");
                startActivity(intent);

            }
        });



    }

public void addAllReviews(List<Review> reviews){
    reviewTextAdapter.addAll(reviews);
    reviewTextAdapter.notifyDataSetChanged();
}
    public SlidingUpPanelLayout getSlidingUpPanelLayout(){
        return slidingUpPanelLayout;
    }

}

package com.example.prakharagarwal.binge.Review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends FragmentActivity {

    RecyclerView mRecyclerView;
    ReviewTextAdapter reviewTextAdapter;
    List<Review> reviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        reviews=new ArrayList<Review>();


        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);

        mRecyclerView=(RecyclerView)findViewById(R.id.review_text_fragment_recycler_view);
        reviewTextAdapter= new ReviewTextAdapter(this,reviews);
        try{
            mRecyclerView.setAdapter(reviewTextAdapter);
        }catch (NoClassDefFoundError e){

        }
        mRecyclerView.setLayoutManager(linearLayoutManager);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviews.clear();
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("error","error");
            }
        });

        Button buttonWriteReview=(Button)findViewById(R.id.button_write_review);
        buttonWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReviewActivity.this,WriteReviewActivity.class);
                startActivity(intent);

            }
        });



    }

    public void getData(DataSnapshot dataSnapshot) {

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            if (child.getKey().equals("reviews")) {
                for (DataSnapshot child1 : child.getChildren()) {
                    Review review=new Review();

                    for (DataSnapshot child2 : child1.getChildren()) {

                        if (child2.getKey().equals("userid")){
                            // Log.e("Restaurant",""+child2.getValue());
                            review.setUserid(""+child2.getValue());
                        }
                        if (child2.getKey().equals("review")){
                            // Log.e("Restaurant",""+child2.getValue());
                            review.setReview(""+child2.getValue());
                        }
                        if (child2.getKey().equals("rating")){
                            // Log.e("Restaurant",""+child2.getValue());
                            review.setRating(Float.parseFloat(""+child2.getValue()));
                        }
                        if (child2.getKey().equals("restaurant")){
                            // Log.e("Restaurant",""+child2.getValue());
                            review.setRestaurant(""+child2.getValue());
                        }

                    }
                    reviews.add(review);

                }
            }
        }

        reviewTextAdapter.addAll(reviews);
        reviewTextAdapter.notifyDataSetChanged();
    }


}

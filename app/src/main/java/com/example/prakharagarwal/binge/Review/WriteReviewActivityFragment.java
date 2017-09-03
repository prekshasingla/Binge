package com.example.prakharagarwal.binge.Review;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A placeholder fragment containing a simple view.
 */
public class WriteReviewActivityFragment extends Fragment {

    public WriteReviewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView=(View)inflater.inflate(R.layout.fragment_write_review, container, false);

        Intent intent=getActivity().getIntent();
        final String restaurantName=intent.getStringExtra("restaurant");
        final String userId=intent.getStringExtra("user");


        TextView textViewRestaurant=(TextView) rootView.findViewById(R.id.title_write_review);
        textViewRestaurant.setText(restaurantName);
        Button buttonSubmitReviewText=(Button)rootView.findViewById(R.id.button_submit_review);
        buttonSubmitReviewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTextReview=(EditText)rootView.findViewById(R.id.review_text);
                String review= ""+editTextReview.getText();
                RatingBar ratingBarReview=(RatingBar)rootView.findViewById(R.id.rating_bar);
                float rating=ratingBarReview.getRating();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("reviews").child(restaurantName).child(userId);

                long epoch= System.currentTimeMillis();

                myRef.setValue(new Review(review,rating,epoch));

                getActivity().finish();

            }
        });


        return rootView;
    }


}

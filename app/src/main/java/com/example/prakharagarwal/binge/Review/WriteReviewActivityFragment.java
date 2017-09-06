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


    EditText editTextReview;
    EditText editTextTitle;
    RatingBar ratingBarReview;
    public WriteReviewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView=(View)inflater.inflate(R.layout.fragment_write_review, container, false);

        Intent intent=getActivity().getIntent();
        final String restaurantID=intent.getStringExtra("restaurantID");
        final String restaurantName=intent.getStringExtra("restaurantName");
        final String userId=intent.getStringExtra("user");

        editTextReview=(EditText)rootView.findViewById(R.id.review_text);
        editTextTitle = (EditText) rootView.findViewById(R.id.review_title);
        ratingBarReview = (RatingBar) rootView.findViewById(R.id.rating_bar);




        final TextView textViewError=(TextView) rootView.findViewById(R.id.write_review_error);


        TextView textViewRestaurant=(TextView) rootView.findViewById(R.id.title_write_review);
        textViewRestaurant.setText(restaurantName);
        Button buttonSubmitReviewText=(Button)rootView.findViewById(R.id.button_submit_review);
        buttonSubmitReviewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextReview.getText().toString().equals("") && editTextTitle.getText().toString().equals("") && ratingBarReview.getRating()==0){
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Above fields cannot be blank");
                }
                else  if(editTextReview.getText().toString().equals("")){
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Description cannot be blank");
                }
                else  if(editTextTitle.getText().toString().equals("")){
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Title cannot be blank");
                }
                else  if(ratingBarReview.getRating()==0) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Rating cannot be zero");
                }

                else {
                    String review = "" + editTextReview.getText();
                    String title = "" + editTextTitle.getText();

                    float rating = ratingBarReview.getRating();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("reviews").child(restaurantID).child(userId);

                    long epoch = System.currentTimeMillis();

                    myRef.setValue(new Review(title, review, rating, epoch));

                    getActivity().finish();
                }

            }
        });


        return rootView;
    }


}

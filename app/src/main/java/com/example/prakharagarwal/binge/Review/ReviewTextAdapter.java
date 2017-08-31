package com.example.prakharagarwal.binge.Review;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;

import java.util.List;

/**
 * Created by prekshasingla on 24/08/17.
 */

public class ReviewTextAdapter extends RecyclerView.Adapter<ReviewTextAdapter.ReviewTextAdapterViewHolder> {
    private List<Review> reviews;
    final private Context mContext;
    ReviewTextAdapter.ReviewTextAdapterViewHolder holder;
    private void bindActivity() {
    }


    public ReviewTextAdapter(Context context, List<Review> reviews) {
        mContext = context;
        this.reviews=reviews;
    }


    @Override
    public ReviewTextAdapter.ReviewTextAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_adapter_item, parent, false);
        holder = new ReviewTextAdapter.ReviewTextAdapterViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(final ReviewTextAdapter.ReviewTextAdapterViewHolder holder, final int position) {


        final String review = reviews.get(position).review;
        final String userid = reviews.get(position).userid;
        final float rating= reviews.get(position).rating;


        holder.textViewUserid.setText(userid);
        holder.textViewReview.setText(review);
        holder.ratingBar.setRating(rating);
        holder.ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        if(reviews.get(position).veg){
//            holder.imageViewVeg.setImageResource(R.mipmap.veg);
//
//        }
//        else {
//            holder.imageViewVeg.setImageResource(R.mipmap.nonveg);
//        }
    }



    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public class ReviewTextAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewUserid;
        public TextView textViewReview;
        public RatingBar ratingBar;


        public ReviewTextAdapterViewHolder(View view) {
            super(view);

            textViewUserid = (TextView) view.findViewById(R.id.review_text_user_name);
            textViewReview = (TextView) view.findViewById(R.id.review_text_description);
            ratingBar = (RatingBar) view.findViewById(R.id.review_text_rating_bar);

        }
    }

    public void addAll(List<Review> reviews) {
        ReviewTextAdapter.this.reviews=reviews;
    }

    public void removeAll(){
        ReviewTextAdapter.this.reviews=null;
    }

}


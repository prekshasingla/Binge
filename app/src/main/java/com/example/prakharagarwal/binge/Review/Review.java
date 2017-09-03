package com.example.prakharagarwal.binge.Review;

/**
 * Created by prekshasingla on 22/08/17.
 */

public class Review {

    String review;
    float rating;
    String userid;
    long epoch;


    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Review(){

    };


    public Review(String review,float rating, long epoch){
        this.review=review;
        this.rating=rating;
        this.epoch=epoch;
    }


}

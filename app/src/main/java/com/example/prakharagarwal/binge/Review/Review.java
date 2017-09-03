package com.example.prakharagarwal.binge.Review;

/**
 * Created by prekshasingla on 22/08/17.
 */

public class Review {

    String review;
    String title;
    float rating;
    String userid;
    long epoch;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


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


    public Review(String title,String review,float rating, long epoch){
        this.title=title;
        this.review=review;
        this.rating=rating;
        this.epoch=epoch;
    }


}

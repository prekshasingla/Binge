package com.example.prakharagarwal.fingerlickingawesome.Review;

/**
 * Created by prekshasingla on 22/08/17.
 */

public class Review {

    String review;
    float rating;
    String restaurant;
    String userid;

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

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Review(){

    };


    public Review(String review,float rating, String restaurant, String userid){
        this.review=review;
        this.rating=rating;
        this.restaurant=restaurant;
        this.userid=userid;
    }


}

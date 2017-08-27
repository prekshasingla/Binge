package com.example.prakharagarwal.fingerlickingawesome.Review;

import android.net.Uri;

/**
 * Created by prekshasingla on 26/08/17.
 */

public class StoryReview {


    Uri uri;
    String restaurant;
    String userid;


    public StoryReview(){

    };


    public StoryReview(Uri uri, String restaurant, String userid){
        this.uri=uri;
        this.restaurant=restaurant;
        this.userid=userid;
    }

}

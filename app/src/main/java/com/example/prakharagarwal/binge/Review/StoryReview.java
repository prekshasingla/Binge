package com.example.prakharagarwal.binge.Review;

/**
 * Created by prekshasingla on 26/08/17.
 */

public class StoryReview {


     String uri;
     String userid;


    String youtube_id;

    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    long epoch;


    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public StoryReview(){

    };


    public StoryReview(String uri, long epoch, String youtube_id){
        this.uri=uri;
        this.epoch=epoch;
        this.youtube_id=youtube_id;
    }

}

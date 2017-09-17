package com.example.prakharagarwal.binge.Review;

/**
 * Created by prekshasingla on 26/08/17.
 */

public class StoryReview {


     String uri;
     String userid;
    Long has_video;
    String youtube_id;

    Long epoch;


    public Long getEpoch() {
        return epoch;
    }

    public void setEpoch(Long epoch) {
        this.epoch = epoch;
    }


    public Long getHas_video() {
        return has_video;
    }

    public void setHas_video(Long has_video) {
        this.has_video = has_video;
    }


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


    public StoryReview(String uri, long epoch,long has_video, String youtube_id){
        this.uri=uri;
        this.epoch=epoch;
        this.has_video=has_video;
        this.youtube_id=youtube_id;
    }

}

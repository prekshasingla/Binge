package com.example.prakharagarwal.binge.MainScreen;

/**
 * Created by prekshasingla on 05/07/17.
 */
public class Restaurant {

    public String name;
    public String video;
    public String address;
    public String lattitude;
    public String longitude;
    public String typeOfRestaurant;
    public int ambienceEndTime;
    public int ambienceStartTime;
    public String closingTime;
    public String cuisineType;
    public String openingTime;
    public int signatureEndTime;
    public int signatureStartTime;
    String id;
    public int costForTwo;
    String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Restaurant(){
        costForTwo=-1;
    }
    public int getCostForTwo() {
        return costForTwo;
    }

    public void setCostForTwo(int costForTwo) {
        this.costForTwo = costForTwo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTypeOfRestaurant() {
        return typeOfRestaurant;
    }

    public void setTypeOfRestaurant(String typeOfRestaurant) {
        this.typeOfRestaurant = typeOfRestaurant;
    }

    public int getAmbienceEndTime() {
        return ambienceEndTime;
    }

    public void setAmbienceEndTime(int ambienceEndTime) {
        this.ambienceEndTime = ambienceEndTime;
    }

    public int getAmbienceStartTime() {
        return ambienceStartTime;
    }

    public void setAmbienceStartTime(int ambienceStartTime) {
        this.ambienceStartTime = ambienceStartTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public int getSignatureEndTime() {
        return signatureEndTime;
    }

    public void setSignatureEndTime(int signatureEndTime) {
        this.signatureEndTime = signatureEndTime;
    }

    public int getSignatureStartTime() {
        return signatureStartTime;
    }

    public void setSignatureStartTime(int signatureStartTime) {
        this.signatureStartTime = signatureStartTime;
    }


}

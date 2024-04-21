package com.project.bookmyroom.viewmodel;

import java.io.Serializable;

public class RecentsData implements Serializable {

    String placeName;
    String countryName;
    String price;
    String imageUrl;
    String address;
    String contactNumber;

    public RecentsData(String placeName, String countryName, String price, String imageUrl, String address, String contactNumber) {
        this.placeName = placeName;
        this.countryName = countryName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}

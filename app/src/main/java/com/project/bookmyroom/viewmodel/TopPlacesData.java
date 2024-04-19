package com.project.bookmyroom.viewmodel;

public class TopPlacesData {

    String placeName;
    String countryName;
    String price;
    String imageUrl;

    public String getImageUrl() { // Changed to return String
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) { // Changed parameter type to String
        this.imageUrl = imageUrl;
    }
    public TopPlacesData(String placeName, String countryName, String price, String imageUrl) {
        this.placeName = placeName;
        this.countryName = countryName;
        this.price = price;
        this.imageUrl = imageUrl;
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
}

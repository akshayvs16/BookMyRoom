package com.project.bookmyroom.viewmodel;
import java.io.Serializable;

public class TopPlacesData  implements Serializable {

    String placeName;
    String countryName;
    String price;
    String imageUrl;



    String description;


    public String getImageUrl() { // Changed to return String
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) { // Changed parameter type to String
        this.imageUrl = imageUrl;
    }
    public TopPlacesData(String placeName, String countryName, String price, String imageUrl,String description) {
        this.placeName = placeName;
        this.countryName = countryName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description=description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

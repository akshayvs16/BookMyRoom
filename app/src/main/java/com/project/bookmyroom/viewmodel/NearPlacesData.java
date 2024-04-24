package com.project.bookmyroom.viewmodel;

import java.io.Serializable;

public class NearPlacesData implements Serializable {

    private String placeName;
    private String countryName;
    private String price;
    private String imageUrl;
    private String description;
    private String address;
    private String lat;
    private String nearByRailStops;
    private String nearByBusStops;
    private String districtId;
    private String image;
    private String name;



    private String id;

    public NearPlacesData(   String name, String address, String description, String districtId, String nearByBusStops, String nearByRailStops, String lat,  String image,String id) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.nearByBusStops = nearByBusStops;
        this.nearByRailStops = nearByRailStops;
        this.lat = lat;
        this.image = image;
        this.districtId = districtId;
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getNearByRailStops() {
        return nearByRailStops;
    }

    public void setNearByRailStops(String nearByRailStops) {
        this.nearByRailStops = nearByRailStops;
    }

    public String getNearByBusStops() {
        return nearByBusStops;
    }

    public void setNearByBusStops(String nearByBusStops) {
        this.nearByBusStops = nearByBusStops;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

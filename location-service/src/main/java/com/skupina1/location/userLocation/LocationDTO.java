package com.skupina1.location.userLocation;

//DTO to store the coordinates of the person
//used in PATCH request
//lng --> double
//lat --> double
public class LocationDTO {
    private double lng;
    private double lat;
    public LocationDTO(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }
    public LocationDTO(){

    }
    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
}
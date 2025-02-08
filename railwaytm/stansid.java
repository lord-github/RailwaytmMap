package com.bbstudios.railwaytm;

public class stansid {
    String region,ady;
    Double lat,lon;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAdy() {
        return ady;
    }

    public void setAdy(String ady) {
        this.ady = ady;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public stansid(String region, String ady, Double lat, Double lon) {
        this.region = region;
        this.ady = ady;
        this.lat = lat;
        this.lon = lon;
    }
}

package com.example.capstone.map;

//마커부분. Getter Setter 라고 보면 됨
public class MarkerItem {
    private double lat;
    private double lon;
    private String placeTitle;
    private String placeInfo;
    private String preferenceRatio;

    public MarkerItem(double lat, double lon, String placeTitle, String placeInfo, String preferenceRatio) {
        this.lat = lat;
        this.lon = lon;
        this.placeTitle = placeTitle;
        this.placeInfo = placeInfo;
        this.preferenceRatio = preferenceRatio;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public void setPlaceTitle(String placeTitle) {
        this.placeTitle = placeTitle;
    }

    public String getPlaceInfo() { return placeInfo; }

    public void setPlaceInfo(String placeInfo) { this.placeInfo = placeInfo; }

    public String getPreferenceRatio() {
        return preferenceRatio;
    }

    public void setPreferenceRatio(String preferenceRatio) {
        this.preferenceRatio = preferenceRatio;
    }

}

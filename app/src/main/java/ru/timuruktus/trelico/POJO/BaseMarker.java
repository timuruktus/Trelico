package ru.timuruktus.trelico.POJO;

import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

public class BaseMarker extends SugarRecord{

    private String title, snippet;
    private LatLng coordinates;
    private double lat, lng;

    public BaseMarker(String title, String snippet, double lat, double lng) {
        this.title = title;
        this.snippet = snippet;
        this.lat = lat;
        this.lng = lng;
//        this.coordinates = new LatLng(lat, lng);
    }

    public BaseMarker() {
    }

    public LatLng calculateCoordinates(){
        this.coordinates = new LatLng(lat, lng);
        return this.coordinates;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

}

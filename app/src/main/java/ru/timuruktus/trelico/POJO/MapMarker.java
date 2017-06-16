package ru.timuruktus.trelico.POJO;

public class MapMarker {
    private int lat;
    private int lng;
    private String title;
    private String snippet;


    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
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


    public MapMarker(int lat, int lng, String title, String snippet) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.snippet = snippet;
    }

    public MapMarker() {
    }
}

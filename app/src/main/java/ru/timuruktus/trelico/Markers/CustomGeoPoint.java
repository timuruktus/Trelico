package ru.timuruktus.trelico.Markers;

import com.backendless.geo.GeoPoint;
import com.orm.SugarRecord;

public class CustomGeoPoint extends SugarRecord {

    private GeoPoint geoPoint;

    public CustomGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }


    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

}

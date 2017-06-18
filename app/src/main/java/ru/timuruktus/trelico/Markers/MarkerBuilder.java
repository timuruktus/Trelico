package ru.timuruktus.trelico.Markers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.timuruktus.trelico.POJO.BaseMarker;

public class MarkerBuilder {

    private BaseMarker marker;

    public MarkerBuilder(BaseMarker marker){
        this.marker = marker;
    }

    public MarkerOptions getMarker(){
        LatLng coordinates = marker.getCoordinates();
        String title = marker.getTitle();
        String snippet = marker.getSnippet();
        return new MarkerOptions().position(coordinates).title(title).snippet(snippet);
    }

    public void showMarker(GoogleMap map){
        map.addMarker(getMarker());
    }

    public static void showMarker(GoogleMap map, BaseMarker marker){
        LatLng coordinates = marker.getCoordinates();
        String title = marker.getTitle();
        String snippet = marker.getSnippet();
        map.addMarker(new MarkerOptions().position(coordinates).title(title).snippet(snippet));
    }

}

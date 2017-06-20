package ru.timuruktus.trelico.MapPart.Interafaces;

import java.util.List;

import ru.timuruktus.trelico.Markers.DownloadListener;
import ru.timuruktus.trelico.POJO.CustomGeoPoint;


public interface BaseMapModel {


    DownloadListener getMarkersDownloadListener();
    boolean isMarkersDownloaded();
    List<CustomGeoPoint> getAllMarkerInRadius(BaseLocationTracker locationTracker);
    List<CustomGeoPoint> getAllMarkers();
    void buildLocationManager();
    CustomLocationListener getLocationListener();
    List<CustomGeoPoint> getAllMarkerInRadius(double currentLat, double currentLng);

}

package ru.timuruktus.trelico.MapPart.Interafaces;

import java.util.List;

import ru.timuruktus.trelico.Markers.DownloadListener;
import ru.timuruktus.trelico.POJO.BaseMarker;


public interface BaseMapModel {


    DownloadListener getMarkersDownloadListener();
    boolean isMarkersDownloaded();
    List<BaseMarker> getAllMarkerInRadius(BaseLocationTracker locationTracker);
    List<BaseMarker> getAllMarkers();
    void buildLocationManager();
    CustomLocationListener getLocationListener();
    List<BaseMarker> getAllMarkerInRadius(double currentLat, double currentLng);

}

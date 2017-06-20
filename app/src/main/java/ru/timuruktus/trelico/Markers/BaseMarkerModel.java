package ru.timuruktus.trelico.Markers;

public interface BaseMarkerModel{


    void downloadAllMarkers(DownloadListener listener);
    void refreshAllMarkers(DownloadListener listener);
}

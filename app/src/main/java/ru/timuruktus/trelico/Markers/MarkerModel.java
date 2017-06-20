package ru.timuruktus.trelico.Markers;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

import ru.timuruktus.trelico.POJO.BaseMarker;
import ru.timuruktus.trelico.POJO.CustomGeoPoint;

public class MarkerModel implements BaseMarkerModel {

    private int offset = 0;
    private ArrayList<CustomGeoPoint> finalMarkers = null;
    private boolean firstCall = true;


    @Override
    public void downloadAllMarkers(DownloadListener listener) {
        if(firstCall) {
            finalMarkers = new ArrayList<>();
        }
        firstCall = false;
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100);
        queryBuilder.setOffset(offset);
        Backendless.Persistence.of(CustomGeoPoint.class).find(queryBuilder, getDownloadListener(listener));
        listener.onStart();
    }

    private AsyncCallback<List<CustomGeoPoint>> getDownloadListener(DownloadListener listener){
        return new AsyncCallback<List<CustomGeoPoint>>(){
            @Override
            public void handleResponse(List<CustomGeoPoint> foundMarkers) {
                int size = foundMarkers.size();
                ArrayList<CustomGeoPoint> tasks = (ArrayList<CustomGeoPoint>) foundMarkers;
                if(size > 0){
                    offset += tasks.size();
                    finalMarkers.addAll(tasks);
                    downloadAllMarkers(listener);
                }else{
                    offset = 0;
                    for(CustomGeoPoint marker : finalMarkers){
                        marker.save();
                    }
                    listener.onComplete();
                    firstCall = true;
                }
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                listener.onError(fault);
            }
        };
    }

    private AsyncCallback<List<CustomGeoPoint>> getRefreshListener(DownloadListener listener){
        return new AsyncCallback<List<CustomGeoPoint>>(){
            @Override
            public void handleResponse(List<CustomGeoPoint> foundMarkers) {
                int size = foundMarkers.size();
                ArrayList<CustomGeoPoint> tasks = (ArrayList<CustomGeoPoint>) foundMarkers;
                if(size > 0){
                    offset += tasks.size();
                    finalMarkers.addAll(tasks);
                    downloadAllMarkers(listener);
                }else{
                    offset = 0;
                    CustomGeoPoint.deleteAll(CustomGeoPoint.class);
                    for(CustomGeoPoint marker : finalMarkers){
                        marker.save();
                    }
                    listener.onComplete();
                    firstCall = true;
                }
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                listener.onError(fault);
            }
        };
    }

    @Override
    public void refreshAllMarkers(DownloadListener listener){
        if(firstCall) {
            finalMarkers = new ArrayList<>();
        }
        firstCall = false;
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100);
        queryBuilder.setOffset(offset);
        Backendless.Persistence.of(CustomGeoPoint.class).find(queryBuilder, getRefreshListener(listener));
        listener.onStart();
    }

}

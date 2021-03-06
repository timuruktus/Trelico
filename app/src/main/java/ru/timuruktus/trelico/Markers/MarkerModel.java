package ru.timuruktus.trelico.Markers;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

import ru.timuruktus.trelico.POJO.BaseMarker;

public class MarkerModel implements BaseMarkerModel {

    private int offset = 0;
    private ArrayList<BaseMarker> finalMarkers = null;
    private boolean firstCall = true;


    @Override
    public void downloadAllMarkers(DownloadListener listener) {
        if(firstCall) {
            finalMarkers = new ArrayList<>();
            listener.onStart();
        }
        firstCall = false;
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100);
        queryBuilder.setOffset(offset);
        Backendless.Persistence.of(BaseMarker.class).find(queryBuilder, getDownloadListener(listener));
    }

    private AsyncCallback<List<BaseMarker>> getDownloadListener(DownloadListener listener){
        return new AsyncCallback<List<BaseMarker>>(){
            @Override
            public void handleResponse(List<BaseMarker> foundMarkers) {
//                        if(foundMarkers == null) Log.d("mytag", "Found tasks is null");
                int size = foundMarkers.size();
//                        Log.d("mytag", "Found tasks size = " + size);
                ArrayList<BaseMarker> tasks = (ArrayList<BaseMarker>) foundMarkers;
                if(size > 0){
                    offset += tasks.size();
                    finalMarkers.addAll(tasks);
                    downloadAllMarkers(listener);
                }else{
                    offset = 0;
                    for(BaseMarker marker : finalMarkers){
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

    private AsyncCallback<List<BaseMarker>> getRefreshListener(DownloadListener listener){
        return new AsyncCallback<List<BaseMarker>>(){
            @Override
            public void handleResponse(List<BaseMarker> foundMarkers) {
//                        if(foundMarkers == null) Log.d("mytag", "Found tasks is null");
                int size = foundMarkers.size();
//                        Log.d("mytag", "Found tasks size = " + size);
                ArrayList<BaseMarker> tasks = (ArrayList<BaseMarker>) foundMarkers;
                if(size > 0){
                    offset += tasks.size();
                    finalMarkers.addAll(tasks);
                    refreshAllMarkers(listener);
                }else{
                    offset = 0;
                    BaseMarker.deleteAll(BaseMarker.class);
                    for(BaseMarker marker : finalMarkers){
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
            listener.onStart();
        }
        firstCall = false;
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100);
        queryBuilder.setOffset(offset);
        Backendless.Persistence.of(BaseMarker.class).find(queryBuilder, getRefreshListener(listener));

    }

}

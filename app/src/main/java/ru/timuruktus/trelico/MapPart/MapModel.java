package ru.timuruktus.trelico.MapPart;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

import ru.timuruktus.trelico.MapPart.Interfaces.BaseLocationTracker;
import ru.timuruktus.trelico.MapPart.Interfaces.BaseMapModel;
import ru.timuruktus.trelico.MapPart.Interfaces.BaseMapPresenter;
import ru.timuruktus.trelico.MapPart.Interfaces.CustomLocationListener;
import ru.timuruktus.trelico.Markers.DownloadListener;
import ru.timuruktus.trelico.POJO.BaseMarker;
import ru.timuruktus.trelico.R;

import static android.location.LocationProvider.AVAILABLE;
import static ru.timuruktus.trelico.MapPart.LocationTracker.CIRCLE_SEARCH_RADIUS;

class MapModel implements BaseMapModel {


    public static final int SNACKBAR_DURATION = 10000;
    private BaseMapPresenter presenter;

    MapModel(BaseMapPresenter presenter) {
        this.presenter = presenter;
    }


    public DownloadListener getMarkersDownloadListener(){
        Context context = presenter.askForContext();
        return new DownloadListener() {
            @Override
            public void onStart() {
                Toast.makeText(context, R.string.start_markers_download, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                presenter.updateMap();
                presenter.animateCameraToUser();
            }

            @Override
            public void onError(BackendlessFault fault) {
                Log.d("mytag", "Download error. More info: " + fault.toString() +
                        " Details: " + fault.getDetail() +
                " Message: " + fault.getMessage() + " Code: " + fault.getCode());
                Toast.makeText(context, R.string.need_internet_to_download, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unsubscribe() {
                setSubscribed(false);
            }
        };
    }

    public DownloadListener getMarkersRefreshListener(View view){
        Context context = presenter.askForContext();
        return new DownloadListener() {
            @Override
            public void onStart() {
                Snackbar snackbar = Snackbar.make(view
                        ,R.string.refreshing_markers,
                        Snackbar.LENGTH_LONG);
                snackbar.setDuration(SNACKBAR_DURATION);
                snackbar.show();
            }

            @Override
            public void onComplete() {
                presenter.updateMap();
                presenter.animateCameraToUser();
            }

            @Override
            public void onError(BackendlessFault fault) {
                presenter.updateMap();
                presenter.animateCameraToUser();
                Log.d("mytag", "Download error. More info: " + fault.toString() +
                        " Details: " + fault.getDetail() +
                        " Message: " + fault.getMessage() + " Code: " + fault.getCode());
                Toast.makeText(context, R.string.need_internet_to_refresh, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unsubscribe() {
                setSubscribed(false);
            }
        };
    }


    public List<BaseMarker> getAllMarkers(){
        return BaseMarker.listAll(BaseMarker.class);
    }

    public List<BaseMarker> getAllMarkerInRadius(BaseLocationTracker locationTracker) throws NullPointerException{
        double currentLat = locationTracker.getLastLocation().getLatitude();
        double currentLng = locationTracker.getLastLocation().getLongitude();
        return getAllMarkerInRadius(currentLat, currentLng);
    }

    public List<BaseMarker> getAllMarkerInRadius(double currentLat, double currentLng) throws NullPointerException{
        float[] results = new float[1];
        ArrayList<BaseMarker> finalMarkers = new ArrayList<>();
        List<BaseMarker> nearMarkers = BaseMarker.
                findWithQuery(BaseMarker.class,
                        "Select * from base_marker where lat between ? and ? and lng between ? and ?",
                        currentLat - 10 + "" , currentLat + 10 + "", currentLng - 10 + "", currentLng + 10 + "" );

        for(BaseMarker marker : nearMarkers){
            double lat = marker.getLat();
            double lng = marker.getLng();
            Location.distanceBetween(lat, lng, currentLat, currentLng, results);
            if(results[0] < CIRCLE_SEARCH_RADIUS){
                finalMarkers.add(marker);
            }
        }

        for(BaseMarker marker : finalMarkers){
        }

        return finalMarkers;
    }

    public boolean isMarkersDownloaded(){
        return getAllMarkers().size() != 0;
    }


    public CustomLocationListener getLocationListener(){
        return new CustomLocationListener() {

            private Location currentLocation;
            private boolean gpsAvailable, networkGpsAvailable;

            @Override
            public void onLocationChanged(Location newLocation) {
                this.currentLocation = newLocation;
                presenter.updateMap();
            }

            @Override
            public void onProviderDisabled(String provider) {
                if(provider.equals(LocationManager.GPS_PROVIDER)) {
                    gpsAvailable = false;
                } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                    networkGpsAvailable = false;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                if(provider.equals(LocationManager.GPS_PROVIDER)) {
                    gpsAvailable = true;
                } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                    networkGpsAvailable = true;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                boolean available = (status == AVAILABLE);
                if(provider.equals(LocationManager.GPS_PROVIDER)) {
                    gpsAvailable = available;
                } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                    networkGpsAvailable = available;
                }
            }


            public Location getCurrentLocation() {
                return currentLocation;
            }

            public void setCurrentLocation(Location currentLocation) {
                this.currentLocation = currentLocation;
            }

        };
    }
}

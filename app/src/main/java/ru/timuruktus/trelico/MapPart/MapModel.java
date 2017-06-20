package ru.timuruktus.trelico.MapPart;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

import ru.timuruktus.trelico.MapPart.Interafaces.BaseLocationTracker;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapModel;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.CustomLocationListener;
import ru.timuruktus.trelico.Markers.DownloadListener;
import ru.timuruktus.trelico.POJO.BaseMarker;
import ru.timuruktus.trelico.POJO.CustomGeoPoint;
import ru.timuruktus.trelico.R;

import static android.location.LocationProvider.AVAILABLE;

class MapModel implements BaseMapModel {



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
                Log.d("mytag", "DOwnload erorr. More info: " + fault.toString() +
                        " Details: " + fault.getDetail() +
                " Message: " + fault.getMessage() + " Code: " + fault.getCode());
                Toast.makeText(context, R.string.need_internet_to_download, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unsubscribe() {

            }
        };
    }


    public void buildLocationManager(){

    }

    public List<CustomGeoPoint> getAllMarkers(){
        return CustomGeoPoint.listAll(CustomGeoPoint.class);
    }

    public List<CustomGeoPoint> getAllMarkerInRadius(BaseLocationTracker locationTracker) throws NullPointerException{
        double currentLat = locationTracker.getLastLocation().getLatitude();
        double currentLng = locationTracker.getLastLocation().getLongitude();
        return getAllMarkerInRadius(currentLat, currentLng);
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

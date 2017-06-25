package ru.timuruktus.trelico.MapPart;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.timuruktus.trelico.MapPart.Interfaces.BaseLocationTracker;

public class LocationTracker implements BaseLocationTracker{

    private FusedLocationProviderClient fusedLocationClient;
    private Observable timer;
    private Disposable disposableTracking;
    private Location location;
    private GoogleMap googleMap;
    private Circle circle;
    private MapPresenter mapPresenter;

    public static final int TIMER_DELAY = 1000; // in Milliseconds
    public static final int NO_DELAY = 0;
    public static final int CIRCLE_SEARCH_RADIUS = 1000;
    public static final int FILL_COLOR = 0x44E49273;
    public static final int STROKE_COLOR = 0x55000000;

    public LocationTracker(FusedLocationProviderClient fusedLocationClient, MapPresenter mapPresenter) {
        this.fusedLocationClient = fusedLocationClient;
        this.mapPresenter = mapPresenter;

    }

    @Override
    public void start(GoogleMap googleMap) throws SecurityException{
        start(NO_DELAY, googleMap);
        this.googleMap = googleMap;
    }

    @Override
    public void start(int delay, GoogleMap googleMap) throws SecurityException{
        timer = Observable.interval(TIMER_DELAY + delay, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
        startTrackingLocation();
        this.googleMap = googleMap;
    }

    @Override
    public void setPause(int millis) {
        stop();
        start(millis, this.googleMap);
    }

    @Override
    public void stop() {
        if(disposableTracking != null) {
            disposableTracking.dispose();
        }
    }

    @Override
    public Location getLastLocation() {
        return location;
    }

    private void startTrackingLocation() throws SecurityException{
        disposableTracking = (Disposable) timer.subscribeWith(new DisposableObserver(){

            @Override
            public void onNext(@NonNull Object o) throws SecurityException{
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                LocationTracker.this.location = location;
                                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                                mapPresenter.refreshMarkers();
                                if(circle == null) {
                                    CircleOptions circleOptions = new CircleOptions()
                                            .center(currentPosition)
                                            .radius(CIRCLE_SEARCH_RADIUS)
                                            .fillColor(FILL_COLOR)
                                            .strokeColor(STROKE_COLOR);
                                    circle = googleMap.addCircle(circleOptions);
                                }else{
                                    circle.setCenter(currentPosition);
                                }
                            }
                        });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}

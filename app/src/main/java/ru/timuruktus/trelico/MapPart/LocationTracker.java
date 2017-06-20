package ru.timuruktus.trelico.MapPart;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseLocationTracker;

public class LocationTracker implements BaseLocationTracker{

    private FusedLocationProviderClient fusedLocationClient;
    private Observable timer;
    private Disposable disposableTracking;
    private Disposable disposableTimer;
    private Location location;

    public static final int TIMER_DELAY = 10000; // in Milliseconds
    public static final int NO_DELAY = 0;

    public LocationTracker(FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient = fusedLocationClient;

    }

    @Override
    public void start() throws SecurityException{
        start(NO_DELAY);
    }

    private void start(int delay) throws SecurityException{
        timer = Observable.interval(TIMER_DELAY + delay, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
        startTrackingLocation();
    }

    @Override
    public void setPause(int millis) {
        stop();
        start(millis);
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

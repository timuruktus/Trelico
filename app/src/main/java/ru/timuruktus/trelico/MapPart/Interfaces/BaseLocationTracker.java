package ru.timuruktus.trelico.MapPart.Interfaces;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

public interface BaseLocationTracker {


    void start(GoogleMap googleMap);
    void start(int delay, GoogleMap googleMap);
    void setPause(int millis);
    void stop();
    Location getLastLocation();
}

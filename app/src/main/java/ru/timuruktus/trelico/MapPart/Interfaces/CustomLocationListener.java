package ru.timuruktus.trelico.MapPart.Interfaces;

import android.location.Location;
import android.location.LocationListener;

public interface CustomLocationListener extends LocationListener {

    Location currentLocation = null;
    boolean gpsAvailable = false;
    boolean networkGpsAvailable = false;

    Location getCurrentLocation();
    void setCurrentLocation(Location currentLocation);
}

package ru.timuruktus.trelico.MapPart.Interafaces;

import android.location.Location;

public interface BaseLocationTracker {


    void start();
    void setPause(int millis);
    void stop();
    Location getLastLocation();
}

package ru.timuruktus.trelico.MapPart.Interafaces;

import android.content.Context;

import com.google.android.gms.maps.OnMapReadyCallback;

public interface BaseMapPresenter {

    // VIEW QUERIES
    void onCreateView();
    void onDestroy();
    OnMapReadyCallback onMapReady();
    void onResume();
    void onPause();
    void onRefreshFABClick();
    void onSettingsFABClick();

    void updateMap();
    Context askForContext();
    void animateCameraToUser();
}

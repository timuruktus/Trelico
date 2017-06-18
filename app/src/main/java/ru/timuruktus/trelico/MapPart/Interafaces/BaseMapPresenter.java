package ru.timuruktus.trelico.MapPart.Interafaces;

import com.google.android.gms.maps.OnMapReadyCallback;

public interface BaseMapPresenter {

    void onCreateView();
    void onDestroy();
    OnMapReadyCallback onMapReady();
}

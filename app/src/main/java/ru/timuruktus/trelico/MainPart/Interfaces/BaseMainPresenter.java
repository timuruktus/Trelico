package ru.timuruktus.trelico.MainPart.Interfaces;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.Serializable;

public interface BaseMainPresenter extends Serializable {

    String ARG_INFO = "Info";
    String ARG_PRESENTER = "Presenter";

    /**
     * Activity lifecycle mothods
     */
    void onCreate();
    void onDestroy();
    void onStop();
    void onResume();
    void onPause();
    void onStart();

    void setMenuItem(Fragment fragment);
    void changeFragment(Fragment fragment, boolean addToBackStack, boolean hideMenu, Serializable info);
    void changeFragment(Fragment fragment, boolean addToBackStack, boolean hideMenu);
    void changeFragment(Fragment fragment, boolean addToBackStack);
    void changeFragment(Fragment fragment, boolean addToBackStack, Serializable info);

    void clearBackStack();

    Activity getMainActivity();
}

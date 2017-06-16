package ru.timuruktus.trelico.MainPart.Interfaces;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;

import com.google.android.gms.maps.SupportMapFragment;

public interface BaseMainActivity {

    Context getAppContext();
    void setMenuItem(int resId);
    FragmentManager getFragmentManager();
    Activity getActivity();

}

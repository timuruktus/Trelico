package ru.timuruktus.trelico.SettingsPart.Interfaces;

import android.app.Activity;
import android.content.Context;

public interface BaseSettingsView {

    void hideGPSPart();
    void loadGPSPart();
    Context getContext();
    Activity getActivity();
}

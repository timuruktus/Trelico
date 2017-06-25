package ru.timuruktus.trelico.MapPart.Interfaces;

import android.app.Activity;
import android.content.Context;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;


public interface BaseMapView {


    Context getAppContext();
    BaseMainPresenter getMainPresenter();
    Activity getActivity();
}

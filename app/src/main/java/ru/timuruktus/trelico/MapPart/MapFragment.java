package ru.timuruktus.trelico.MapPart;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapView;
import ru.timuruktus.trelico.R;

public class MapFragment extends Fragment implements BaseMapView {


    private View rootView;
    private BaseMainPresenter mainPresenter;
    private BaseMapPresenter mapPresenter;
    private MapView mapView;
    private Context context;

    public static MapFragment newInstance(BaseMainPresenter mainPresenter){
        MapFragment fragment = new MapFragment();
        fragment.mainPresenter = mainPresenter;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =
                inflater.inflate(R.layout.map_fragment, container, false);
        context = rootView.getContext();

        mapPresenter = new MapPresenter(this);
        mapPresenter.onCreateView();

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(mapPresenter.onMapReady());

        return rootView;
    }



    public BaseMainPresenter getMainPresenter(){
        return mainPresenter;
    }

    @Override
    public Context getAppContext() {
        return context;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainPresenter.setMenuItem(this);
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapPresenter.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}

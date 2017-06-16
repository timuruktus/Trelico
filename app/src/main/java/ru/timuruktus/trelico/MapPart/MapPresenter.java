package ru.timuruktus.trelico.MapPart;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapModel;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapView;

class MapPresenter implements BaseMapPresenter {



    private BaseMapView view;
    private BaseMapModel model;
    private LatLng yekaterinburg = new LatLng(60.60570, 56.83892);

    MapPresenter(BaseMapView view) {
        this.view = view;
        model = new MapModel(this);

    }

    @Override
    public void onCreateView() {
        BaseMainPresenter mainPresenter = view.getMainPresenter();

    }


    private OnMapReadyCallback mapReadyCallback = googleMap -> {
        googleMap.addMarker(new MarkerOptions().position(yekaterinburg)
                .title("Marker in Yekaterinburg"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yekaterinburg));
    };

    @Override
    public void onDestroy() {

    }


}

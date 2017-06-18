package ru.timuruktus.trelico.MapPart;



import android.Manifest;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapModel;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapView;
import ru.timuruktus.trelico.Markers.CustomInfoWindowAdapter;
import ru.timuruktus.trelico.POJO.BaseMarker;

import static android.support.v4.content.PermissionChecker.PERMISSION_DENIED;
import static ru.timuruktus.trelico.MainPart.MainActivity.MY_PERMISSIONS_REQUEST_FINE_LOCATION;

class MapPresenter implements BaseMapPresenter {



    private BaseMapView view;
    private BaseMapModel model;
    private LatLng yekaterinburg = new LatLng(56.78149,60.67455);
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationClient;

    MapPresenter(BaseMapView view) {
        this.view = view;
        model = new MapModel(this);

    }

    @Override
    public void onCreateView() {
        BaseMainPresenter mainPresenter = view.getMainPresenter();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getAppContext());
    }


    private OnMapReadyCallback mapReadyCallback = googleMap -> {
        googleMap.addMarker(new MarkerOptions().position(yekaterinburg)
                .title("Marker in Yekaterinburg"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yekaterinburg));
    };

    @Override
    public void onDestroy() {

    }

    @Override
    public OnMapReadyCallback onMapReady() {
        return map -> {
            googleMap = map;
            int permissionCheck = ContextCompat.checkSelfPermission(view.getAppContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck == PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(view.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
//                    Log.d("mytag", "Haven't got a permission");
            } else {
//                    Log.d("mytag", "Have a permission");
                loadMap();
            }
        };
    }

    private void loadMap(){
//        Log.d("mytag", "loadMap()");
        CustomInfoWindowAdapter markerAdapter = new CustomInfoWindowAdapter(view.getActivity());
        try {
            googleMap.setMyLocationEnabled(true);
            // For dropping a marker at a point on the Map
            googleMap.addMarker(new MarkerOptions().position(yekaterinburg).title("Yekaterinburg").snippet("10% Скидка. Шатровая 6. Самый правый подъезд со стороны реки. Вход с ул. Шатровая. Арагац- лучший магазин по самым низким ценам! Заходите только сюда! Лучшие продукты и высокие скидки!!"));

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            googleMap.setInfoWindowAdapter(markerAdapter);
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();
                            LatLng currentPlace = new LatLng(lat,lng);
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPlace).zoom(16).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    });
        }catch(SecurityException ex){
            ex.printStackTrace();
        }
    }

    private boolean checkIfMarkerDownloaded(){
//        BaseMarker.listAll()
    }


}

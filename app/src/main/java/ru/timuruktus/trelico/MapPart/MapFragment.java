package ru.timuruktus.trelico.MapPart;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapView;
import ru.timuruktus.trelico.R;

import static android.support.v4.content.PermissionChecker.PERMISSION_DENIED;
import static ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter.ARG_PRESENTER;

public class MapFragment extends Fragment implements BaseMapView {


    private View rootView;
    private BaseMainPresenter mainPresenter;
    private BaseMapPresenter mapPresenter;
    MapView mapView;
    private GoogleMap googleMap;
    private Context context;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

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

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
//                anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
                int permissionCheck = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if(permissionCheck == PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(mainPresenter.getMainActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                }else {
                    loadMap();
                }

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        return rootView;
    }

    private void loadMap(){
        try {
            googleMap.setMyLocationEnabled(true);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }

        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadMap();
                } else {
                    Toast.makeText(context, R.string.please_give_permission, Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(mainPresenter.getMainActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

        }
    }

    public BaseMainPresenter getMainPresenter(){
        return mainPresenter;
    }

    @Override
    public Context getAppContext() {
        return rootView.getContext();
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

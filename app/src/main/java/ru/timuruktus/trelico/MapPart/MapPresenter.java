package ru.timuruktus.trelico.MapPart;



import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseLocationTracker;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapModel;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapPresenter;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapView;
import ru.timuruktus.trelico.MapPart.Interafaces.CustomLocationListener;
import ru.timuruktus.trelico.Markers.CustomInfoWindowAdapter;
import ru.timuruktus.trelico.Markers.MarkerBuilder;
import ru.timuruktus.trelico.Markers.MarkerModel;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static ru.timuruktus.trelico.MainPart.MainActivity.MY_PERMISSIONS_REQUEST_FINE_LOCATION;

class MapPresenter implements BaseMapPresenter {



    private BaseMapView view;
    private BaseMapModel model;
    private GoogleMap googleMap;
    private static final int DEFAULT_ZOOM = 16;
    private LocationManager locationManager;
    private CustomLocationListener locationListener;
    private BaseLocationTracker locationTracker;
    private BaseMainPresenter mainPresenter;
    private ArrayList<Marker> showedMarkers = new ArrayList<>();
    public static final int UPDATE_MAP_DELAY = 3000;

    MapPresenter(BaseMapView view) {
        this.view = view;
        model = new MapModel(this);

    }

    @Override
    public void onCreateView() {
        mainPresenter = view.getMainPresenter();
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getAppContext());
        locationManager = (LocationManager) view.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = model.getLocationListener();
        locationTracker = new LocationTracker(fusedLocationClient);
    }


    @Override
    public void onDestroy() {
        locationTracker.stop();
    }

    @Override
    public OnMapReadyCallback onMapReady() {
        return map -> {
            googleMap = map;
            CustomInfoWindowAdapter markerAdapter = new CustomInfoWindowAdapter(view.getActivity());
            googleMap.setInfoWindowAdapter(markerAdapter);
            int permissionCheck = ContextCompat.checkSelfPermission(view.getAppContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissionCheck == PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                locationTracker.start();
                prepareMap();
            }else{
                ActivityCompat.requestPermissions(view.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        };
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRefreshFABClick() {
//        MarkerModel markerModel = new MarkerModel();
//        markerModel.refreshAllMarkers();
//        clearAllShowedMarkers();
//        mainPresenter.changeFragment(new MapFragment(), false);
//        Snackbar
    }

    @Override
    public void onSettingsFABClick() {

    }

    private void prepareMap(){
//        Log.d("mytag", "prepareMap()");
        if(!model.isMarkersDownloaded()){
            MarkerModel markerModel = new MarkerModel();
            markerModel.downloadAllMarkers(model.getMarkersDownloadListener());
        }else{
            updateMap();
            animateCameraToUser();
        }

    }

    public void animateCameraToPosition(LatLng position){
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(DEFAULT_ZOOM).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void animateCameraToUser(){
        Location currentLocation = locationTracker.getLastLocation();
        if (currentLocation != null) {
            double lat = currentLocation.getLatitude();
            double lng = currentLocation.getLongitude();
            LatLng currentPlace = new LatLng(lat,lng);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPlace).zoom(DEFAULT_ZOOM).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void updateMap(){
        try {
            ArrayList<BaseMarker> markers = (ArrayList<BaseMarker>) model.getAllMarkerInRadius(locationTracker);
            clearAllShowedMarkers();
            for(BaseMarker marker : markers){
                Marker showedMarker = MarkerBuilder.showMarker(googleMap, marker);
                showedMarkers.add(showedMarker);
            }
            animateCameraToUser();
        }catch(NullPointerException ex){
            Handler handler = new Handler();
            Runnable task = () -> updateMap();
            handler.postDelayed(task, UPDATE_MAP_DELAY);
        }
    }

    private void clearAllShowedMarkers(){
        if(showedMarkers != null && showedMarkers.size() != 0){
            for(Marker marker : showedMarkers){
                marker.remove();
            }
        }
    }

    private void deleteAllSavedMarkers(){
        BaseMarker.deleteAll(BaseMarker.class);
    }

    @Override
    public Context askForContext() {
        return view.getAppContext();
    }








}

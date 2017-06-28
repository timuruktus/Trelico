package ru.timuruktus.trelico.MapPart;



import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.MapPart.Interfaces.BaseLocationTracker;
import ru.timuruktus.trelico.MapPart.Interfaces.BaseMapModel;
import ru.timuruktus.trelico.MapPart.Interfaces.BaseMapPresenter;
import ru.timuruktus.trelico.MapPart.Interfaces.BaseMapView;
import ru.timuruktus.trelico.MapPart.Interfaces.CustomLocationListener;
import ru.timuruktus.trelico.Markers.CustomInfoWindowAdapter;
import ru.timuruktus.trelico.Markers.MarkerBuilder;
import ru.timuruktus.trelico.Markers.MarkerModel;
import ru.timuruktus.trelico.POJO.BaseMarker;
import ru.timuruktus.trelico.R;
import ru.timuruktus.trelico.SettingsPart.SettingsFragment;

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
        locationTracker = new LocationTracker(fusedLocationClient, this);
    }


    @Override
    public void onDestroy() {
        locationTracker.stop();
    }

    @Override
    public OnMapReadyCallback onMapReady() {
        return map -> {
            googleMap = map;
            setMapStyle(R.raw.map_style_2);
            CustomInfoWindowAdapter markerAdapter = new CustomInfoWindowAdapter(view.getActivity());
            googleMap.setInfoWindowAdapter(markerAdapter);
            int permissionCheck = ContextCompat.checkSelfPermission(view.getAppContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissionCheck == PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                locationTracker.start(googleMap);
                prepareMap();
            }else{
                ActivityCompat.requestPermissions(view.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        };
    }

    private void setMapStyle(int rawId){
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            view.getAppContext(), rawId));
            if (!success) {
                Log.e("mytag", "Style parsing failed.");
            }
        }catch (Exception ex){
            Log.d("mytag", "Error while setting map style. Info: " + ex.getMessage() );
            ex.printStackTrace();
        }
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
        MarkerModel markerModel = new MarkerModel();
        markerModel.refreshAllMarkers(model.getMarkersRefreshListener(mainPresenter.getContainer()));
//        mainPresenter.changeFragment(new MapFragment(), false);
    }

    @Override
    public void onSettingsFABClick() {
        mainPresenter.changeFragment(SettingsFragment.newInstance(mainPresenter), false);
    }

    private void prepareMap(){
//        Log.d("mytag", "prepareMap()");
        MarkerModel markerModel = new MarkerModel();
        if(!model.isMarkersDownloaded()){
            markerModel.downloadAllMarkers(model.getMarkersDownloadListener());
        }else{
            markerModel.refreshAllMarkers(model.getMarkersRefreshListener(mainPresenter.getContainer()));
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
    public void refreshMarkersOnMap() throws NullPointerException {
        refreshMarkersOnMap(true);
    }

    @Override
    public void refreshMarkersOnMap(boolean softClear) throws NullPointerException {
        ArrayList<BaseMarker> markers = (ArrayList<BaseMarker>) model.getAllMarkerInRadius(locationTracker);
        clearAllShowedMarkers(softClear);
        for(BaseMarker marker : markers){
            Marker showedMarker = MarkerBuilder.showMarker(googleMap, marker);
            showedMarkers.add(showedMarker);
        }
    }

    @Override
    public void updateMap(){
        try {
            refreshMarkersOnMap();
            animateCameraToUser();
        }catch(NullPointerException ex){
            Handler handler = new Handler();
            Runnable task = () -> updateMap();
            handler.postDelayed(task, UPDATE_MAP_DELAY);
        }
    }

    private void clearAllShowedMarkers(boolean softClear){
        if(showedMarkers != null && showedMarkers.size() != 0){
            for(Marker marker : showedMarkers){
                if(!marker.isInfoWindowShown() || !softClear) {
                    marker.remove();
                }
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

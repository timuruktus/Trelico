package ru.timuruktus.trelico.MainPart;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainActivity;
import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.MapPart.MapFragment;
import ru.timuruktus.trelico.R;
import ru.timuruktus.trelico.Utils.Settings;
import ru.timuruktus.trelico.Utils.SettingsImpl;

public class MainActivity extends AppCompatActivity implements BaseMainActivity,
        ActivityCompat.OnRequestPermissionsResultCallback {


    private BaseMainPresenter presenter;
    private BottomNavigationView navigation;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        presenter = new MainPresenter(this);
        presenter.onCreate();

        Settings settings = new SettingsImpl();
        settings.initSettings(this);

//        navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setMenuItem(int resId) {
//        navigation.setSelectedItemId(resId);
    }

    @Override
    public FragmentManager getFragmentManager(){
        return super.getFragmentManager();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = item -> {
            switch (item.getItemId()) {
                case R.id.nav_map:

                    return true;
                case R.id.nav_settings:

                    return true;
            }
            return false;
        };


    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
//        Log.d("mytag", "onRequestPermissionsResult()");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Log.d("mytag", "Permission Granted");
                    presenter.changeFragment(MapFragment.newInstance(presenter), false);
                }else{
//                    Log.d("mytag", "Permission Denied");
                    Toast.makeText(this, R.string.denied_permission, Toast.LENGTH_LONG).show();
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                }
            }
        }
    }

    /**
     * Main activity lifetime = app lifetime.
     * @return app context;
     */

    @Override
    public Context getAppContext() {
        return this.getApplicationContext();
    }


}

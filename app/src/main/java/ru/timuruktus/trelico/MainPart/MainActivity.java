package ru.timuruktus.trelico.MainPart;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;


import com.google.android.gms.maps.SupportMapFragment;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainActivity;
import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.R;

public class MainActivity extends AppCompatActivity implements BaseMainActivity {


    private BaseMainPresenter presenter;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        presenter = new MainPresenter(this);
        presenter.onCreate();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setMenuItem(int resId) {
        navigation.setSelectedItemId(resId);
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



    /**
     * Main activity lifetime = app lifetime.
     * @return app context;
     */

    @Override
    public Context getAppContext() {
        return this.getApplicationContext();
    }


}

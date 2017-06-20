package ru.timuruktus.trelico.MainPart;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.backendless.Backendless;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.Serializable;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainActivity;
import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainModel;
import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.MapPart.MapFragment;
import ru.timuruktus.trelico.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;

class MainPresenter implements BaseMainPresenter {

    private BaseMainActivity activity;
    private BaseMainModel model;

    MainPresenter(BaseMainActivity activity) {
        this.activity = activity;
        model = new MainModel(this);
        configureBackendless(activity.getAppContext());
        configureFonts();
        loadFirstFragment();
    }

    private void loadFirstFragment(){
        changeFragment(MapFragment.newInstance(this), false);
    }


    @Override
    public void setMenuItem(Fragment fragment) {
        if(fragment instanceof MapFragment){
            activity.setMenuItem(R.id.nav_map);
        }else{
            activity.setMenuItem(0);
        }
    }

    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack, boolean hideMenu, Serializable info) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(addToBackStack) fragmentTransaction.addToBackStack(fragment.getClass().toString());
        Bundle args = new Bundle();
        if(info != null) args.putSerializable(ARG_INFO, info);
        fragment.setArguments(args);
        fragmentTransaction.setTransition(TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack, boolean hideMenu) {
        changeFragment(fragment, addToBackStack, hideMenu, null);
    }

    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        changeFragment(fragment, addToBackStack, false, null);
    }

    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack, Serializable info) {
        changeFragment(fragment, addToBackStack, false, info);
    }

    @Override
    public void clearBackStack() {
        FragmentManager fragmentManager = activity.getFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        while(count > 0){
            fragmentManager.popBackStack();
            count--;
        }
    }


    @Override
    public Activity getMainActivity() {
        return activity.getActivity();
    }

    @Override
    public View getContainer() {
        return activity.getActivity().findViewById(R.id.container);
    }


    private void configureBackendless(Context context){
        final String APP_ID = "195764CA-FB57-8617-FFD8-2729F0D17D00";
        final String API_KEY = "912669E3-B258-7FC0-FF94-A8249F8E2800";
        Backendless.initApp(context, APP_ID, API_KEY);
    }

    private void configureFonts(){
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }




    /**
     * Below this line- Activity lifecycle methods
     */

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }



}

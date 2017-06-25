package ru.timuruktus.trelico.SettingsPart;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.MapPart.MapFragment;
import ru.timuruktus.trelico.R;
import ru.timuruktus.trelico.SettingsPart.Interfaces.BaseSettingsPresenter;
import ru.timuruktus.trelico.SettingsPart.Interfaces.BaseSettingsView;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static ru.timuruktus.trelico.MainPart.MainActivity.MY_PERMISSIONS_REQUEST_FINE_LOCATION;

class SettingsPresenter implements BaseSettingsPresenter {

    private BaseSettingsView view;
    private BaseMainPresenter mainPresenter;

    SettingsPresenter(BaseSettingsView view, BaseMainPresenter mainPresenter) {
        this.view = view;
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void onCreate() {
        int permissionCheck = ContextCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PERMISSION_GRANTED) {
            view.hideGPSPart();
        }else{
            view.loadGPSPart();
        }
    }

    @Override
    public void onGPSButClick() {
        Toast.makeText(view.getContext(), R.string.user_denied_gps, Toast.LENGTH_LONG).show();
        ActivityCompat.requestPermissions(view.getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    @Override
    public void onBackBtnClick() {
        mainPresenter.changeFragment(MapFragment.newInstance(mainPresenter), false);
    }

    @Override
    public void onRateBtnClick() {
        final String appPackageName = view.getContext().getPackageName();
        try {
            view.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            view.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}

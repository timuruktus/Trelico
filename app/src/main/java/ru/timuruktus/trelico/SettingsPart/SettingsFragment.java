package ru.timuruktus.trelico.SettingsPart;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;
import ru.timuruktus.trelico.R;
import ru.timuruktus.trelico.SettingsPart.Interfaces.BaseSettingsPresenter;
import ru.timuruktus.trelico.SettingsPart.Interfaces.BaseSettingsView;

public class SettingsFragment extends Fragment implements BaseSettingsView {

    private View rootView;
    private Context context;
    private BaseSettingsPresenter presenter;
    private BaseMainPresenter mainPresenter;


    public static SettingsFragment newInstance(BaseMainPresenter mainPresenter){
        SettingsFragment fragment = new SettingsFragment();
        fragment.mainPresenter = mainPresenter;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =
                inflater.inflate(R.layout.settings_fragment, container, false);
        context = rootView.getContext();

        presenter = new SettingsPresenter(this, mainPresenter);
        presenter.onCreate();

        Button backBtn = (Button) rootView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> presenter.onBackBtnClick());

        Button rateBtn = (Button) rootView.findViewById(R.id.rateBtn);
        rateBtn.setOnClickListener(v -> presenter.onRateBtnClick());

        return rootView;
    }

    @Override
    public void hideGPSPart(){
        LinearLayout settingsGPSPart = (LinearLayout) rootView.findViewById(R.id.settingsGPSPart);
        settingsGPSPart.setVisibility(View.GONE);
    }

    @Override
    public void loadGPSPart() {
        Button gpsBtn = (Button) rootView.findViewById(R.id.gpsBtn);
        gpsBtn.setOnClickListener(v -> presenter.onGPSButClick());
    }
}

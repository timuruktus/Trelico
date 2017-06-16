package ru.timuruktus.trelico.MapPart;

import android.content.Context;


import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapModel;
import ru.timuruktus.trelico.MapPart.Interafaces.BaseMapPresenter;

class MapModel implements BaseMapModel {



    private BaseMapPresenter presenter;

    MapModel(BaseMapPresenter presenter) {
        this.presenter = presenter;
    }

}

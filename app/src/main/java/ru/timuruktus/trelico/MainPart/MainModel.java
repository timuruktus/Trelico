package ru.timuruktus.trelico.MainPart;

import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainModel;
import ru.timuruktus.trelico.MainPart.Interfaces.BaseMainPresenter;

class MainModel implements BaseMainModel {

    private BaseMainPresenter presenter;

    MainModel(BaseMainPresenter presenter) {
        this.presenter = presenter;
    }
}

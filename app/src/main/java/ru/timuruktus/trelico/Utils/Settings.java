package ru.timuruktus.trelico.Utils;

import android.content.Context;

public interface Settings {

    boolean isFirstSign();
    void setFirstSign(boolean firstSign);
    void initSettings(Context con);
}

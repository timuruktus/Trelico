package ru.timuruktus.trelico.Markers;

import com.backendless.exceptions.BackendlessFault;

public interface DownloadListener {

    boolean isSubscribed = true;

    void onStart();
    void onComplete();
    void onError(BackendlessFault fault);
    void unsubscribe();

}

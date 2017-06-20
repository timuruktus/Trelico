package ru.timuruktus.trelico.Markers;

import com.backendless.exceptions.BackendlessFault;

public abstract class DownloadListener {

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    private boolean subscribed = true;

    public abstract void onStart();
    public abstract void onComplete();
    public abstract void onError(BackendlessFault fault);
    public abstract void unsubscribe();

}

package ru.timuruktus.trelico.Markers;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import ru.timuruktus.trelico.POJO.BaseMarker;

public interface BaseMarkerModel{


    void downloadAllMarkers(MarkerDownloadListener listener);
}

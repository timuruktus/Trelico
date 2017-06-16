package ru.timuruktus.trelico.Utils;

import java.text.NumberFormat;

public class GeoUtils {

    public static double distanceBetweenPoints(double lng1, double lat1, double lng2, double lat2){
        return 111.2 * Math.sqrt( (lng1 - lng2)*(lng1 - lng2) + (lat1 - lat2)*Math.cos(Math.PI*lng1/180)*(lat1 - lat2)*Math.cos(Math.PI*lng1/180));
    }
}

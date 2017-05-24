package com.och.flightbook.model;

import java.io.Serializable;

/**
 * Created by o.chauvie on 16/02/2015.
 */
public class GpsPosition  implements Serializable  {

    private double lat;
    private double lon;
    private double alt;

    public GpsPosition(double lat, double lon, double alt) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }
}

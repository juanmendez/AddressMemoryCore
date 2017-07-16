package info.juanmendez.mapmemorycore.models;

/**
 * Created by Juan Mendez on 7/16/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LatLon {
    long lat;
    long lon;

    public LatLon() {
    }

    public LatLon(long lat, long lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public  boolean isInitialized(){
        return SubmitError.initialized(lat) && SubmitError.initialized(lon);
    }
}
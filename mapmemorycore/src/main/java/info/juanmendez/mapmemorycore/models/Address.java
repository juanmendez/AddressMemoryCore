package info.juanmendez.mapmemorycore.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */


public class Address extends RealmObject{

    @PrimaryKey
    private long addressId;

    @Index
    String name;


    String address1;
    String address2;


    @Index
    int timesVisited;

    @Index
    Date dateUpdated;

    //this is a google id if we were to get it from autocomplete
    String mapId;

    long lat;
    long lon;
    String url;
    String photoUrl;

    public Address() {
        if( dateUpdated == null ){
            dateUpdated = new Date();
        }
    }

    public Address(long addressId) {
        this.addressId = addressId;
    }

    public long getAddressId() {
        return addressId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void updateDateModified() {
        this.dateUpdated = new Date();
    }

    public int getTimesVisited() {
        return timesVisited;
    }

    public void setTimesVisited(int timesVisited) {
        this.timesVisited = timesVisited;
    }
}

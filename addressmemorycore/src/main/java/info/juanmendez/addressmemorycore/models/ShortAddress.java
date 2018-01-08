package info.juanmendez.addressmemorycore.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */


public class ShortAddress extends RealmObject{

    @PrimaryKey
    private long mAddressId;

    @Index
    private String mName = "";

    private String mAddress1 = "";
    private String mAddress2 = "";

    @Index
    private int mTimesVisited;

    @Index
    private Date mDateUpdated;

    //this is a google id if we were to get it from autocomplete
    private String mMapId = "";

    private double mLat;
    private double mLon;
    private String mUrl;
    private String mPhotoLocation = "";

    private Commute mCommute = new Commute();

    public ShortAddress() {
        if( mDateUpdated == null ){
            mDateUpdated = new Date();
        }
    }

    public ShortAddress(long addressId) {
        mAddressId = addressId;
    }

    public void setAddressId(long addressId) {
        mAddressId = addressId;
    }

    public void setDateUpdated(Date dateUpdated) {
        mDateUpdated = dateUpdated;
    }

    public long getAddressId() {
        return mAddressId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = SubmitError.emptyOrNull(name)?"":name;
    }

    public String getAddress1() {
        return mAddress1;
    }

    public void setAddress1(String address1) {
        mAddress1 = SubmitError.emptyOrNull(address1)?"":address1;
    }

    public String getAddress2() {
        return mAddress2;
    }

    public void setAddress2(String address2) {
        mAddress2 =  SubmitError.emptyOrNull(address2)?"":address2;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getPhotoLocation() {
        return mPhotoLocation;
    }

    public void setPhotoLocation(String photoLocation) {
        mPhotoLocation = photoLocation;
    }

    public String getMapId() {
        return mMapId;
    }

    public void setMapId(String mapId) {
        mapId = SubmitError.emptyOrNull(mapId)?"":mapId;
    }

    public Date getDateUpdated() {
        return mDateUpdated;
    }

    public void updateDateModified() {
        mDateUpdated = new Date();
    }

    public int getTimesVisited() {
        return mTimesVisited;
    }

    public void setTimesVisited(int timesVisited) {
        mTimesVisited = timesVisited;
    }

    public Commute getCommute() {
        return mCommute;
    }

    public void setCommute(Commute commute) {
        mCommute = commute;
    }
}
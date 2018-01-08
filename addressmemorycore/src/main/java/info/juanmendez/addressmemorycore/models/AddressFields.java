package info.juanmendez.addressmemorycore.models;

/**
 * Created by Juan Mendez on 7/5/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressFields {

    public static final String ADDRESSID = "mAddressId";

    public static final String  NAME = "mName";
    public static final String ADDRESS1 = "mAddress1";
    public static final String ADDRESS2 = "mAddress2";

    public static final String  LL = "latLon";
    public static final String  URL = "mUrl";
    public static final String  PHOTOURL = "mPhotoLocation";

    public static final String DATEUPDATED = "mDateUpdated";
    public static final String TIMESVISITED = "mTimesVisited";
    public static final String MAPID = "mMapId";

    public static final String COMMUTE_TYPE = "mCommuteType";
    public static final String COMMUTE_TOLLS = "mCommuteTolls";
    public static final String COMMUTE_HIGHWAY = "mCommuteHighway";

    public static final String[] sColumns = new String[]{ADDRESSID, NAME,
            ADDRESS1, ADDRESS2, DATEUPDATED,
            MAPID, PHOTOURL, TIMESVISITED, COMMUTE_TYPE, COMMUTE_TOLLS, COMMUTE_HIGHWAY };
}

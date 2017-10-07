package info.juanmendez.addressmemorycore.models;

import io.realm.RealmObject;


/**
 * Created by Juan Mendez on 10/6/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class Commute extends RealmObject {
    public static final String DRIVING = "d";
    public static final String BICYCLE = "b";
    public static final String WALKING = "w";
    public static final String PUBLICTRANSPORTATION = "t";
    public static final String UNDECIDED = "u";

    private String type = UNDECIDED;
    private Boolean xpressway = true;
    private Boolean tolls = true;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getXpressway() {
        return xpressway;
    }

    public void setXpressway(Boolean xpressway) {
        this.xpressway = xpressway;
    }

    public Boolean getTolls() {
        return tolls;
    }

    public void setTolls(Boolean tolls) {
        this.tolls = tolls;
    }
}
